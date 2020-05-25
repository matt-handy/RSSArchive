package handy.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import handy.common.GMSEC.Log;
import handy.common.GMSEC.Log.LogLevel;
import handy.rssarchive.config.ArticleInfo;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;
import handy.rssarchive.file.FileUtil;
import handy.rssarchive.html.ImageTagCleaner;

public class ArticleAccessHelper {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy hh");

	public static String getText(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}

	public static Document getXMLDocumentFromURL(String url) throws Exception {
		String page = getText(url);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(page)));
		return document;
	}

	public static NodeList getItemList(Document rssFeed) {
		return rssFeed.getElementsByTagName("item");
	}

	public static ArticleInfo getArticleInfo(Node articleItem, SiteConfig config) {
		String title = null;
		String author = null;
		Date date = null;
		String dateStr = null;
		String url = null;
		String description = null;

		if (config.authorTag == null) {
			author = config.name;
		}

		for (int jdx = 0; jdx < articleItem.getChildNodes().getLength(); jdx++) {
			Node metadata = articleItem.getChildNodes().item(jdx);
			// System.out.println(metadata.getNodeName());
			if (metadata.getNodeName().equalsIgnoreCase("title")) {
				title = metadata.getTextContent();
			}

			String targetLinkTagName = "guid";// Default
			if (config.linkTag != null) {
				targetLinkTagName = config.linkTag;
			}
			if (metadata.getNodeName().equalsIgnoreCase(targetLinkTagName)) {
				url = metadata.getTextContent();
			}

			if (metadata.getNodeName().equalsIgnoreCase("pubDate")) {
				try {
					dateStr = metadata.getTextContent();
					date = config.format.parse(dateStr);
					dateStr = DATE_FORMAT.format(date);
				} catch (DOMException | ParseException e) {
					e.printStackTrace();
				}

			}

			if (config.authorTag != null) {
				if (metadata.getNodeName().equalsIgnoreCase(config.authorTag)) {
					author = metadata.getTextContent();
				}
			}

			if (metadata.getNodeName().equalsIgnoreCase("description")) {
				description = metadata.getTextContent();
			}

		}

		if (title != null && date != null && url != null) {
			if (config.processor != null) {
				url = config.processor.adjustURL(url);
			}
			ArticleInfo ai = new ArticleInfo(title, dateStr, author, url, description, null);
			ai.addDate(date);
			return ai;
		} else {
			Log.getInstance().log("Error parsing article data: Title=" + title + " Date=" + date + " url=" + url,
					LogLevel.WARNING);
			return null;
		}

	}

	public static void rssRecorder(Document rssFeed, String targetDir, SiteConfig config, MasterConfig mConfig) {
		NodeList articleList = getItemList(rssFeed);
		for (int idx = 0; idx < articleList.getLength(); idx++) {
			Node articleItem = articleList.item(idx);
			ArticleInfo aInfo = getArticleInfo(articleItem, config);
			boolean outcome = FileUtil.writeRawHTMLRecord(aInfo.title, aInfo.date, aInfo.url, targetDir);
			if (!outcome) {
				// If we already have saved this HTML or can't write the
				// HTML, nothing here matters
				continue;
			}
			String filename = targetDir + FileUtil.getFileSep()
					+ FileUtil.articleFolderNameGenerator(aInfo.title, aInfo.date);

			if (mConfig.enableChrome) {
				try {
					// Invoke pdf generation here
					String invokeCommand = mConfig.chromeInvoke + filename + FileUtil.getFileSep()
							+ FileUtil.articleFolderNameGenerator(aInfo.title, aInfo.date) + ".pdf " + aInfo.url;
					Process p = Runtime.getRuntime().exec(invokeCommand);
					int exitVal = p.waitFor();
					Log.getInstance().log("Generate PDF: " + invokeCommand, LogLevel.DEBUG);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			String processedText = null;
			if (config.nativeRSSContent) {
				processedText = aInfo.description;
			} else {
				if (config.processor != null && config.processor.canProcess(aInfo.url)) {
					try {
						processedText = config.processor.process(ArticleAccessHelper.getText(aInfo.url));
					} catch (Exception e) {
						Log.getInstance().log("Was not able to process: " + aInfo.url + e.toString(), LogLevel.WARNING);
					}
				}
			}

			if (processedText != null) {
				// Strip out image tags and write images to directory
				processedText = ImageTagCleaner.imageTagCleaner(processedText, filename);

				// Strip tags and write text
				processedText = FileUtil.processAndWriteText(aInfo.title, aInfo.date, processedText, filename);
			}

			outcome &= writeXMLArticle(filename, aInfo.title, aInfo.pubDateStr, aInfo.url, aInfo.description,
					aInfo.date, processedText, aInfo.author);
			/*
			 * } else { Log.getInstance().log("Error parsing article data: Title=" + title +
			 * " Date=" + date + " url=" + url, LogLevel.WARNING); }
			 */
		}
	}

	public static boolean writeXMLArticle(String filename, String title, String date, String url, String description,
			Date dateObj, String processedText, String author) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("article");
			doc.appendChild(rootElement);

			Element titleEl = doc.createElement("title");
			titleEl.setTextContent(title);
			rootElement.appendChild(titleEl);

			Element authorEl = doc.createElement("author");
			authorEl.setTextContent(author);
			rootElement.appendChild(authorEl);

			Element dateEl = doc.createElement("date");
			dateEl.setTextContent(date);
			rootElement.appendChild(dateEl);

			Element urlEl = doc.createElement("url");
			urlEl.setTextContent(url);
			rootElement.appendChild(urlEl);

			Element descriptionEl = doc.createElement("description");
			descriptionEl.setTextContent(description);
			rootElement.appendChild(descriptionEl);

			if (processedText != null) {
				Element processedTextEl = doc.createElement("processedText");
				processedTextEl.setTextContent(processedText);
				rootElement.appendChild(processedTextEl);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(
					filename + FileUtil.getFileSep() + FileUtil.articleFolderNameGenerator(title, dateObj) + ".xml"));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}

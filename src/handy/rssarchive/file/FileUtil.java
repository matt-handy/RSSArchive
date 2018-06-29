package handy.rssarchive.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import handy.rssarchive.html.BasicTagCleaner;
import handy.rssarchive.html.BlockquoteCleaner;
import handy.rssarchive.html.HeaderTagCleaner;
import handy.rssarchive.html.ImageTagCleaner;
import handy.rssarchive.html.ParagraphTagCleaner;
import handy.rssarchive.html.ScriptCleaner;
import handy.xml.ArticleAccessHelper;

public class FileUtil {

	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	public static String articleFolderNameGenerator(String title, Date date) {
		title = title.replaceAll(":", "-");
		title = title.replaceAll("/", "");
		title = title.replaceAll("\"", "-");
		title = title.replaceAll(" ", "_");

		int targetIdx = 10;
		if (targetIdx > title.length() + 1) {
			targetIdx = title.length() - 1;
		}

		return df.format(date) + "_" + title.substring(0, targetIdx);
	}

	public static boolean writeRawHTMLRecord(String title, Date date, String url, String targetDir) {
		String rootName = articleFolderNameGenerator(title, date);
		String dirName = targetDir + "\\" + rootName;

		File folder = new File(dirName);
		if (!folder.exists()) {
			folder.mkdirs();
			try {
				PrintWriter writer = new PrintWriter(dirName + "\\htmlDump.txt");
				writer.write(ArticleAccessHelper.getText(url));
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else {
			return false;
		}

		return true;
	}

	public static String processAndWriteText(String title, Date date, String text, String targetDir) {
		text = ParagraphTagCleaner.cleanParagraph(text);
		text = BasicTagCleaner.cleanAllBasic(text);
		text = BlockquoteCleaner.cleanQuote(text);
		text = HeaderTagCleaner.headerTagCleaner(text);
		text = ScriptCleaner.cleanScript(text);
		
		try {
			PrintWriter writer = new PrintWriter(targetDir + "\\processedText.txt");
			writer.write(text);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return text;
	}

	public static String readFile(String pathname) throws IOException {

		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int) file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");

		try {
			while (scanner.hasNextLine()) {
				if (scanner.hasNextLine()) {
					fileContents.append(scanner.nextLine() + lineSeparator);
				} else {
					fileContents.append(scanner.nextLine());
				}
			}
		} finally {
			scanner.close();
		}
		return fileContents.toString();
	}

	public static boolean readBinaryFileFromUrl(String url, String targetFilename) throws Exception {
		if(!url.contains("http")){
			url = "http:" + url;
		}
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		int contentLength = uc.getContentLength();
		if (contentLength == -1) {
			throw new IOException("Invalid file returned");
		}
		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}
		in.close();

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		}

		FileOutputStream out = new FileOutputStream(targetFilename);
		out.write(data);
		out.flush();
		out.close();
		return true;
	}

}

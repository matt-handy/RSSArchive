package handy.rssarchive.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import handy.rssarchive.file.FileUtil;

public class GUIData {

	private MasterConfig mConfig;
	private String rootDirStr;
	
	private Map<SiteConfig, Set<ArticleInfo>> allMetaData = new HashMap<SiteConfig, Set<ArticleInfo>>();
	private Map<String, Set<ArticleInfo>> authorsToArticles = new HashMap<String, Set<ArticleInfo>>();
	private Map<String, ArticleInfo> articleNameToArticles = new HashMap<String, ArticleInfo>();
	
	public GUIData(MasterConfig mConfig, String rootDirStr){
		this.mConfig = mConfig;
		this.rootDirStr = rootDirStr;
	}
	
	public void load() throws ParserConfigurationException, SAXException, IOException{
		for(SiteConfig site : mConfig.configs){
			File[] directories = new File(rootDirStr + "\\" + site.folder).listFiles(File::isDirectory);
			Set<ArticleInfo> articles = new HashSet<ArticleInfo>();
			for(File file : directories){
				File[] xml = file.listFiles(FileUtil.xmlFilter);
				if(xml.length == 1){
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					InputStream in = new FileInputStream(xml[0]);
					Document document = builder.parse(new InputSource(new InputStreamReader(in)));
					
					NodeList siteList = document.getElementsByTagName("article");
					
					for(int idx = 0; idx < siteList.getLength(); idx++){
						Node articleItem = siteList.item(idx);
						String title = null;
						String description = null;
						String url = null;
						String author = null;
						String pubDateStr = null;
						String processedText = null;
						
						for(int jdx = 0; jdx < articleItem.getChildNodes().getLength(); jdx++){
							Node metadata = articleItem.getChildNodes().item(jdx);
							if(metadata.getNodeName().equalsIgnoreCase("title")){
								title = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("description")){
								description = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("url")){
								url = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("author")){
								author = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("date")){
								pubDateStr = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("processedText")){
								processedText = metadata.getTextContent();
							}
						}
						
						ArticleInfo aInfo = new ArticleInfo(title, pubDateStr, author, url, description, processedText);
						articles.add(aInfo);
						
						if(title != null){
							articleNameToArticles.put(title, aInfo);
							
							if(author != null){
								if(authorsToArticles.containsKey(author)){
									authorsToArticles.get(author).add(aInfo);
								}else{
									Set<ArticleInfo> authorArticles = new HashSet<ArticleInfo>();
									authorArticles.add(aInfo);
									authorsToArticles.put(author, authorArticles);
								}
							}
						}
						
						
					}
				}
			}
			allMetaData.put(site, articles);
			//System.out.println(authorsToArticles.get("Sandra Laville").size());
		}
	}

	public Map<SiteConfig, Set<ArticleInfo>> getAllMetaData() {
		return allMetaData;
	}

	public Map<String, Set<ArticleInfo>> getAuthorsToArticles() {
		return authorsToArticles;
	}

	public Map<String, ArticleInfo> getArticleNameToArticles() {
		return articleNameToArticles;
	}
}

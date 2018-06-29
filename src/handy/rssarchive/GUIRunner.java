package handy.rssarchive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import handy.rssarchive.config.ArticleInfo;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;

public class GUIRunner {

	static FilenameFilter textFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			if(name.contains(".xml")){
				return true;
			}else{
				return false;
			}
		}
	};
	
	public static void main(String[] args) throws Exception {
		String rootDirStr = args[0];
		File rootDirectory = new File(rootDirStr);
		if(!rootDirectory.exists() || !rootDirectory.isDirectory()){
			System.out.println("Specified target directory is not valid");
			return;
		}
		
		MasterConfig masterConfig = new MasterConfig("config\\config.xml");
		
		Map<SiteConfig, Set<ArticleInfo>> allMetaData = new HashMap<SiteConfig, Set<ArticleInfo>>();
		for(SiteConfig site : masterConfig.configs){
			File[] directories = new File(rootDirStr + "\\" + site.folder).listFiles(File::isDirectory);
			Set<ArticleInfo> articles = new HashSet<ArticleInfo>();
			for(File file : directories){
				File[] xml = file.listFiles(textFilter);
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
							if(metadata.getNodeName().equalsIgnoreCase("pubDateStr")){
								pubDateStr = metadata.getTextContent();
							}
							if(metadata.getNodeName().equalsIgnoreCase("processedText")){
								processedText = metadata.getTextContent();
							}
						}
						
						ArticleInfo aInfo = new ArticleInfo(title, pubDateStr, author, url, description, processedText);
						articles.add(aInfo);
					}
				}
			}
			allMetaData.put(site, articles);
		}

	}

}

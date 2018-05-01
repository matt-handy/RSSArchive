package handy.rssarchive;
import java.io.File;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;
import handy.xml.ArticleAccessHelper;

public class Runner {
	public static void main(String[] args) throws Exception {
		if(args.length != 1){
			System.out.println("Please specify target directory");
			return;
		}
		
		String rootDirStr = args[0];
		File rootDirectory = new File(rootDirStr);
		if(!rootDirectory.exists() || !rootDirectory.isDirectory()){
			System.out.println("Specified target directory is not valid");
			return;
		}
		
		MasterConfig masterConfig = new MasterConfig("config\\config.xml");
		
		while(true){
			for(SiteConfig config : masterConfig.configs){
				Document rssFeed = ArticleAccessHelper.getXMLDocumentFromURL(config.url);
				ArticleAccessHelper.rssRecorder(rssFeed, rootDirStr + "\\" + config.folder, config);
			}
			
			System.out.println("Sleeping for hours: " + masterConfig.refreshHours);
			System.out.println((new Date()).toString());
			Thread.sleep(masterConfig.refreshHours * 3600 * 1000);
		}
		
    }
}

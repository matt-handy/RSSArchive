package handy.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import handy.rssarchive.config.ArticleInfo;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;
import handy.rssarchive.file.FileUtil;

class ArticleAccessHelperTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
		MasterConfig masterConfig = new MasterConfig("config" + FileUtil.getFileSep() + "config.xml");
		
		for (SiteConfig config : masterConfig.configs) {
			int docCount = 0;
			try {
				Document rssFeed = ArticleAccessHelper.getXMLDocumentFromURL(config.url);
				NodeList articleList = ArticleAccessHelper.getItemList(rssFeed);
				for (int idx = 0; idx < articleList.getLength(); idx++) {
					Node articleItem = articleList.item(idx);
					ArticleInfo ai = ArticleAccessHelper.getArticleInfo(articleItem, config);
					docCount++;
					try { 
			            new URL(ai.url).toURI(); 
			        } 
			        catch (Exception e) { 
			        	fail("Invalid URL: " + ai.url); 
			        } 
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
			assertTrue(docCount > 0, "Failing for site: " + config.name);
			
		}
	}

}

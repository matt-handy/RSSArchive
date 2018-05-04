package handy.rssarchive.config.test;

import static org.junit.Assert.*;

import org.junit.Test;

import handy.rssarchive.config.MasterConfig;

public class MasterConfigTest {

	@Test
	public void test() {
		MasterConfig masterConfig = new MasterConfig("testData"
				+ "\\config.xml");
		assertTrue(masterConfig.refreshHours == 1);
		
		assertTrue(masterConfig.configs.size() == 4);
		
		assertTrue(masterConfig.configs.get(0).folder.equals("cnn"));
		assertTrue(masterConfig.configs.get(0).name.equals("CNN"));
		assertTrue(masterConfig.configs.get(0).url.equals("http://rss.cnn.com/rss/cnn_latest.rss"));
		assertFalse(masterConfig.configs.get(0).nativeRSSContent);
		
		assertTrue(masterConfig.configs.get(1).folder.equals("financialsense"));
		assertTrue(masterConfig.configs.get(1).name.equals("Financial Sense"));
		assertTrue(masterConfig.configs.get(1).url.equals("http://feeds.feedburner.com/fso?format=xml"));
		assertFalse(masterConfig.configs.get(1).nativeRSSContent);
		
		assertTrue(masterConfig.configs.get(2).folder.equals("zerohedge"));
		assertTrue(masterConfig.configs.get(2).name.equals("Zerohedge"));
		assertTrue(masterConfig.configs.get(2).url.equals("http://feeds.feedburner.com/zerohedge/feed"));
		assertTrue(masterConfig.configs.get(2).nativeRSSContent);
		
		assertTrue(masterConfig.configs.get(3).folder.equals("theguardian"));
		assertTrue(masterConfig.configs.get(3).name.equals("The Guardian"));
		assertTrue(masterConfig.configs.get(3).url.equals("https://www.theguardian.com/uk/rss"));
		assertFalse(masterConfig.configs.get(3).nativeRSSContent);
	}

}

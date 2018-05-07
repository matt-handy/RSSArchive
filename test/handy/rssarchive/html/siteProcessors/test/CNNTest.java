package handy.rssarchive.html.siteProcessors.test;

import static org.junit.Assert.*;

import org.junit.Test;

import handy.rssarchive.html.siteProcessors.CNNProcessor;
import handy.rssarchive.html.siteProcessors.FinancialSenseProcessor;
import handy.rssarchive.html.siteProcessors.SiteProcessor;
import handy.xml.ArticleAccessHelper;

public class CNNTest {

	@Test
	public void test() {
		String url = "https://www.cnn.com/2018/05/04/politics/donald-trump-speech-nra/index.html";
		SiteProcessor processor = CNNProcessor.getInstance();
		String adjustedUrl = processor.adjustURL(url);
		
		assertTrue(adjustedUrl.equals("https://www.cnn.com/2018/05/04/politics/donald-trump-speech-nra/index.html"));
		
		try {
			String article = ArticleAccessHelper.getText(adjustedUrl);
			processor.process(article);
			//System.out.println(processor.process(article));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

}

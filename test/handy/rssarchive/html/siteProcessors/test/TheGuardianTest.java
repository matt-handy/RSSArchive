package handy.rssarchive.html.siteProcessors.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import handy.rssarchive.html.siteProcessors.SiteProcessor;
import handy.rssarchive.html.siteProcessors.TheGuardianProcessor;
import handy.xml.ArticleAccessHelper;

public class TheGuardianTest {

	@Test
	public void test() {
		String url = "https://www.theguardian.com/us-news/2018/may/06/melania-trump-steps-out-of-donalds-shadow-after-a-year-and-a-half";
		SiteProcessor processor = TheGuardianProcessor.getInstance();
		String adjustedUrl = processor.adjustURL(url);
		
		assertTrue(adjustedUrl.equals("https://www.theguardian.com/us-news/2018/may/06/melania-trump-steps-out-of-donalds-shadow-after-a-year-and-a-half"));
		
		try {
			String article = ArticleAccessHelper.getText(adjustedUrl);
			processor.process(article);
			System.out.println(processor.process(article));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

}

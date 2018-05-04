package handy.rssarchive.html.siteProcessors.test;

import static org.junit.Assert.*;

import org.junit.Test;

import handy.rssarchive.html.siteProcessors.FinancialSenseProcessor;
import handy.rssarchive.html.siteProcessors.SiteProcessor;
import handy.xml.ArticleAccessHelper;

public class FinancialSenseTest {

	@Test
	public void test() {
		String url = "https://www.financialsense.com/przemyslaw-radomski-cfa/golds-bullish-vs-bearish-reversals-which-should-you-trust";
		SiteProcessor processor = FinancialSenseProcessor.getInstance();
		String adjustedUrl = processor.adjustURL(url);
		
		assertTrue(adjustedUrl.equals("https://www.financialsense.com/print/przemyslaw-radomski-cfa/golds-bullish-vs-bearish-reversals-which-should-you-trust"));
		
		try {
			String article = ArticleAccessHelper.getText(adjustedUrl);
			System.out.println(processor.process(article));
		} catch (Exception e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

}

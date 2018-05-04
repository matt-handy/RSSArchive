package handy.rssarchive.html.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import handy.rssarchive.html.BasicTagCleaner;

public class AHRefTest {

	@Test
	public void test() {
		String hrefIsolated = "<a href=\"https://www.zerohedge.com/news/2018-04-30/trump-should-win-nobel-peace-prize-south-koreas-moon\"><strong>Nobel Peace Prize</strong></a>";
		String isolatedStrongTags = BasicTagCleaner.cleanHref(hrefIsolated);
		assertTrue(isolatedStrongTags.equals("<strong>Nobel Peace Prize</strong>"));
		
		
		String hrefSample = "<p>South Korean President Moon Jae-in, who suggested on Monday that Donald Trump deserves the <a href=\"https://www.zerohedge.com/news/2018-04-30/trump-should-win-nobel-peace-prize-south-koreas-moon\"><strong>Nobel Peace Prize</strong></a>...</p><p>";
		String hrefClean = BasicTagCleaner.cleanHref(hrefSample);
		String expected = "<p>South Korean President Moon Jae-in, who suggested on Monday that Donald Trump deserves the <strong>Nobel Peace Prize</strong>...</p><p>";
		assertTrue(hrefClean.equals(expected));	
	}

}

package handy.rssarchive.html.test;

import static org.junit.Assert.*;

import org.junit.Test;

import handy.rssarchive.html.BasicTagCleaner;

public class BasicCleanTest {

	@Test
	public void test() {
		String hrefSample = "<p>South Korean President Moon Jae-in, <em>who</em> suggested on Monday that Donald Trump deserves the <a href=\"https://www.zerohedge.com/news/2018-04-30/trump-should-win-nobel-peace-prize-south-koreas-moon\"><strong>Nobel Peace Prize</strong></a>...</p><p>";
		String hrefClean = BasicTagCleaner.cleanAllBasic(hrefSample);
		String expected = "<p>South Korean President Moon Jae-in, who suggested on Monday that Donald Trump deserves the Nobel Peace Prize...</p><p>";
		assertTrue(hrefClean.equals(expected));
	}

}

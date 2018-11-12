package handy.rssarchive.html.siteProcessors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handy.rssarchive.html.BasicTagCleaner;

public class FinancialSenseProcessor extends SiteProcessor{
	private static FinancialSenseProcessor instance = new FinancialSenseProcessor();
	
	private FinancialSenseProcessor(){
		
	}
	
	public static FinancialSenseProcessor getInstance(){
		return instance;
	}
	
	public String process(String html) {
		String contentTag = "<article";
		String endContentTag = "</article>";
		String removedHeader = html.substring(html.indexOf(contentTag));
		String pureContent = removedHeader.substring(0, removedHeader.indexOf(endContentTag));
		
		Pattern pattern = Pattern.compile("<span class=\"staff name\">.*</span>");
		Matcher matcher = pattern.matcher(html);

		if (matcher.find()) {
			String authorTag = matcher.group();
			authorTag = authorTag.substring(0, authorTag.indexOf("</span>"));
			String authorTagIsolated = BasicTagCleaner.cleanAllBasic(authorTag);
			
			pureContent = pureContent + System.getProperty("line.separator") + authorTagIsolated;
		}
		
		return pureContent;
	}

	public String adjustURL(String url) {
		return url;
	}
}

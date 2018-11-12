package handy.rssarchive.html.siteProcessors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinancialSenseProcessorLegacy extends SiteProcessor{
	private static FinancialSenseProcessorLegacy instance = new FinancialSenseProcessorLegacy();
	
	private FinancialSenseProcessorLegacy(){
		
	}
	
	public static FinancialSenseProcessorLegacy getInstance(){
		return instance;
	}
	
	public String process(String html) {
		String contentTag = "<div class=\"content\">";
		String endContentTag = "</div>";
		String removedHeader = html.substring(html.indexOf(contentTag) + contentTag.length());
		String pureContent = removedHeader.substring(0, removedHeader.indexOf(endContentTag));
		
		Pattern pattern = Pattern.compile("<div class=\"article-author-name fn\">.*</div>");
		Matcher matcher = pattern.matcher(html);

		if (matcher.find()) {
			String authorTag = matcher.group();
			String authorTagIsolated = authorTag.substring(0, authorTag.indexOf("</div>") + "</div>".length());
			
			pureContent = pureContent + System.getProperty("line.separator") + authorTagIsolated;
		}
		
		return pureContent;
	}

	public String adjustURL(String url) {
		return url.replace("financialsense.com/", "financialsense.com/print/");
	}
}

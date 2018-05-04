package handy.rssarchive.html;

public class BlockquoteCleaner {
	public static String cleanQuote(String html){
		html = html.replaceAll("<blockquote>", System.getProperty("line.separator") + "\"");
		html = html.replaceAll("<blockquote[^>]*>", "\"");
		html = html.replaceAll("</blockquote>", "\"" + System.getProperty("line.separator"));
		
		return html;
	}
}

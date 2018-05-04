package handy.rssarchive.html;

public class ParagraphTagCleaner {
	public static String cleanParagraph(String html){
		html = html.replaceAll("<p>", "");
		html = html.replaceAll("<p[^>]*>", "");
		html = html.replaceAll("</p>", System.getProperty("line.separator"));
		
		return html;
	}
}

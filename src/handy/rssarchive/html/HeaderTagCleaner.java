package handy.rssarchive.html;

public class HeaderTagCleaner {
	public static String headerTagCleaner(String html){
		html = html.replaceAll("<h1>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("<h1[^>]*>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("</h1>", System.getProperty("line.separator"));
		
		html = html.replaceAll("<h2>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("<h2[^>]*>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("</h2>", System.getProperty("line.separator"));
		
		html = html.replaceAll("<h3>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("<h3[^>]*>", System.getProperty("line.separator") + "Header: ");
		html = html.replaceAll("</h3>", System.getProperty("line.separator"));
		
		return html;
	}
}

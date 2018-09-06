package handy.rssarchive.html;

public class BasicTagCleaner {
	public static String cleanHref(String html){
		html = html.replaceAll("</a>", "");
		html = html.replaceAll("<a[^>]*>", "");
		
		return html;
	}
	
	public static String cleanNbsp(String html){
		return html.replaceAll("&nbsp;", " ");
	}
	
	public static String cleanSpan(String html){
		html = html.replaceAll("</span>", "");
		html = html.replaceAll("<span[^>]*>", "");
		
		return html;
	}
	
	public static String cleanDiv(String html){
		html = html.replaceAll("</div>", "");
		html = html.replaceAll("<div[^>]*>", "");
		
		return html;
	}
	
	public static String cleanAside(String html){
		html = html.replaceAll("</aside>", "");
		html = html.replaceAll("<aside[^>]*>", "");
		
		return html;
	}
	
	public static String cleanStrong(String html){
		html = html.replaceAll("<strong>", "");
		html = html.replaceAll("</strong>", "");
		
		return html;
	}
	
	public static String cleanEm(String html){
		html = html.replaceAll("<em>", "");
		html = html.replaceAll("</em>", "");
		
		return html;
	}
	
	public static String cleanU(String html){
		html = html.replaceAll("<u>", "");
		html = html.replaceAll("</u>", "");
		
		return html;
	}
	
	public static String cleanI(String html){
		html = html.replaceAll("<i>", "");
		html = html.replaceAll("</i>", "");
		
		return html;
	}
	
	public static String cleanIFrames(String html){
		html = html.replaceAll("</iframe>", "");
		html = html.replaceAll("<iframe[^>]*>", "");
		
		return html;
	}
	
	public static String cleanAllBasic(String html){
		html = cleanEm(html);
		html = cleanStrong(html);
		html = cleanHref(html);
		html = cleanU(html);
		html = cleanI(html);
		html = cleanIFrames(html);
		html = cleanSpan(html);
		html = cleanDiv(html);
		html = cleanNbsp(html);
		html = cleanAside(html);
		
		return html;
	}
}

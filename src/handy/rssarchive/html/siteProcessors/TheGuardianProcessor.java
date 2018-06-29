package handy.rssarchive.html.siteProcessors;

public class TheGuardianProcessor extends SiteProcessor {

	private static TheGuardianProcessor instance = new TheGuardianProcessor();
	
	private TheGuardianProcessor(){
		
	}
	
	public static TheGuardianProcessor getInstance(){
		return instance;
	}
	public String process(String html) {
		int articleStart = html.indexOf("itemprop=\"articleBody\"");
		if(articleStart < 0){
			articleStart = html.indexOf("itemprop=\"reviewBody\"");
		}
		int articleEnd = html.indexOf("<span class=\"submeta__label\">Topics</span>");
		String partialTrim = html.substring(articleStart, articleEnd);
		int incompleteDivTagEndIdx = partialTrim.indexOf(">");
		String fullyTrim = partialTrim.substring(incompleteDivTagEndIdx + 1);
		return cleanGuardianTags(fullyTrim);
	}

	public String adjustURL(String url) {
		return url;
	}
	
	public static String cleanGuardianTags(String html){
		html = html.replaceAll("</footer>", "");
		html = html.replaceAll("<footer[^>]*>", "");
		
		html = html.replaceAll("</cite>", "");
		html = html.replaceAll("<cite[^>]*>", "");
		
		html = html.replaceAll("</svg>", "");
		html = html.replaceAll("<svg[^>]*>", "");
		
		return html;
	}

	@Override
	public boolean canProcess(String url){
		if(url.contains("/video/") ||
				url.contains("/gallery/") ||
				url.contains("/football/") || 
				url.contains("/picture/") ||
				url.contains("/slideshow/") ||
				url.contains("/ng-interactive/")){
			return false;
		}else{
			return true;
		}
	}
}

package handy.rssarchive.html.siteProcessors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CNNProcessor extends SiteProcessor {

	private static CNNProcessor instance = new CNNProcessor();
	
	private CNNProcessor(){
		
	}
	
	public static CNNProcessor getInstance(){
		return instance;
	}
	
	public String process(String html) throws Exception {
		Pattern pattern = Pattern.compile("<div class=\"pg-rail-tall__body\" itemprop=\"articleBody\">.*</div></section>");
		Matcher matcher = pattern.matcher(html);
		if(matcher.find()){
			String contentTag = matcher.group();
			String contentTagIsolated = contentTag.substring(0, contentTag.indexOf("</div></section>") + "</div></section>".length());

			String trimmedFrontDiv = contentTagIsolated.replaceAll("<div class=\"pg-rail-tall__body\" itemprop=\"articleBody\">", "");
			String divCleaned = cleanCNNDiv(trimmedFrontDiv);
			return cleanArticlesAndSection(divCleaned);
		}else{
			throw new Exception("CNN Text processor: No match for isolated article");
		}
	}

	public String adjustURL(String url) {
		return url;
	}
	
	public static String cleanCNNDiv(String html){
		html = html.replaceAll("<div class=\"zn-body__paragraph\">", System.getProperty("line.separator"));
		html = html.replaceAll("</div>", "");
		html = html.replaceAll("<div[^>]*>", "");
		
		return html;
	}
	
	public static String cleanArticlesAndSection(String html){
		html = html.replaceAll("</article>", "");
		html = html.replaceAll("<article[^>]*>", "");
		
		html = html.replaceAll("</section>", "");
		html = html.replaceAll("<section[^>]*>", "");
		
		return html;
	}

}

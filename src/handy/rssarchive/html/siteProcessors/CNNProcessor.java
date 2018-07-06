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
			Pattern pattern2 = Pattern.compile("<div class=\"el__leafmedia el__leafmedia--sourced-paragraph\">.*</div></section>");
			Matcher matcher2 = pattern2.matcher(html);
			if(matcher2.find()){
				String contentTag = matcher2.group();
				String contentTagIsolated = contentTag.substring(0, contentTag.indexOf("</div></section>") + "</div></section>".length());

				String trimmedFrontDiv = contentTagIsolated.replaceAll("<div class=\"el__leafmedia el__leafmedia--sourced-paragraph\">", "");
				String divCleaned = cleanCNNDiv(trimmedFrontDiv);
				return cleanArticlesAndSection(divCleaned);
			}else{
				Pattern pattern3 = Pattern.compile("<h2 class=\"speakable\">.*<!--/storytext-->");
				Matcher matcher3 = pattern3.matcher(html);
				if(matcher3.find()){
					String contentTag = matcher3.group();
					String contentTagIsolated = contentTag.substring(0, contentTag.indexOf("<!--/storytext-->"));

					String divCleaned = cleanCNNDiv(contentTagIsolated);
					return cleanArticlesAndSection(divCleaned);
				}else{
					throw new Exception("CNN text processor can't parse this URL");
				}
			}
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
		
		html = html.replaceAll("</h2>", "");
		html = html.replaceAll("<h2[^>]*>", "");
		
		return html;
	}
	
	@Override
	public boolean canProcess(String url){
		if(url.contains("/video/") ||
				url.contains("/gallery/") ||
				url.contains("/travel/") ||
				url.contains("/live-news/")){
			return false;
		}else{
			return true;
		}
	}

}

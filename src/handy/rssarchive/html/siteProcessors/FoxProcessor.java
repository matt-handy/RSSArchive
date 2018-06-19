package handy.rssarchive.html.siteProcessors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoxProcessor extends SiteProcessor {

	private static FoxProcessor instance = new FoxProcessor();
	
	private FoxProcessor(){
		
	}
	
	public static FoxProcessor getInstance(){
		return instance;
	}
	
	public String process(String html) throws Exception {
		Pattern pattern = Pattern.compile("<p class=\"speakable\">.*<footer class=\"article-footer\">");
		Matcher matcher = pattern.matcher(html);
		if(matcher.find()){
			String contentTag = matcher.group();
			String contentTagIsolated = contentTag.substring(0, contentTag.indexOf("<footer class=\"article-footer\">") + "<footer class=\"article-footer\">".length());

			return trimContent(contentTagIsolated);
		}else{
			Pattern pattern2 = Pattern.compile("<p>.*<footer class=\"article-footer\">");
			Matcher matcher2 = pattern2.matcher(html);
			if(matcher2.find()){
				String contentTag = matcher2.group();
				String contentTagIsolated = contentTag.substring(0, contentTag.indexOf("<footer class=\"article-footer\">") + "<footer class=\"article-footer\">".length());

				return trimContent(contentTagIsolated);
			}else{
				throw new Exception("Fox Headlines Text processor: No match for isolated article");
			}
		}
	}
	
	public String trimContent(String content){
		content = content.replace("<footer class=\"article-footer\">", "");
		content = content.replaceAll("\t+", "\t");
		return content.trim().replaceAll(" +", " ");
	}

	public String adjustURL(String url) {
		return url;
	}
}

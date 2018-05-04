package handy.rssarchive.html.siteProcessors;

public abstract class SiteProcessor {
	public abstract String process(String html); 
	
	public abstract String adjustURL(String url);
}

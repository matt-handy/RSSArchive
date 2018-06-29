package handy.rssarchive.html.siteProcessors;

public abstract class SiteProcessor {
	public abstract String process(String html) throws Exception; 
	
	public abstract String adjustURL(String url);
	
	public boolean canProcess(String url){
		return true;
	}
}

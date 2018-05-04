package handy.rssarchive.config;

import java.text.SimpleDateFormat;

import handy.rssarchive.html.siteProcessors.FinancialSenseProcessor;
import handy.rssarchive.html.siteProcessors.SiteProcessor;
import handy.rssarchive.html.siteProcessors.test.FinancialSenseTest;

public class SiteConfig {

	public final String name;
	public final String url;
	public final String folder;
	public final boolean nativeRSSContent;
	public final SimpleDateFormat format;
	public SiteProcessor processor = null;
	
	public SiteConfig(String name, String url, String folder, boolean nativeRSSContent) {
		this.name = name;
		this.url = url;
		this.folder = folder;
		this.nativeRSSContent = nativeRSSContent;
		if (this.name.equals("Financial Sense")) {
			this.format = new SimpleDateFormat("EEE, dd MMM yyyy hh");
		} else {
			this.format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
		}
		
		if(this.name.equals("Financial Sense")){
			processor = FinancialSenseProcessor.getInstance();
		}
	}
}

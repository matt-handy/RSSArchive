package handy.rssarchive.config;

import java.text.SimpleDateFormat;

import handy.rssarchive.html.siteProcessors.CNNProcessor;
import handy.rssarchive.html.siteProcessors.FinancialSenseProcessor;
import handy.rssarchive.html.siteProcessors.FoxProcessor;
import handy.rssarchive.html.siteProcessors.SiteProcessor;
import handy.rssarchive.html.siteProcessors.TheGuardianProcessor;

public class SiteConfig {

	public final String name;
	public final String url;
	public final String folder;
	public final String authorTag;
	public final String linkTag;
	public final boolean nativeRSSContent;
	public final SimpleDateFormat format;
	public SiteProcessor processor = null;
	
	public SiteConfig(String name, String url, String folder, String authorTag, String linkTag, boolean nativeRSSContent) {
		this.name = name;
		this.url = url;
		this.folder = folder;
		this.nativeRSSContent = nativeRSSContent;
		this.authorTag = authorTag;
		this.linkTag = linkTag;
		
		//The Hacky BS section, this should all be configurable instead of hard coded
		if (this.name.equals("Financial Sense")) {
			this.format = new SimpleDateFormat("EEE, dd MMM yyyy hh");
		} else {
			this.format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
		}
		
		if(this.name.equals("Financial Sense")){
			processor = FinancialSenseProcessor.getInstance();
		}else if(this.name.equals("CNN")){
			processor = CNNProcessor.getInstance();
		}else if(this.name.equals("The Guardian")){
			processor = TheGuardianProcessor.getInstance();
		}else if(this.name.equals("Fox Headlines")){
			processor = FoxProcessor.getInstance();
		}else if(this.name.equals("Fox Opinion")){
			processor = FoxProcessor.getInstance();
		}
		//Processor will default to NULL, so if there is no recognized name, it will not try to 
		//process text
		//End hacky BS section
	}
}

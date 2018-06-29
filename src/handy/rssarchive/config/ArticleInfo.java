package handy.rssarchive.config;

public class ArticleInfo {
	public final String title;
	public final String pubDateStr;
	public final String author;
	public final String url;
	public final String description;
	public final String processedText;
	
	public ArticleInfo(String title, String pubDateStr, String author, String url, String description, String processedText){
		this.title = title;
		this.pubDateStr = pubDateStr;
		this.author = author;
		this.url = url;
		this.description = description;
		this.processedText = processedText;
	}
}

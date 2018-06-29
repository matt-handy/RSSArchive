package handy.rssarchive.html;

public class ScriptCleaner {
	public static String cleanScript(String text){
		int firstIdx = text.indexOf("<script");
		while(firstIdx > 0){
			int closeIdx = text.indexOf("</script>");
			text = text.substring(0, firstIdx) + text.substring(closeIdx + "</script>".length());
			
			firstIdx = text.indexOf("<script");
		}
		
		return text;
	}
}

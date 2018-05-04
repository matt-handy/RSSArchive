package handy.rssarchive.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handy.rssarchive.file.FileUtil;

public class ImageTagCleaner {
	public static String imageTagCleaner(String html, String directory) {
		Pattern pattern = Pattern.compile("<img[^>]*>");

		Matcher matcher = pattern.matcher(html);

		int imageCount = 1;
		while (matcher.find()) {
			String imgTag = matcher.group();
			html = html.replaceFirst("<img[^>]*>", System.getProperty("line.separator") + "See Image #" + imageCount + System.getProperty("line.separator"));

			Pattern srcPattern = Pattern.compile("src=\"[^\"]*\"");
			Matcher srcMatcher = srcPattern.matcher(imgTag);
			if(srcMatcher.find()){
				String rawTargetUrl = srcMatcher.group();
				String targetUrl = srcMatcher.group().substring(5, rawTargetUrl.length() - 1);
				try{
				FileUtil.readBinaryFileFromUrl(targetUrl, directory + "\\FileNumber" + imageCount + targetUrl.substring(targetUrl.length()-4, targetUrl.length()));
				}catch (Exception e){
					e.printStackTrace();
				}
				System.out.println(targetUrl);
			}
			
			imageCount++;
		}

		return html;
	}
}

package handy.rssarchive.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import handy.rssarchive.Log;
import handy.rssarchive.Log.LogLevel;
import handy.rssarchive.file.FileUtil;

public class ImageTagCleaner {
	public static String imageTagCleaner(String html, String directory) {
		Pattern pattern = Pattern.compile("<img[^>]*>");

		Matcher matcher = pattern.matcher(html);

		int imageCount = 1;
		while (matcher.find()) {
			String imgTag = matcher.group();

			Pattern srcPattern = Pattern.compile("src=\"[^\"]*\"");
			Matcher srcMatcher = srcPattern.matcher(imgTag);
			if (srcMatcher.find()) {
				String rawTargetUrl = srcMatcher.group();
				String targetUrl = srcMatcher.group().substring(5, rawTargetUrl.length() - 1);
				try {
					FileUtil.readBinaryFileFromUrl(targetUrl, directory + FileUtil.getFileSep() + "FileNumber"
							+ imageCount + targetUrl.substring(targetUrl.length() - 4, targetUrl.length()));
					html = html.replaceFirst("<img[^>]*>", System.getProperty("line.separator") + "See Image #"
							+ imageCount + System.getProperty("line.separator"));
					imageCount++;
				} catch (Exception e) {
					Pattern srcPattern2 = Pattern.compile("data-src-large=\"[^\"]*\"");
					Matcher srcMatcher2 = srcPattern2.matcher(imgTag);
					if (srcMatcher2.find()) {
						String rawTargetUrl2 = srcMatcher2.group();
						String targetUrl2 = srcMatcher2.group().substring("data-src-large=\"".length(), rawTargetUrl2.length() - 1);
						try {
							FileUtil.readBinaryFileFromUrl(targetUrl2, directory + FileUtil.getFileSep() + "FileNumber"
									+ imageCount + targetUrl2.substring(targetUrl2.length() - 4, targetUrl2.length()));
							html = html.replaceFirst("<img[^>]*>", System.getProperty("line.separator") + "See Image #"
									+ imageCount + System.getProperty("line.separator"));
							imageCount++;
						} catch (Exception e2) {
							html = html.replaceFirst("<img[^>]*>", "");
							Log.log("Can't access image: " + targetUrl + "AND Can't access image: " + targetUrl2 + System.lineSeparator() + "at: " + imgTag, LogLevel.WARNING);
						}
					}		
				}
			}

		}

		return html;
	}
}

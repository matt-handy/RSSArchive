package handy.rssarchive.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import handy.xml.ArticleAccessHelper;

public class FileUtil {
	
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	
	public static String articleFolderNameGenerator(String title, Date date){
		title = title.replaceAll(":", "-");
		title = title.replaceAll("/", "");
		title = title.replaceAll("\"", "-");
		title = title.replaceAll(" ", "_");
		String rootName = df.format(date) + "_" + title.substring(0, 10);
		return rootName;
	}

	public static boolean writeRawHTMLRecord(String title, Date date, String url, String targetDir){
		String rootName = articleFolderNameGenerator(title, date);
		String dirName = targetDir + "\\" + rootName; 
		
		File folder = new File(dirName);
		if(!folder.exists()) { 
		    folder.mkdirs();
		    try {
				PrintWriter writer = new PrintWriter(dirName + "\\" + rootName + ".txt");
				writer.write(ArticleAccessHelper.getText(url));
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		    
		}else{
			return false;
		}
		
		return true;
	}
	
	public static String readFile(String pathname) throws IOException {

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(file);
	    String lineSeparator = System.getProperty("line.separator");

	    try {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + lineSeparator);
	        }
	    } finally {
	        scanner.close();
	    }
	    return fileContents.toString();
	}
}

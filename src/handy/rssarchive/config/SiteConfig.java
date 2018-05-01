package handy.rssarchive.config;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import handy.rssarchive.file.FileUtil;

public class SiteConfig {

	public final String name;
	public final String url;
	public final String folder;
	public final boolean nativeRSSContent;
	public final SimpleDateFormat format;

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
	}
}

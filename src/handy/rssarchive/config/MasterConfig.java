package handy.rssarchive.config;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import gov.nasa.gsfc.gmsec.api.Config;
import handy.rssarchive.file.FileUtil;

public class MasterConfig {
	
	public final List<SiteConfig> configs;
	public int refreshHours;
	
	public String gmsecHeartbeatSubject = null; 
	public String gmsecLogSubject = null;
	public Config gmsecConfig = null;
	
	public String chromeInvoke = null;
	
	public boolean enableChrome;
	
	public MasterConfig(String configFile){
		configs = new ArrayList<SiteConfig>();
		refreshHours = 1;//Default value
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(FileUtil.readFile(configFile))));
			
			NodeList refreshIntList = document.getElementsByTagName("refreshIntervalHrs");
			//Just assume one node returned
			String refreshHoursStr = null;
			for(int idx = 0; idx < refreshIntList.getLength(); idx++){
				refreshHoursStr = refreshIntList.item(idx).getTextContent();
			}
			refreshHours = Integer.parseInt(refreshHoursStr);
			
			NodeList GMSECHeartbeatSubjectList = document.getElementsByTagName("GMSECHeartbeatSubject");
			for(int idx = 0; idx < GMSECHeartbeatSubjectList.getLength(); idx++){
				gmsecHeartbeatSubject = GMSECHeartbeatSubjectList.item(idx).getTextContent();
			}
			
			NodeList GMSECLogSubjectList = document.getElementsByTagName("GMSECLogSubject");
			for(int idx = 0; idx < GMSECLogSubjectList.getLength(); idx++){
				gmsecLogSubject = GMSECLogSubjectList.item(idx).getTextContent();
			}
			
			NodeList GMSECConfigList = document.getElementsByTagName("GMSECConfigArgs");
			for(int idx = 0; idx < GMSECConfigList.getLength(); idx++){
				makeGMSECConfig(GMSECConfigList.item(idx).getTextContent());
			}
			
			chromeInvoke = document.getElementsByTagName("chrome_invoke").item(0).getTextContent();
			
			enableChrome = document.getElementsByTagName("enable_chrome").item(0).getTextContent().equals("true");
			
			NodeList siteList = document.getElementsByTagName("site");
			
			for(int idx = 0; idx < siteList.getLength(); idx++){
				Node articleItem = siteList.item(idx);
				String name = null;
				String directory = null;
				String url = null;
				String authorTag = null;
				String linkTag = null;
				boolean nativeFormat = false;
				
				for(int jdx = 0; jdx < articleItem.getChildNodes().getLength(); jdx++){
					Node metadata = articleItem.getChildNodes().item(jdx);
					if(metadata.getNodeName().equalsIgnoreCase("name")){
						name = metadata.getTextContent();
					}
					if(metadata.getNodeName().equalsIgnoreCase("targetDir")){
						directory = metadata.getTextContent();
					}
					if(metadata.getNodeName().equalsIgnoreCase("url")){
						url = metadata.getTextContent();
					}
					if(metadata.getNodeName().equalsIgnoreCase("authorTag")){
						authorTag = metadata.getTextContent();
					}
					if(metadata.getNodeName().equalsIgnoreCase("linkTag")){
						linkTag = metadata.getTextContent();
					}
					if(metadata.getNodeName().equalsIgnoreCase("nativeRSSContent")){
						String nativeFormatStr = metadata.getTextContent();
						if(nativeFormatStr.equalsIgnoreCase("true")){
							nativeFormat = true;
						}
					}						
				}
				
				if(name != null &&
						directory != null &&
						url != null){
					SiteConfig newConfig = new SiteConfig(name, url, directory, authorTag, linkTag, nativeFormat);
					configs.add(newConfig);
				}else{
					System.out.println("Name: " + name);
					System.out.println("Directory: " + directory);
					System.out.println("URL: " + url);
					System.out.println("Error ingesting XML");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void makeGMSECConfig(String str) throws Exception{
		String[] args = str.split(" ");
		gmsecConfig = new Config();
		for(String arg : args){
			String[] argElems = arg.split("=");
			if(argElems.length != 2){
				throw new Exception("Bad GMSEC config");
			}
			System.out.println(argElems[0] + " " + argElems[1]);
			gmsecConfig.addValue(argElems[0], argElems[1]);
		}
	}
}

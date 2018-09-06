package handy.rssarchive;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;

import gov.nasa.gsfc.gmsec.api.field.Field;
import gov.nasa.gsfc.gmsec.api.field.StringField;
import gov.nasa.gsfc.gmsec.api.mist.ConnectionManager;
import handy.rssarchive.Log.LogLevel;
import handy.rssarchive.config.MasterConfig;
import handy.rssarchive.config.SiteConfig;
import handy.rssarchive.file.FileUtil;
import handy.xml.ArticleAccessHelper;

public class Runner {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Please specify target directory");
			return;
		}

		String rootDirStr = args[0];
		File rootDirectory = new File(rootDirStr);
		if (!rootDirectory.exists() || !rootDirectory.isDirectory()) {
			System.out.println("Specified target directory is not valid:");
			System.out.println(rootDirectory);
			return;
		}

		MasterConfig masterConfig = new MasterConfig("config" + FileUtil.getFileSep() + "config.xml");

		try {
			if (masterConfig.gmsecConfig != null & masterConfig.gmsecHeartbeatSubject != null &&
					masterConfig.gmsecLogSubject != null) {
				ConnectionManager connMan = new ConnectionManager(masterConfig.gmsecConfig);
				connMan.initialize();
				List<Field> commonFields = new ArrayList<Field>();
				commonFields.add(new StringField("COMPONENT", "RSSArchive"));
				connMan.setStandardFields(commonFields);
				connMan.startHeartbeatService(masterConfig.gmsecHeartbeatSubject, new ArrayList<Field>());
				
				Log.setConnectionManager(connMan, masterConfig.gmsecLogSubject);
			} else {
				System.out.println("Continuing with no GMSEC connectivity");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to GMSEC bus");
		}

		while (true) {
			for (SiteConfig config : masterConfig.configs) {
				Document rssFeed = ArticleAccessHelper.getXMLDocumentFromURL(config.url);
				ArticleAccessHelper.rssRecorder(rssFeed, rootDirStr + FileUtil.getFileSep() + config.folder, config);
			}

			Log.log("Sleeping for hours: " + masterConfig.refreshHours, LogLevel.NOMINAL);
			System.out.println((new Date()).toString());
			Thread.sleep(masterConfig.refreshHours * 3600 * 1000);
		}

	}
}

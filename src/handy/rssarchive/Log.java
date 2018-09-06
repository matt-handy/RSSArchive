package handy.rssarchive;

import java.util.ArrayList;

import gov.nasa.gsfc.gmsec.api.GMSEC_Exception;
import gov.nasa.gsfc.gmsec.api.field.Field;
import gov.nasa.gsfc.gmsec.api.field.I32Field;
import gov.nasa.gsfc.gmsec.api.mist.ConnectionManager;

public class Log {

	private static ConnectionManager connMan;
	public enum LogLevel {NOMINAL(1), WARNING(3), CRITICAL(4);
		public final int levelMap;
		private LogLevel(int levelMap){
			this.levelMap = levelMap;
		}
	}
	
	public static void log(String entry, LogLevel level){
		if(connMan != null){
			try{
				connMan.publishLog(entry, new I32Field("LOG-LEVEL", level.levelMap));
			}catch(GMSEC_Exception e){
				System.out.println("I can't log!: " + entry);
			}
		}
		System.out.println(level + ": " + entry);
		
	}
	
	static void setConnectionManager(ConnectionManager connMan, String subject) throws GMSEC_Exception{
		Log.connMan = connMan;
		connMan.setLoggingDefaults(subject, new ArrayList<Field>());
	}
}

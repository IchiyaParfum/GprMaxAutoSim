package autoSim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
	public final static String appDir = System.getProperty("user.dir");
	public final static String tempDir = "resources\\templates";
	public final static String scriptDir = "resources\\scripts";
	public static String simDir;
	public static String runFile;
	public static String python;
	public static String matlab;
	
	static{
		FileInputStream is = null;
		
			try {
				Properties prop = new Properties();
				String propFileName = "resources\\config.properties"; 
				is = new FileInputStream(new File(propFileName));				
				prop.load(is);
	 
				//Get properties
				simDir = prop.getProperty("simDir");
				runFile = prop.getProperty("runFile");
				python = prop.getProperty("python");
				matlab = prop.getProperty("matlab");
	 
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	}
}

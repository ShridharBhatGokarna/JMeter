package dataaccess;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import base.Launcher;

public class PropertyReader {
	static Properties properties;
	
	public PropertyReader(){
//		loadAllProperties();
	}

	public static void loadAllProperties() {
		if (properties != null) {	//config.properties is only read once. This if statement ensures that config file is read ONLY ONCE.
			return;
		}
		properties = new Properties();
		try{
			System.out.println("Loading config.properties data.");
			String ParentProjectPath = Launcher.filePathPrefix.substring(0,Launcher.filePathPrefix.lastIndexOf(File.separator));
			properties.load(new FileInputStream(ParentProjectPath+File.separator+"config.properties"));
		}catch(Exception e){
			throw new RuntimeException("Could not read Properties File");
		}		
	
	}
	
	public static String readItem(String propertyName){
		return properties.getProperty(propertyName);
	}
}
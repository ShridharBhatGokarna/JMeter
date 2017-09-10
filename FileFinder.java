package utils;

import java.io.File;
import java.io.IOException;



import org.apache.commons.io.FileUtils;

import dataaccess.PropertyReader;
import base.Launcher;


public class FileFinder {

	static String folderLocation = System.getProperty("user.dir");	

	
	/********************************************************************************************
	 * @Function_Name :  deleteLogFile
	 * @Description   :  Check for IOIAutomation log file 
	 * 					 If the Files Exist then delete the Files
	 *******************************************************************************************/
	
	
	public static boolean deleteoutputDirForStress(){					
		File screenshotDir = new File(Launcher.filePathPrefix +PropertyReader.readItem("outputpathDir"));
		if(screenshotDir.exists() && screenshotDir.isDirectory()){
			try {				
				
				FileUtils.cleanDirectory(screenshotDir); 
				if(screenshotDir.list() != null){
					System.out.println("Files inside Screenshot Directory deleted !!");
				}else{
					System.err.println("Files inside Screenshot Directory could not be deleted");
				}				
			} catch (IOException e) {			
				System.out.println("Cleaning the files in directory");
				//cleaning directory by deleting files in it
				File[] files = screenshotDir.listFiles();	
				for (int i = 0; i < files.length; i++) {
		                files[i].delete();
		        }
			}
		}else{
			System.err.println("Screenshot Directory does not Exist.");
		}	
		return true;
	}



	
	
	
	
}

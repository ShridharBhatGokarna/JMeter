package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.FileFinder;
import dataaccess.ExcelReader;
import dataaccess.PropertyReader;

public class Launcher {

	public static String filePathPrefix = System.getProperty("user.dir");
	public static String jmeterinstallbinloc =null;
	public static String jtlLocation =null;
	public static String jmxLocation =null;
	public static String csvLocation =null;
	public static String xlsxLocation =null;
    public static String jmxFileName =null;
    public static String cmdrunnerjarloc=null;
	public static String pngLocation=null;
    public static String Threads = null;
	public static String RampUpPeriodinSeconds = null;
	public static String TestCaseDurationinSeconds= null;
	public static String sheetName =null;
	public static List<String> HeadingsAndData = new ArrayList<String>();
	public static HashMap<String,String> CSVDetailsHashMap= new HashMap<String,String>();



	public static void main(String args[]) throws Exception{

		

		PropertyReader.loadAllProperties();
		FileFinder.deleteoutputDirForStress();
		jmeterinstallbinloc= filePathPrefix+PropertyReader.readItem("jmeterinstallbinloc");
		cmdrunnerjarloc=filePathPrefix+PropertyReader.readItem("cmdrunnerjarloc");
		Threads=PropertyReader.readItem("NoOfThreads");
		RampUpPeriodinSeconds=PropertyReader.readItem("RampUpPeriod");
		TestCaseDurationinSeconds=PropertyReader.readItem("Duration");


		for(int i=1;i<=Integer.parseInt(PropertyReader.readItem("NoOfTestCases"));i++){
			jmxLocation=filePathPrefix+PropertyReader.readItem("jmxfilepath"+i);

			jmxFileName=jmxLocation.substring(jmxLocation.lastIndexOf(File.separator)+1,jmxLocation.lastIndexOf("."));

			jtlLocation=filePathPrefix+PropertyReader.readItem("outputpathDir")+File.separator+jmxFileName+".jtl";
			csvLocation=filePathPrefix+PropertyReader.readItem("outputpathDir")+File.separator+jmxFileName+".csv";
			xlsxLocation=filePathPrefix+PropertyReader.readItem("outputpathDir")+File.separator+jmxFileName+".xlsx";
			pngLocation=filePathPrefix+PropertyReader.readItem("outputpathDir")+File.separator+jmxFileName+".png";


			ExecuteCommand(jmeterinstallbinloc+File.separator+"jmeter"+" -n -t \""+jmxLocation+"\" -Jthreads="+Threads+" -Jrampup="+RampUpPeriodinSeconds +" -Jduration="+TestCaseDurationinSeconds +" -l \""+jtlLocation+"\"");
			ExecuteCommand("java -jar "+cmdrunnerjarloc+File.separator+"cmdrunner.jar"+" --tool Reporter --generate-csv \""+csvLocation+"\" --input-jtl \""+jtlLocation+"\" --plugin-type AggregateReport");
			ExecuteCommand("java -jar "+cmdrunnerjarloc+File.separator+"cmdrunner.jar"+" --tool Reporter --generate-png \""+pngLocation+"\" --input-jtl \""+jtlLocation+"\" --plugin-type ResponseTimesOverTime --width 1200 --height 1200");

		}

		sheetName = csvLocation.substring(csvLocation.lastIndexOf("\\")+1,csvLocation.lastIndexOf("."));
		ExcelReader.csvToXLSX(csvLocation,sheetName);
		ExcelReader CSVExcelFileReader = new ExcelReader(xlsxLocation);
		HeadingsAndData=CSVExcelFileReader.ReadHeadingAndSingleRow(sheetName, "TOTAL");
		csvLocation.substring(csvLocation.lastIndexOf("\\"));
		csvLocation.substring(csvLocation.lastIndexOf("."));
		CreateHashMapDataforTestCase(HeadingsAndData);

	}

	public static HashMap<String,String> CreateHashMapDataforTestCase(List<String> HeadingsAndData){


		String[] array = HeadingsAndData.toArray(new String[0]);
		String[] headings = array[0].split("%%");
		String[] data = array[1].split("%%");

		for(int i=0;i<data.length;i++){
			try{
				CSVDetailsHashMap.put(headings[i+1], data[i]);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return CSVDetailsHashMap;

	}

	public static void ExecuteCommand(String CMD) throws Exception{


		System.out.println("The command required to execute:"+CMD);
		Process process =null;
		try {
			String[] command = {"cmd.exe", "/C", "Start", CMD};
			process = Runtime.getRuntime().exec("cmd.exe /c"+" "+CMD);         
		} catch (IOException ex) {
		}

		// Get input streams
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		// Read command standard output
		String s1,s2;

		// Read command errors

		/*process.waitFor();*/
		/*
		System.out.println("Standard error: ");
		while ((s2 = stdError.readLine()) != null) {
			System.out.println(s2);
		}*/


		System.out.println("Standard output: ");
		while ((s1 = stdInput.readLine()) != null) {
			System.out.println(s1);
		}


		


		int returnValue=process.exitValue();
		System.out.println("Return value for jmeter command is:"+ returnValue);
		stdError.close();
		stdInput.close();
		process.destroy();


	} 


}

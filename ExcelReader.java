package dataaccess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private String inputFile;
	private String sheetName;
	private Boolean SkipHeading;
	

	public ExcelReader(String inputFile) throws IOException {
		this.inputFile = inputFile;
	}

	public ArrayList<String>readAllCategoriesData() throws IOException{

		ArrayList<String> table = new ArrayList<String>();
		StringBuilder rowList;

		FileInputStream inp = new FileInputStream(inputFile);
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(inp);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}  
		Sheet sheet =workbook.getSheet(sheetName);
		Row row=null;
		Cell cell=null;

		Iterator<?> rows = sheet.rowIterator();
		
		if(this.SkipHeading==true)
		rows.next();  	//We don't want heading, so we move to next row 


		while(rows.hasNext()){

			row = (Row) rows.next();
			Iterator<?> cells = row.cellIterator();
			rowList=new StringBuilder();

			while (cells.hasNext())	{

				cell = (Cell) cells.next();

				if(cell.toString().length()!=0){
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						rowList.append(cell.getStringCellValue()+"%%");
					}
					else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
						rowList.append((int)cell.getNumericCellValue()+"%%");
					}
					else if(cell.getCellType()==Cell.CELL_TYPE_BOOLEAN){
						rowList.append(cell.getBooleanCellValue()+"%%");
					}				
					else {  
						//U Can Handle Formula, Errors 					
					}
				}

			}	
			if(rowList.toString().length()!=0)
			{
				table.add(rowList.toString());
			}
		}
		return table;
	}	
	
	public static void csvToXLSX(String csvFilePath,String SheetName) {
	    try {
	        String csvFileAddress = csvFilePath; //csv file address
	        String xlsxFileAddress = csvFilePath.substring(0,csvFilePath.lastIndexOf("."))+".xlsx"; //xlsx file address
	        XSSFWorkbook workBook = new XSSFWorkbook();
	        XSSFSheet sheet = workBook.createSheet(SheetName);
	        String currentLine=null;
	        int RowNum=0;
	        BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
	        while ((currentLine = br.readLine()) != null) {
	            String str[] = currentLine.split(",");
	            RowNum++;
	            XSSFRow currentRow=sheet.createRow(RowNum);
	            for(int i=0;i<str.length;i++){
	                currentRow.createCell(i).setCellValue(str[i]);
	            }
	        }

	        FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
	        workBook.write(fileOutputStream);
	        fileOutputStream.close();
	        System.out.println("Done");
	    } catch (Exception ex) {
	        System.out.println(ex.getMessage()+"Exception in try");
	    }
	}

	public List<String> getData(){
		return null;
	}

	public ArrayList<String> readSheet(String sheetName,Boolean SkipHeading) throws IOException  {
		this.sheetName = sheetName;
		this.SkipHeading=SkipHeading;
		ArrayList<String> table = null;

		try{
			table = readAllCategoriesData();
		}catch(IOException e){
			System.out.println("Exception in readSheet : " +e.getMessage());
		}			
		return table;			
	}	

	public ArrayList<String> ReadHeadingAndSingleRow(String sheetName,String ID) throws IOException {
		this.sheetName = sheetName;
		ArrayList<String> table = new ArrayList<String>();
		StringBuilder rowList;
		FileInputStream inp = new FileInputStream(inputFile);
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(inp);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {			
			e.printStackTrace();
		}  
		Sheet sheet =workbook.getSheet(sheetName);
		Row row=null;
		Cell cell=null;

		Iterator<?> rows = sheet.rowIterator();

		//Reading only header details below
		Row firstrow = (Row) rows.next();
		Iterator<?> firstrowcells = firstrow.cellIterator();
		StringBuilder firstrowList = new StringBuilder();

		while(firstrowcells.hasNext()){
			cell = (Cell) firstrowcells.next();
			if(!cell.toString().equalsIgnoreCase("ID")){
				firstrowList.append(cell.getStringCellValue()+"%%");
			}
			
		}	
			table.add(firstrowList.toString());
        
			//Reading the desired row matching ID
			while(rows.hasNext()){

				row = (Row) rows.next();
				Iterator<?> cells = row.cellIterator();
				rowList=new StringBuilder();

				while (cells.hasNext())	{

					cell = (Cell) cells.next();


					if(!cell.toString().equalsIgnoreCase(ID)){
						row = (Row) rows.next();
						cells = row.cellIterator();
						rowList=new StringBuilder();
						continue;
					}

					else 
					{

						while (cells.hasNext()){

							cell = (Cell) cells.next();

							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								rowList.append(cell.getStringCellValue()+"%%");
							}
							else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
								rowList.append((int)cell.getNumericCellValue()+"%%");
							}
							else if(cell.getCellType()==Cell.CELL_TYPE_BOOLEAN){
								rowList.append(cell.getBooleanCellValue()+"%%");
							}				
							else {  
								//U Can Handle Formula, Errors 					
							}

						}

						table.add(rowList.toString());

						return table;
					}



				}	

			}
			
			System.err.println("Data matching ID not found");
			
			return null;

		}
	
	
}
		

	

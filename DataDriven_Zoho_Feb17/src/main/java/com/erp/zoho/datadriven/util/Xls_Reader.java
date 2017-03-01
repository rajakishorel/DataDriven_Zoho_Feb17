package com.erp.zoho.datadriven.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Xls_Reader {

	public String path;
	public FileInputStream fis=null;
	public FileOutputStream fos=null;
	public XSSFWorkbook workbook=null;
	public XSSFSheet sheet=null;
	public XSSFRow row=null;
	public XSSFCell cell=null;

	public Xls_Reader(String path){
		this.path=path;
		try{
			fis = new FileInputStream(path);
			workbook = new XSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public int getRowCount(String sheetName){
		int index = workbook.getSheetIndex(sheetName);

		if(index==-1)
			return 0;
		else{
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum()+1;
			return number;
		}
	}
	
	public int getColumnCount(String sheetName){
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(0);
		
		if(row==null)
			return -1;
		
		return row.getLastCellNum();
	}
	
	public boolean isSheetExist(String sheetName){
		int index = workbook.getSheetIndex(sheetName);
		if(index==-1){
			index = workbook.getSheetIndex(sheetName.toUpperCase());
			if(index==-1)
				return false;
			else
				return true;
		}
		return true;
	}
	
	public String getCellData(String sheetName, String colName, int rowNum){
		try{
			if(rowNum<=0)
				return "";
			
			int index = workbook.getSheetIndex(sheetName);
			int col_Num=-1;
			
			if(index==-1)
				return "";
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(0);
			
			for(int i=0; i<row.getLastCellNum(); i++){
				if(row.getCell(i).getStringCellValue().trim().equals(colName.trim())){
					col_Num=i;
				}
			}
			
			if(col_Num==-1)
				return "";
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			
			if(row==null)
				return "";
			
			cell = row.getCell(col_Num);
			
			if(cell==null)
				return "";
			
			if(cell.getCellType()==cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType()==cell.CELL_TYPE_NUMERIC || cell.getCellType()==cell.CELL_TYPE_FORMULA){
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			}else if(cell.getCellType()==cell.CELL_TYPE_BLANK)
				return "";
			else{
				return String.valueOf(cell.getBooleanCellValue());
			}
		}catch(Exception e){
			e.printStackTrace();
			return "Row "+rowNum+" or Column"+colName+" does not exist in xls";
		}
	}
	
	public String getCellData(String sheetName, int colNum, int rowNum){
		try{
			if(rowNum<=0)
				return "";
			
			int index = workbook.getSheetIndex(sheetName);
			
			if(index==-1)
				return "";
			
			sheet = workbook.getSheetAt(index);
			row = sheet.getRow(rowNum-1);
			
			if(row==null)
				return "";
			
			cell = row.getCell(colNum);
			
			if(cell==null)
				return "";
			
			if(cell.getCellType()==cell.CELL_TYPE_STRING)
				return cell.getStringCellValue();
			else if(cell.getCellType()==cell.CELL_TYPE_NUMERIC || cell.getCellType()==cell.CELL_TYPE_FORMULA){
				String cellText = String.valueOf(cell.getNumericCellValue());
				return cellText;
			}else if(cell.getCellType()==cell.CELL_TYPE_BLANK)
				return "";
			else{
				return String.valueOf(cell.getBooleanCellValue());
			}
		}catch(Exception e){
			e.printStackTrace();
			return "Row "+rowNum+" or Column"+colNum+" does not exist in xls";
		}
	}
}



package com.erp.zoho.datadriven.util;

import java.util.Hashtable;

public class DataUtil {
	
	public static Object[][] getTestData(Xls_Reader xls, String testCaseName){
		String sheetName="Data";
		
		int testStartRowNum=1;
		while(!xls.getCellData(sheetName, 0, testStartRowNum).equals(testCaseName)){
			testStartRowNum++;
		}
		System.out.println("Test starts from row no: "+testStartRowNum);
		
		int colStartRowNum=testStartRowNum+1;
		int dataStartRowNum=testStartRowNum+2;
		
		int rows=0;
		while(!xls.getCellData(sheetName, 0, dataStartRowNum+rows).equals("")){
			rows++;
		}
		System.out.println("Total rows of Test data: "+rows);
		
		int cols=0;
		while(!xls.getCellData(sheetName, cols, colStartRowNum).equals("")){
			cols++;
		}
		System.out.println("Total columns are: "+cols);
		
		Object[][] data = new Object[rows][1];
		int dataRow=0;
		Hashtable<String,String> table = null;
		
		for(int rNum=dataStartRowNum; rNum<dataStartRowNum+rows; rNum++){
			table = new Hashtable<String,String>();
			for(int cNum=0; cNum<cols; cNum++){
				String key = xls.getCellData(sheetName, cNum, colStartRowNum);
				String value = xls.getCellData(sheetName, cNum, rNum);
				table.put(key, value);
			}
			data[dataRow][0]=table;
			dataRow++;
		}
		return data;
	}
	
	public static boolean isRunnable(String testName, Xls_Reader xls){
		String sheetName="TestCases";
		int rows = xls.getRowCount(sheetName);
		for(int r=2; r<=rows; r++){
			String tName = xls.getCellData(sheetName, "TCID", r);
			if(tName.equals(testName)){
				String runmode = xls.getCellData(sheetName, "Runmode", r);
				if(runmode.equals("Y"))
					return true;
				else
					return false;
			}
		}
		return false;
	}

}

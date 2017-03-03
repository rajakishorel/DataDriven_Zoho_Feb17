package com.erp.zoho.datadriven.testcases;

import java.util.Hashtable;

import org.testng.asserts.SoftAssert;

import com.erp.zoho.datadriven.base.BaseTest;
import com.erp.zoho.datadriven.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class LoginTest extends BaseTest {
	
	public String testCaseName = "LoginTest";
	SoftAssert softAssert = null;
	Xls_Reader xls;
	
	public void doLoginTest(Hashtable<String,String> data){
		test = rep.startTest("LoginTest");
		test.log(LogStatus.INFO, "Starting Login Test");
		test.log(LogStatus.INFO, data.toString());
		
		
	}

}
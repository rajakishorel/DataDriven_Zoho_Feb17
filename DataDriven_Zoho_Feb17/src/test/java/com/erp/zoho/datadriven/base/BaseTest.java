package com.erp.zoho.datadriven.base;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.erp.zoho.datadriven.util.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {

	public WebDriver driver;
	public FileInputStream fis=null;
	public Properties prop=null;
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	public boolean gridRun=false;

	public void init(){
		if(prop==null){
			prop = new Properties();
			envProp = new Properties();
			try{
				fis = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//ProjectConfig.properties");
				prop.load(fis);

				/*String env = prop.getProperty("env");
				fis = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//ProjectConfig.properties");
				envProp.load(fis);*/
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void openBrowser(String bType){
		test.log(LogStatus.INFO, "Launching "+bType+ "Browser");
		if(!gridRun){

			if(bType.equals("Mozilla"))
				driver = new FirefoxDriver();
			else if(bType.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver", prop.getProperty("ChromeDriver_EXE"));
				driver = new ChromeDriver();
			}else if(bType.equals("IE")){
				System.setProperty("webdriver.ie.driver", prop.getProperty("IEDriver_EXE"));
				driver = new InternetExplorerDriver();
			}
		}else {
			DesiredCapabilities cap = null;
			if(bType.equals("Mozilla")){
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}else if(bType.equals("Chrome")){
				cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			try{
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser "+bType+ "launched successfully");
	}

	public void navigate(String urlKey){
		test.log(LogStatus.INFO, "Navigating to "+urlKey);
		driver.get(prop.getProperty(urlKey));
		test.log(LogStatus.INFO, "Successfully navigated to "+urlKey);
	}

	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on "+locatorKey);
		getElement(locatorKey).click();
		test.log(LogStatus.INFO, "Clicked on "+locatorKey);
	}
	
	public void type(String locatorKey, String data){
		test.log(LogStatus.INFO, "Typing into "+locatorKey+ "with data "+data);
		getElement(locatorKey).sendKeys(data);
		test.log(LogStatus.INFO, "Typed into "+locatorKey+ "with data "+data);
	}

	public WebElement getElement(String locatorKey){
		WebElement e = null;
		try{
			if(locatorKey.equals("_id"))
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));
			else if(locatorKey.equals("_name"))
				e = driver.findElement(By.name(prop.getProperty(locatorKey)));
			else if(locatorKey.equals("_xpath"))
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			else{
				reportFailure("Locator not correct "+locatorKey);
				Assert.fail("Locator not correct "+locatorKey);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			reportFailure(ex.getMessage());
			Assert.fail("Test Failed -> "+ex.getMessage());
		}
		
		return e;
	}
	
//		****************** Reporting ************************
	
	public void reportFailure(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenshot();
		Assert.fail(msg);
	}
	
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	
	public void takeScreenshot(){
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_")+".jpg";
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		try{
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		}catch(Exception e){
			e.printStackTrace();
		}
		test.log(LogStatus.INFO, "Screenshot -> "+test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
	}
	
//		****************** Validations ************************
	
	public boolean verifyTitle(String expectedTitleKey){
		String actualTitle = driver.getTitle();
		String expectedTitle = prop.getProperty(expectedTitleKey);
		
		if(actualTitle.equals(expectedTitle))
			return true;
		else
			return false;
	}
	
	public boolean verifyText(String locatorKey, String expectedTextKey){
		String actualText = getElement(locatorKey).getText().trim();
		String expectedText = prop.getProperty(expectedTextKey);
		
		if(actualText.equals(expectedText))
			return true;
		else
			return false;
	}
	
	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementsList = null;
		if(locatorKey.endsWith("_id"))
			elementsList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementsList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			elementsList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct "+locatorKey);
			Assert.fail("Locator not correct "+locatorKey);
		}
		
		if(elementsList.size()==0)
			return false;
		else
			return true;
		
	}
	
//	****************** Reusable Functions ************************

	public void wait(int timeInSeconds){
		try{
			Thread.sleep(timeInSeconds*1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void waitForPageToLoad(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
			
		}
	}
	
	public void clickAndWait(String locatorKey, String locatorForConfirmation){
		test.log(LogStatus.INFO, "Clicking and Waiting on "+locatorKey);
		int count=5;
		for(int i=0; i<count; i++){
			getElement(locatorKey).click();
			wait(2);
			if(isElementPresent(locatorForConfirmation))
				break;
		}
	}
	
	public void acceptAlert(){
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.alertIsPresent());
		test.log(LogStatus.INFO, "Accepting Alert");
		Alert alert = driver.switchTo().alert();
		String alertText = driver.switchTo().alert().getText();
		System.out.println(alertText);
		alert.accept();
		driver.switchTo().defaultContent();
		
	}
	
	public String getText(String locatorKey){
		test.log(LogStatus.INFO, "Getting text from "+locatorKey);
		return getElement(locatorKey).getText();
	}
	
	
//	****************** App Functions ************************
	
	/*public boolean doLogin(String username, String password){
		test.log(LogStatus.INFO, "Trying to login with "+username+ "and "+password);
		
	}*/
}

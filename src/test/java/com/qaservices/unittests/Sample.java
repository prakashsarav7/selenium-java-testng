package com.qaservices.unittests;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qaservices.utils.DataProviderClass;
import com.qaservices.utils.HTMLReport;
import com.qaservices.utils.PropertyUtil;
import com.qaservices.utils.ReportLog;
import com.qaservices.utils.RetryAnalyzer;
import com.qaservices.utils.WebDriverFactory;

@Listeners(HTMLReport.class)
public class Sample {

	@Test(groups = { "Smoke", "Regression", "Integration" }, dataProviderClass = DataProviderClass.class, dataProvider = "dataProviderMethod")
	public void test(String brString) throws Exception {
		ReportLog.testCaseInfo("Launching webdriver");
		ReportLog.debug("debug");
		 ReportLog.fail("fail");
		WebDriver driver = WebDriverFactory.getDriver(brString);
		driver.get("http://www.google.com");
		ReportLog.info("Navigated to google home page", driver, true);
		ReportLog.warning("Navigated to google home page", driver, true);
		ReportLog.exception(new SkipException("Skipped"), driver);
		driver.quit();
		ReportLog.endTest();
	}

	int i = 0;

	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void testPropertyReader() {
		PropertyUtil configProperty = PropertyUtil.getEnvironmentProperties();
		/*
		 * HashMap<String, String> values = Maps.newHashMap();
		 * values.put("HubHost", "localhost"); values.put("HubPort", "4444");
		 * propertyUtil.setProperties(values);
		 */
		if (i++ < 3)
			ReportLog.fail("fail");
		;
		ReportLog.info(configProperty.getProperty("HubHost"));
	}

	@Test
	public void testRemoteWebDriver() {
		ReportLog.testCaseInfo("Launching webdriver");
		WebDriver driver = WebDriverFactory.getDriver("phantomjs_windows");
		driver.get("http://www.google.com");
		ReportLog.info("Navigated to google home page", driver, true);
		driver.quit();
		ReportLog.endTest();
	}

	public static void main(String[] args) {
		ExtentReports report = new ExtentReports();
		report.attachReporter(new ExtentHtmlReporter("ExtentReport.html"));
		ExtentTest test = report.createTest("Class1");
		ExtentTest test1 = test.createNode("Test1").assignCategory("smoke", "regression", "integration").assignAuthor("author");
		test.createNode("Test2").fail("fail");
		test.createNode("Test2").fail("fail");
		test.createNode("Test2").fail("fail");
		test.createNode("Test2").fail("pass");
		test.createNode("Test3").fail("fail");
		test1.fail(new Exception("Intentional"));
		report.flush();
	}

}

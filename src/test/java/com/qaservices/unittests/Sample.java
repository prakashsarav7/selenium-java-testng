package com.qaservices.unittests;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qaservices.utils.Enumerators.Browser;
import com.qaservices.utils.HTMLReport;
import com.qaservices.utils.ReportLog;
import com.qaservices.utils.WebDriverFactory;

@Listeners(HTMLReport.class)
public class Sample {
	
	@Test(groups = {"Smoke", "Regression", "Integration"})
	public void test() throws Exception {
		ReportLog.testCaseInfo("Launching webdriver");
		WebDriver driver = WebDriverFactory.get(Browser.FIREFOX);
		driver.get("http://www.google.com");
		ReportLog.info("Navigated to google home page", driver, true);
		ReportLog.warning("Navigated to google home page", driver, true);
		ReportLog.exception(new SkipException("Skipped"), driver);
		driver.quit();
		ReportLog.endTest();
	}

}

package com.qaservices.unittests;

import org.openqa.selenium.WebDriver;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.qaservices.utils.DataProviderClass;
import com.qaservices.utils.HTMLReport;
import com.qaservices.utils.PropertyUtil;
import com.qaservices.utils.ReportLog;
import com.qaservices.utils.WebDriverFactory;

@Listeners(HTMLReport.class)
public class Sample {

	@Test(groups = { "Smoke", "Regression", "Integration" }, dataProviderClass = DataProviderClass.class, dataProvider = "dataProviderMethod")
	public void test(String brString) throws Exception {
		ReportLog.testCaseInfo("Launching webdriver");
		WebDriver driver = WebDriverFactory.getDriver(brString);
		driver.get("http://www.google.com");
		ReportLog.info("Navigated to google home page", driver, true);
		ReportLog.warning("Navigated to google home page", driver, true);
		ReportLog.exception(new SkipException("Skipped"), driver);
		driver.quit();
		ReportLog.endTest();
	}

	@Test
	public void testPropertyReader() {
		PropertyUtil configProperty = PropertyUtil.getEnvironmentProperties();
		/*
		 * HashMap<String, String> values = Maps.newHashMap();
		 * values.put("HubHost", "localhost"); values.put("HubPort", "4444");
		 * propertyUtil.setProperties(values);
		 */
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

}

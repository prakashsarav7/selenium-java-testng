package com.qaservices.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.qaservices.utils.Enumerators.Browser;

public class WebDriverFactory {

	public static WebDriver get(Browser browser) {
		WebDriver driver = null;
		String driverPath = System.getProperty("user.dir") + "\\src\\main\\resources\\drivers\\";
		switch (browser) {
		case CHROME:
			System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver_v25.exe");
			driver = new ChromeDriver();
			break;
		case FIREFOX:
			System.setProperty("webdriver.gecko.driver", driverPath + "geckodriver_0_11_1.exe");
			driver = new FirefoxDriver();
			break;
		case IE:
			System.setProperty("webdriver.ie.driver", driverPath + "IEDriverServer_x64_2_53_1.exe");
			driver = new InternetExplorerDriver();
			break;
		case PHANTOMJS:
			System.setProperty("phantomjs.binary.path", driverPath + "phantomjs_v211.exe");
			driver = new PhantomJSDriver();
			break;
		case HTMLUNIT:
			driver = new HtmlUnitDriver(true);
			break;
		}
		if (driver == null)
			ReportLog.fail("Failed to launch " + browser + " browser");
		else
			ReportLog.debug("Launched " + browser + " browser");
		ReportLog.addAttribute(browser.toString());
		driver.manage().window().maximize();
		return driver;
	}

}

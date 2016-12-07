package com.qaservices.utils;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Reporter;

import io.restassured.http.Method;
import io.restassured.response.Response;

public class WebDriverFactory {

	private static PropertyUtil configProperty = PropertyUtil.getEnvironmentProperties();

	private static URL getHubURL() {

		String hubHost = configProperty.getProperty("HubHost");
		String hubPort = configProperty.getProperty("HubPort");

		try {
			URL hubURL = new URL("http://" + hubHost + ":" + hubPort + "/wd/hub");
			return hubURL;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getTestRunMachineInfo(WebDriver driver) {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		String platformName = ((RemoteWebDriver) driver).getCapabilities().getPlatform().toString();
		String hub = Reporter.getCurrentTestResult().getHost();
		try {
			hub = hub != null ? hub : Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hub = "localhost";
		}
		String hubURL = getHubURL().toString();
		String sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
		String postUrl = hubURL.split("/wd/hub")[0] + "/grid/api/testsession?session=" + sessionID;
		Response response = RestUtil.sendRequest(Method.POST, postUrl);
		String nodeIp = JsonUtil.getKey("proxyId", response.asString());
		String testMachine = "(Browser: " + browserName + ", Platform: " + platformName + ", Hub: " + hubURL + ", Node: " + nodeIp + ")";
		return testMachine;
	}

	private static WebDriver getNativeDriver(String browser_platform) {
		WebDriver driver = null;
		String browser = browser_platform.split("_")[0].toLowerCase();
		DesiredCapabilities capability = DesiredCapabilitiesUtils.getDesiredCapability(browser_platform);
		
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", Constants.DRIVERS_FILEPATH + "chromedriver_v25.exe");
			driver = new ChromeDriver(capability);
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", Constants.DRIVERS_FILEPATH + "geckodriver_0_11_1.exe");
			driver = new FirefoxDriver(capability);
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver", Constants.DRIVERS_FILEPATH + "IEDriverServer_x64_2_53_1.exe");
			driver = new InternetExplorerDriver(capability);
			break;
		case "phantomjs":
			System.setProperty("phantomjs.binary.path", Constants.DRIVERS_FILEPATH + "phantomjs_v211.exe");
			driver = new PhantomJSDriver(capability);
			break;
		case "htmlunit":
			driver = new HtmlUnitDriver(true);
			break;
		}

		if (driver == null)
			ReportLog.fail("Failed to launch " + browser + " browser");
		else
			ReportLog.debug("Launched " + browser + " browser");

		ReportLog.addAttribute(browser_platform);
		if (browser != "htmlunit")
			driver.manage().window().maximize();
		return driver;
	}

	private static WebDriver getRemoteWebDriver(String browser_platform) {
		URL hubURL = getHubURL();
		DesiredCapabilities capability = DesiredCapabilitiesUtils.getDesiredCapability(browser_platform);
		WebDriver driver = new RemoteWebDriver(hubURL, capability);
		driver.manage().window().maximize();
		ReportLog.addAttribute(getTestRunMachineInfo(driver));
		return driver;
	}

	public static WebDriver getDriver(String browser_platform) {
		browser_platform = browser_platform.contains("_") ? browser_platform : browser_platform + "_" + Constants.OS_NAME.split(" ")[0];
		if (configProperty.hasProperty("GridExecution") && configProperty.getProperty("GridExecution").equals("true"))
			return getRemoteWebDriver(browser_platform);
		return getNativeDriver(browser_platform);
	}

}

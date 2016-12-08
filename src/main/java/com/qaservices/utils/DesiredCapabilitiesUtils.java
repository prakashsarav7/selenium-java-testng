package com.qaservices.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DesiredCapabilitiesUtils {

	public static DesiredCapabilities getDesiredCapability(String browserWithPlatform) {
		DesiredCapabilities capability = null;

		String browser = browserWithPlatform.split("_")[0].toLowerCase();
		String platform = browserWithPlatform.split("_")[1].toUpperCase();

		switch (browser) {

		case "chrome":
			ChromeOptions opt = new ChromeOptions();
			opt.addArguments(Constants.CHROME_ARGS);
			if (browserWithPlatform.split("_").length > 2) {
				List<String> browserDetails = new ArrayList<>(Arrays.asList(browserWithPlatform.split("_")));
				browserDetails = browserDetails.subList(2, browserDetails.size());
				String deviceName = String.join("_", browserDetails);
				opt.setExperimentalOption("mobileEmulation", new DeviceBrowserEmulator().getDeviceEmulationData(deviceName));
			}
			capability = DesiredCapabilities.chrome();
			capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			capability.setCapability(ChromeOptions.CAPABILITY, opt);
			break;

		case "firefox":
			capability = DesiredCapabilities.firefox();
			capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
			break;

		case "ie":
			capability = DesiredCapabilities.internetExplorer();
			capability.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
			capability.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capability.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			capability.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			break;

		case "phantomjs":
			capability = DesiredCapabilities.phantomjs();
			break;

		case "safari":
			capability = DesiredCapabilities.safari();
			break;

		default:
			throw new RuntimeException("Currently " + browserWithPlatform + " is not supported");

		}

		capability.setPlatform(Platform.fromString(platform));
		return capability;
	}
}

package com.qaservices.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

public class ScreenShotUtils {

	private static String screenShotFolderPath;
	private static AtomicInteger screenShotIndex = new AtomicInteger(0);

	static {
		screenShotFolderPath = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory()).getParent() + File.separator + "ScreenShot" + File.separator;
		File screenShotFolder = new File(screenShotFolderPath);

		if (screenShotFolder.exists()) {
			Arrays.asList(screenShotFolder.listFiles()).forEach(file -> {
				file.delete();
			});
		} else {
			screenShotFolder.mkdir();
		}
	}

	public static void takeScreenShot(WebDriver driver, String fileName) {
		File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File destination = new File(fileName);
		try {
			FileUtils.copyFile(screenShot, destination);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		screenShot.delete();
	}

	public static String getScreenShotWithLink(WebDriver driver) {
		String inputFile = Reporter.getCurrentTestResult().getName() + "_" + screenShotIndex.incrementAndGet() + ".png";
		takeScreenShot(driver, screenShotFolderPath + inputFile);
		return "&emsp;<a style=\"text-decoration: none;\" href=\"." + File.separator + "ScreenShot" + File.separator + inputFile + "\" target=\"_blank\">[ScreenShot]</a>";
	}
}

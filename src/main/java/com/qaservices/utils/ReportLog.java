package com.qaservices.utils;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.StringUtil;

public class ReportLog {

	private static ExtentReports extentReports;
	private static HashMap<Integer, ExtentTest> testMethods = new HashMap<Integer, ExtentTest>();
	private static HashMap<String, ExtentTest> testClasses = new HashMap<String, ExtentTest>();

	private static final String TEST_TITLE_HTML_BEGIN = "<div class=\"test-title\"><strong><font size = \"4\" color = \"#000080\">";
	private static final String TEST_TITLE_HTML_END = "</font></strong></div><br/><div><strong>Steps:</strong></div><br/>";
	private static final String EXTENTREPORT_DESC_HTML_BEGIN = "<strong><font size = \"4\" color = \"#000080\">";
	private static final String EXTENTREPORT_DESC_HTML_END = "</font></strong>";

	private static final String MESSAGE_HTML_BEGIN = "<div class=\"test-message\">";
	private static final String MESSAGE_HTML_END = "</div>";

	private static final String EVENT_HTML_BEGIN = "<div class=\"test-event\"><font color=\"maroon\"><small>---";
	private static final String EVENT_HTML_END = "</small></font></div>";

	private static final String PASS_HTML_BEGIN = "<div class=\"test-result\"><font color=\"green\"><strong>";
	private static final String FAIL_HTML_BEGIN = "<div class=\"test-result\"><font color=\"red\"><strong>";
	private static final String SKIP_HTML_BEGIN = "<div class=\"test-result\"><font color=\"dodgerblue\"><strong>";
	private static final String WARN_HTML_BEGIN = "<div class=\"test-result\"><font color=\"orange\">";

	private static final String LOG_HTML_END = "</strong></font></div>";
	private static final String WARN_HTML_END = "</font></div>";

	private static Logger getLogger() {
		return LogManager.getLogger(Thread.currentThread().getName());
	}

	private static void logToStandardReport(String message) {
		Reporter.log(message);
	}

	private static ExtentHtmlReporter getExtentHTMLReporterInstance() {
		String reportFilePath = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory()).getParent() + File.separator + "ExtentReport.html";
		ExtentHtmlReporter htmlReport = new ExtentHtmlReporter(reportFilePath);
		htmlReport.config().setDocumentTitle("QA Test Report");
		htmlReport.config().setTheme(Theme.STANDARD);
		htmlReport.config().setEncoding("UTF-8");
		htmlReport.config().setProtocol(Protocol.HTTPS);
		htmlReport.config().setReportName("Automation Report");
		htmlReport.config().setJS("$(document).ready(function() {"
									+ "$('td.status.debug').parent().hide();"
									+ "$('a.brand-logo').attr('href','http://www.google.com');"
									+ "$('a.brand-logo').html('QA');"
									+ "$('#nav-mobile>a')[1].remove();"
									+ "$(\"a[view='dashboard-view']\").click();"
								+ "});"
								+ "window.addEventListener('keyup', checkKeyUp, true);"
								+ "window.addEventListener('keydown', checkKeyDown, true);"
								+ "var keys = [];"
								+ "function checkKeyDown(e) {"
									+ "keys[e.keyCode] = true;"
									+ "if(keys[17] && keys[18] && keys[66]) {"
										+ "$('td.status.debug').parent().fadeToggle(500);"
									+ "}"
								+ "}"
								+ "function checkKeyUp(e) {"
									+ "keys[e.keyCode] = false;"
								+ "}");
		return htmlReport;
	}

	private static synchronized ExtentReports getExtentReporterInstance() {
		if (extentReports == null) {
			extentReports = new ExtentReports();
			extentReports.attachReporter(getExtentHTMLReporterInstance());
		}
		return extentReports;
	}

	private static ExtentTest getClassNode(String className) {
		ExtentTest classNode = null;
		if (testClasses.containsKey(className)) {
			classNode = testClasses.get(className);
		} else {
			classNode = getExtentReporterInstance().createTest(className);
			testClasses.put(className, classNode);
		}
		return classNode;
	}

	private static ExtentTest createExtentTest(String description) {
		ExtentTest extentTest = null;
		Integer hashCode = Reporter.getCurrentTestResult().hashCode();
		String methodName = Reporter.getCurrentTestResult().getName();
		String className = Reporter.getCurrentTestResult().getTestClass().getRealClass().getName();
		if (testMethods.containsKey(hashCode)) {
			extentTest = testMethods.get(hashCode);
			if (StringUtil.isNotNullOrEmpty(description)) {
				extentTest.getModel().setDescription(description);
			}
		} else {
			extentTest = getClassNode(className).createNode(methodName, description)/*.assignCategory(Reporter.getCurrentTestResult().getMethod().getGroups())*/;
//			extentTest = getExtentReporterInstance().createTest(className + "#" + methodName, description)/*.assignCategory(Reporter.getCurrentTestResult().getMethod().getGroups())*/;
			testMethods.put(hashCode, extentTest);
		}
		return extentTest;
	}

	public static void testCaseInfo(String description) {
		getLogger().info("****             " + description + "             ****");
		logToStandardReport(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END);
		createExtentTest(EXTENTREPORT_DESC_HTML_BEGIN + description + EXTENTREPORT_DESC_HTML_END);
	}

	private static ExtentTest getExtentTest() {
		return createExtentTest("");
	}

	public static void addAttribute(String attribute) {
		Object params[] = Reporter.getCurrentTestResult().getParameters();
		if (params == null || params.length == 0) {
			params = new Object[] { attribute };
		}
		Reporter.getCurrentTestResult().setParameters(params);
		getExtentTest().assignAuthor(attribute);
	}

	public static void info(String message) {
		getLogger().info(message);
		logToStandardReport(MESSAGE_HTML_BEGIN + message + MESSAGE_HTML_END);
		getExtentTest().info(message);
	}

	public static void info(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		getLogger().info(message);
		logToStandardReport(MESSAGE_HTML_BEGIN + logMessage + MESSAGE_HTML_END);
		getExtentTest().info(logMessage);
	}

	public static void debug(String message) {
		String currDate = DateTimeUtils.getCurrentTime("dd MMM HH:mm:ss SSS");
		getLogger().debug(message);
		logToStandardReport(EVENT_HTML_BEGIN + currDate + " - " + message + EVENT_HTML_END);
		getExtentTest().debug(message);
	}

	public static void warning(String message) {
		getLogger().warn(message);
		logToStandardReport(WARN_HTML_BEGIN + message + WARN_HTML_END);
		getExtentTest().warning(message);
	}

	public static void warning(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		getLogger().warn(message);
		logToStandardReport(WARN_HTML_BEGIN + logMessage + WARN_HTML_END);
		getExtentTest().warning(logMessage);
	}

	public static void pass(String message) {
		getLogger().info(message);
		logToStandardReport(PASS_HTML_BEGIN + message + LOG_HTML_END);
		getExtentTest().pass(message);
	}

	public static void pass(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		getLogger().info(message);
		logToStandardReport(PASS_HTML_BEGIN + logMessage + LOG_HTML_END);
		getExtentTest().pass(logMessage);
	}

	public static void fail(String message) {
		getLogger().error(message);
		logToStandardReport("<!--FAIL-->" + FAIL_HTML_BEGIN + message + LOG_HTML_END);
		getExtentTest().fail(message);
		throw new AssertionError(message);
	}

	public static void fail(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		getLogger().error(message);
		logToStandardReport("<!--FAIL-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		getExtentTest().fail(logMessage);
		throw new AssertionError(message);
	}

	public static void softFail(String message) {
		getLogger().error(message);
		logToStandardReport("<!--FAILSOFT-->" + FAIL_HTML_BEGIN + message + LOG_HTML_END);
		getExtentTest().fail(message);
	}

	public static void softFail(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		getLogger().error(message);
		logToStandardReport("<!--FAILSOFT-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		getExtentTest().fail(logMessage);
	}

	private static void fatal(Exception t) {
		getLogger().fatal(t);
		logToStandardReport("<!--UnhandledException-->" + FAIL_HTML_BEGIN + t.getMessage() + LOG_HTML_END);
		getExtentTest().fatal(t);
	}

	private static void fatal(Exception t, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? t.getMessage() + ScreenShotUtils.getScreenShotWithLink(driver) : t.getMessage();
		getLogger().fatal(t);
		logToStandardReport("<!--UnhandledException-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		getExtentTest().fatal(logMessage);
	}

	private static void skip(Exception t) {
		getLogger().info(t);
		logToStandardReport(SKIP_HTML_BEGIN + t.getMessage() + LOG_HTML_END);
		getExtentTest().skip(t);
	}

	private static void skip(Exception t, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? t.getMessage() + ScreenShotUtils.getScreenShotWithLink(driver) : t.getMessage();
		getLogger().info(t);
		logToStandardReport(SKIP_HTML_BEGIN + logMessage + LOG_HTML_END);
		getExtentTest().skip(logMessage);
	}

	public static void exception(Exception e) throws Exception {
		if (e instanceof SkipException) {
			skip(e);
		} else {
			fatal(e);
		}
		throw e;
	}

	public static void exception(Exception e, WebDriver driver) throws Exception {
		if (e instanceof SkipException) {
			skip(e, driver, true);
		} else {
			fatal(e, driver, true);
		}
		throw e;
	}

	public static void assertTrue(boolean condition, String passMessage, String failMessage) {
		if (condition) {
			pass(passMessage);
		} else {
			fail(failMessage);
		}
	}

	public static void assertTrue(boolean condition, String passMessage, String failMessage, WebDriver driver) {
		if (condition) {
			pass(passMessage, driver, true);
		} else {
			fail(failMessage, driver, true);
		}
	}

	public static void assertSoftTrue(boolean condition, String passMessage, String failMessage) {
		if (condition) {
			pass(passMessage);
		} else {
			softFail(failMessage);
		}
	}

	public static void assertSoftTrue(boolean condition, String passMessage, String failMessage, WebDriver driver) {
		if (condition) {
			pass(passMessage, driver, true);
		} else {
			softFail(failMessage, driver, true);
		}
	}

	public static void endTest() {
		getExtentReporterInstance().flush();
		String reporterOutput = Reporter.getOutput(Reporter.getCurrentTestResult()).toString();
		if (reporterOutput.matches(".*(FAILSOFT|FAIL|UnhandledException).*")) {
			fail("Test Failed. Check the steps above in red color.");
		}
		getLogger().info("****             End             ****");
	}

	public static void flushExtentReport() {
		if (extentReports != null) {
			extentReports.flush();
		}
	}

}

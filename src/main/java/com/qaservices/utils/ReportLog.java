package com.qaservices.utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.utils.StringUtil;

public class ReportLog {

	private static ExtentReports extentReports;
	private static HashMap<Integer, ExtentTest> testMethods = new HashMap<Integer, ExtentTest>();
	private static HashMap<String, ExtentTest> testClasses = new HashMap<String, ExtentTest>();
	private static PropertyUtil configProperty = PropertyUtil.getEnvironmentProperties();

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

	private static String extentReportOutputFilePath = "";
	private static boolean disableExtentReports = configProperty.isTrue("DisableExtentReports");
	private static boolean disableLogger = configProperty.isTrue("DisableLogger");

	private static Logger getLogger() {
		return LogManager.getLogger(Thread.currentThread().getName());
	}

	private static String callerClass() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	private static void logToLogger(String callerClass, Level logLevel, String message, Throwable t) {
		if (disableLogger) {
			return;
		}
		getLogger().log(callerClass, logLevel, message, t);
	}

	private static void logToStandardReport(String message) {
		Reporter.log(message);
	}

	private static void logToExtentReport(Status status, Object details, String... attributes) {
		if (disableExtentReports) {
			return;
		}
		ExtentTest test = getExtentTest();
		if (test == null) {
			return;
		}
		if (status == null && details == null && attributes.length > 0) {
			test.assignAuthor(attributes);
			return;
		}
		if (details instanceof String) {
			test.log(status, details.toString());
		} else if (details instanceof Throwable) {
			test.log(status, (Throwable) details);
		}
	}

	private static ExtentHtmlReporter getExtentHTMLReporterInstance() {
		ExtentHtmlReporter htmlReport = new ExtentHtmlReporter(extentReportOutputFilePath);
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
		ITestResult result = Reporter.getCurrentTestResult();
		Integer hashCode = result == null ? Thread.currentThread().hashCode() : result.hashCode();
		String methodName = result == null ? Thread.currentThread().getName() : result.getName();
		String className = result == null ? Thread.currentThread().getClass().getName() : result.getTestClass().getRealClass().getName();
		if (testMethods.containsKey(hashCode)) {
			extentTest = testMethods.get(hashCode);
			if (StringUtil.isNotNullOrEmpty(description)) {
				extentTest.getModel().setDescription(description);
			}
		} else {
			if (result == null || !result.getMethod().isTest()) {

			} else {
				extentReportOutputFilePath = new File(result.getTestContext().getOutputDirectory()).getParent() + File.separator + "ExtentReport.html";
				extentTest = getClassNode(className).createNode(methodName, description).assignCategory(result.getMethod().getGroups());
//				extentTest = getExtentReporterInstance().createTest(className + "#" + methodName, description).assignCategory(Reporter.getCurrentTestResult().getMethod().getGroups());
				testMethods.put(hashCode, extentTest);
			}
		}
		return extentTest;
	}

	public static void testCaseInfo(String description) {
		logToLogger(callerClass(), Level.INFO, "****             " + description + "             ****", null);
		logToStandardReport(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END);
		if (!disableExtentReports) {
			createExtentTest(EXTENTREPORT_DESC_HTML_BEGIN + description + EXTENTREPORT_DESC_HTML_END);
		}
		Reporter.getCurrentTestResult().getMethod().setDescription(description);
	}

	private static ExtentTest getExtentTest() {
		return createExtentTest("");
	}

	public static void setAttributes(String... attributes) {
		Object params[] = Reporter.getCurrentTestResult().getParameters();
		if (params == null || params.length == 0) {
			params = new Object[] { attributes };
		}
		Reporter.getCurrentTestResult().setParameters(params);
		logToExtentReport(null, null, attributes);
	}

	public static void info(String message) {
		logToLogger(callerClass(), Level.INFO, message, null);
		logToStandardReport(MESSAGE_HTML_BEGIN + message + MESSAGE_HTML_END);
		logToExtentReport(Status.INFO, message);
	}

	public static void info(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		logToLogger(callerClass(), Level.INFO, message, null);
		logToStandardReport(MESSAGE_HTML_BEGIN + logMessage + MESSAGE_HTML_END);
		logToExtentReport(Status.INFO, logMessage);
	}

	public static void debug(String message) {
		String currDate = DateTimeUtils.getCurrentTime("dd MMM HH:mm:ss SSS");
		logToLogger(callerClass(), Level.DEBUG, message, null);
		logToStandardReport(EVENT_HTML_BEGIN + currDate + " - " + message + EVENT_HTML_END);
		logToExtentReport(Status.DEBUG, message);
	}

	public static void warning(String message) {
		logToLogger(callerClass(), Level.WARN, message, null);
		logToStandardReport(WARN_HTML_BEGIN + message + WARN_HTML_END);
		logToExtentReport(Status.INFO, WARN_HTML_BEGIN + message + WARN_HTML_END);
	}

	public static void warning(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		logToLogger(callerClass(), Level.WARN, message, null);
		logToStandardReport(WARN_HTML_BEGIN + logMessage + WARN_HTML_END);
		logToExtentReport(Status.INFO, WARN_HTML_BEGIN + logMessage + WARN_HTML_END);
	}

	public static void pass(String message) {
		logToLogger(callerClass(), Level.INFO, message, null);
		logToStandardReport(PASS_HTML_BEGIN + message + LOG_HTML_END);
		logToExtentReport(Status.PASS, message);
	}

	public static void pass(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		logToLogger(callerClass(), Level.INFO, message, null);
		logToStandardReport(PASS_HTML_BEGIN + logMessage + LOG_HTML_END);
		logToExtentReport(Status.PASS, logMessage);
	}

	public static void fail(String message) {
		logToLogger(callerClass(), Level.ERROR, message, null);
		logToStandardReport("<!--FAIL-->" + FAIL_HTML_BEGIN + message + LOG_HTML_END);
		logToExtentReport(Status.FAIL, message);
		Assert.fail(message);
	}

	public static void fail(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		logToLogger(callerClass(), Level.ERROR, message, null);
		logToStandardReport("<!--FAIL-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		logToExtentReport(Status.FAIL, logMessage);
		Assert.fail(message);
	}

	public static void softFail(String message) {
		logToLogger(callerClass(), Level.ERROR, message, null);
		logToStandardReport("<!--FAILSOFT-->" + FAIL_HTML_BEGIN + message + LOG_HTML_END);
		logToExtentReport(Status.FAIL, message);
	}

	public static void softFail(String message, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? message + ScreenShotUtils.getScreenShotWithLink(driver) : message;
		logToLogger(callerClass(), Level.ERROR, message, null);
		logToStandardReport("<!--FAILSOFT-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		logToExtentReport(Status.FAIL, logMessage);
	}

	private static void fatal(Exception t) {
		logToLogger(callerClass(), Level.FATAL, t.getMessage(), t);
		logToStandardReport("<!--UnhandledException-->" + FAIL_HTML_BEGIN + t.getMessage() + LOG_HTML_END);
		logToExtentReport(Status.FAIL, t);
	}

	private static void fatal(Exception t, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? t.getMessage() + ScreenShotUtils.getScreenShotWithLink(driver) : t.getMessage();
		logToLogger(callerClass(), Level.FATAL, t.getMessage(), t);
		logToStandardReport("<!--UnhandledException-->" + FAIL_HTML_BEGIN + logMessage + LOG_HTML_END);
		logToExtentReport(Status.FAIL, logMessage);
	}

	private static void skip(Exception t) {
		logToLogger(callerClass(), Level.INFO, t.getMessage(), t);
		logToStandardReport(SKIP_HTML_BEGIN + t.getMessage() + LOG_HTML_END);
		logToExtentReport(Status.SKIP, t);
	}

	private static void skip(Exception t, WebDriver driver, boolean takeScreenShot) {
		String logMessage = takeScreenShot ? t.getMessage() + ScreenShotUtils.getScreenShotWithLink(driver) : t.getMessage();
		logToLogger(callerClass(), Level.INFO, t.getMessage(), t);
		logToStandardReport(SKIP_HTML_BEGIN + logMessage + LOG_HTML_END);
		logToExtentReport(Status.SKIP, logMessage);
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

	public static void softAssertTrue(boolean condition, String passMessage, String failMessage) {
		if (condition) {
			pass(passMessage);
		} else {
			softFail(failMessage);
		}
	}

	public static void softAssertTrue(boolean condition, String passMessage, String failMessage, WebDriver driver) {
		if (condition) {
			pass(passMessage, driver, true);
		} else {
			softFail(failMessage, driver, true);
		}
	}

	public static void endTest() {
		String reporterOutput = Reporter.getOutput(Reporter.getCurrentTestResult()).toString();
		if (reporterOutput.matches(".*(--FAIL--|--FAILSOFT--|--UnhandledException--).*")) {
			fail("Test Failed. Check the steps above in red color.");
		}
		logToLogger(callerClass(), Level.INFO, "****             End             ****", null);
	}

	public static void retryingTest(ITestResult result) {
		try {
			ExtentTest extentTest = testMethods.get(result.hashCode());
			extentTest.getModel().getLogContext().getAll().forEach(log -> {
				if (log.getStatus() == Status.FAIL) {
					log.setStatus(Status.SKIP);
				}
			});
			extentTest.getModel().setStatus(Status.SKIP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void flushExtentReport(List<ITestResult> allTestCaseResults, String outputDirectory) {
		if (disableExtentReports) {
			return;
		}
		String className;
		String methodName;
		String description;
		Integer hashCode;
		ExtentTest extentTest;
		extentReportOutputFilePath = outputDirectory + File.separator + "ExtentReport.html";
		if (allTestCaseResults.size() == 0) {
			getExtentReporterInstance().createTest("Empty TestNG Suite", "To run tests, please add '@Test' annotation to your test methods").warning(new Exception("To run tests, please add '@Test' annotation to your test methods"));
		}
		for (ITestResult result : allTestCaseResults) {
			hashCode = result.hashCode();
			Throwable t = result.getThrowable();
			if (!testMethods.containsKey(hashCode)) {
				className = result.getTestClass().getRealClass().getName();
				methodName = result.getName();
				description = result.getMethod().getDescription();
				extentTest = getClassNode(className).createNode(methodName, EXTENTREPORT_DESC_HTML_BEGIN + description + EXTENTREPORT_DESC_HTML_END).assignCategory(result.getMethod().getGroups());
				extentTest.getModel().setStartTime(DateTimeUtils.convertMillisecondsToDate(result.getStartMillis()));
				List<String> logs = Reporter.getOutput(result);
				Status status = Status.INFO;
				for (String logMessage : logs) {
					if (logMessage.contains("test-event")) {
						status = Status.DEBUG;
					} else if (logMessage.contains("test-result")) {
						if (logMessage.contains("green")) {
							status = Status.PASS;
						} else if (logMessage.contains("red")) {
							status = Status.FAIL;
						} else if (logMessage.contains("dodgerblue")) {
							status = Status.SKIP;
						} else {
							status = Status.WARNING;
						}
					}
					extentTest.log(status, logMessage);
				}

				if (t != null) {
					if (t instanceof SkipException) {
						extentTest.skip(t);
					} else {
						extentTest.fail(t);
					}
				}
				extentTest.getModel().setEndTime(DateTimeUtils.convertMillisecondsToDate(result.getEndMillis()));
				testMethods.put(hashCode, extentTest);
			} else {
				extentTest = testMethods.get(hashCode);
				if (!extentTest.getModel().hasException() && t != null) {
					if (result.getStatus() == 2) {
						extentTest.fail(t);
					} else {
						extentTest.skip(t);
					}
					extentTest.getModel().setEndTime(DateTimeUtils.convertMillisecondsToDate(result.getEndMillis()));
				}
			}
		}
		getExtentReporterInstance().flush();
	}

}

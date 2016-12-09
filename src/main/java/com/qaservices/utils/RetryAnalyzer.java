package com.qaservices.utils;

import java.util.Arrays;
import java.util.List;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.log4testng.Logger;

public class RetryAnalyzer implements IRetryAnalyzer {
	private static PropertyUtil configProperty = PropertyUtil.getEnvironmentProperties();
	private static final Logger LOG = Logger.getLogger(RetryAnalyzer.class);
	
	private int retriedCount = 0;
	private int maxRetryCount = configProperty.hasProperty("MaxRetryCount") ? Integer.parseInt(configProperty.getProperty("MaxRetryCount")) : 0;

	private List<String> includeExceptions = configProperty.hasProperty("IncludeExceptions") ? Arrays.asList(configProperty.getProperty("IncludeExceptions").split("\\|")) : Arrays.asList();
	private List<String> excludeExceptions = configProperty.hasProperty("ExcludeExceptions") ? Arrays.asList(configProperty.getProperty("ExcludeExceptions").split("\\|")) : Arrays.asList();

	@Override
	public boolean retry(ITestResult result) {
		boolean isRetry = false;
		String exception = result.getThrowable().getClass().getSimpleName();
		if ((retriedCount < maxRetryCount) && ((includeExceptions.size() == 0) || (includeExceptions.contains(exception))) && (!excludeExceptions.contains(exception))) {
			LOG.debug(result.getName() + " failed with " + result.getThrowable().getClass().getName());
			LOG.debug("Retrying count for " + result.getName() + " <=> " + ++retriedCount);
			isRetry = true;
		}
		return isRetry;
	}
}

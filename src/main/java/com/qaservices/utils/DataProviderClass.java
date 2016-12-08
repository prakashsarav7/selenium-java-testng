package com.qaservices.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviderClass {
	private static PropertyUtil configProperties = PropertyUtil.getEnvironmentProperties();

	@DataProvider(parallel = true)
	public static Iterator<Object[]> dataProviderMethod() {

		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		List<String> browsers = Arrays.asList(configProperties.getProperty("Browsers").split("|"));
		List<String> platforms = Arrays.asList(configProperties.getProperty("Platforms").split("|"));

		if (configProperties.isTrue("DeviceBrowserEmulation")) {
			Arrays.asList(configProperties.getProperty("Devices").split("|")).forEach(device -> {
				dataToBeReturned.add(new Object[] { "chrome_" + platforms.get(0) + "_" + device });
			});
		} else {
			assert browsers.size() == platforms.size();
			for (int i = 0; i < browsers.size(); i++) {
				dataToBeReturned.add(new Object[] { browsers.get(i) + "_" + platforms.get(i) });
			}
		}

		return dataToBeReturned.iterator();
	}

}

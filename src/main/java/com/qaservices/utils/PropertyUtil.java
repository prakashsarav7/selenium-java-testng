package com.qaservices.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

public class PropertyUtil {

	private String filePath;
	private Properties properties;
	private static PropertyUtil environmentPorperties = null;

	public PropertyUtil(String filePath, boolean... readExisting) {
		this.filePath = filePath;
		if (readExisting.length == 0 || readExisting[0])
			loadProperties();
	}

	private void loadProperties() {
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			properties = new Properties();
			properties.load(fis);
			fis.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hasProperty(String key) {
		return properties.getProperty(key) != null;
	}

	public String getProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null)
			throw new RuntimeException("No entry found for key - " + key);
		return value;
	}

	public String setProperty(String key, String value) {
		try {
			File propFile = new File(filePath);
			if (!propFile.exists()) {
				String comment = "#Created at " + DateTimeUtils.getCurrentTime("dd/MMM/yyyy hh:mm:ss a");
				FileUtils.writeStringToFile(propFile, comment, Charset.defaultCharset());
			}
			loadProperties();
			String oldvalue = (String) properties.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(propFile);
			properties.store(out, null);
			out.close();
			return oldvalue;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void setProperties(Map<String, String> mapValues) {
		try {
			File propFile = new File(filePath);
			if (!propFile.exists()) {
				String comment = "#Created at " + DateTimeUtils.getCurrentTime("dd/MMM/yyyy hh:mm:ss a");
				FileUtils.writeStringToFile(propFile, comment, Charset.defaultCharset());
			}
			loadProperties();
			for (Entry<String, String> entry : mapValues.entrySet()) {
				properties.setProperty(entry.getKey(), entry.getValue());
			}
			FileOutputStream out = new FileOutputStream(propFile);
			properties.store(out, null);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static PropertyUtil getEnvironmentProperties() {
		if (environmentPorperties == null) {
			environmentPorperties = new PropertyUtil(Constants.CONFIG_PROPERTIES_FILEPATH, true);
		}
		return environmentPorperties;
	}
}

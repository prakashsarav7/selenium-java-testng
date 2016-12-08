package com.qaservices.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {

	public static final List<String> SUPPORTED_BROWSERS = Arrays.asList("chrome", "firefox", "ie", "phantomjs", "htmlunit");
	public static final List<String> SUPPORTED_PLATFORMS = Arrays.asList("windows");

	public static final List<String> CHROME_ARGS = Arrays.asList("--ignore-certificate-errors", "--disable-bundled-ppapi-flash", "--disable-extensions", "--disable-web-security", "--always-authorize-plugins", "--allow-running-insecure-content", "--test-type", "--enable-npapi");

	public static final String OS_NAME = System.getProperty("os.name");
	public static final String USER_DIR = System.getProperty("user.dir");

	public static final String DRIVERS_FILEPATH = USER_DIR + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "drivers" + File.separator;
	public static final String CONFIG_PROPERTIES_FILEPATH = USER_DIR + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config.properties";

}

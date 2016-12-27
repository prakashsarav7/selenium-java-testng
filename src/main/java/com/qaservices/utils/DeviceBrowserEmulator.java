package com.qaservices.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DeviceBrowserEmulator {

	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String PIXELRATIO = "pixelRatio";
	private static final String DEVICEMETRICS = "deviceMetrics";
	private static final String USERAGENT = "userAgent";

	static final List<String> DEVICES = Arrays.asList(
			"galaxy_tab4_android5.1_chrome_landscape(1280*800)", "galaxy_tab4_android5.1_chrome_portrait(800*1280)", 
			"galaxy_tab4_7_android4.4.2_chrome_landscape(961*600)", "galaxy_tab4_7_android4.4.2_chrome_portrait(600*961)", 
			"galaxy_tab4_android4.4.2_chrome_landscape(1280*800)", "galaxy_tab4_android4.4.2_chrome_portrait(800*1280)", 
			"galaxy_tab3_android4.2.2_chrome_landscape(1280*800)", "galaxy_tab3_android4.2.2_chrome_portrait(800*1280)", 
			"ipad4_ios10_safari_landscape(1024*768)", "ipad4_ios10_safari_portrait(768*1024)", 
			"ipad4_ios9_safari_landscape(1024*768)", "ipad4_ios9_safari_portrait(768*1024)", 
			"ipad4_ios8_safari_landscape(1024*768)", "ipad4_ios8_safari_portrait(768*1024)", 
			"ipad4_ios7_safari_landscape(1024*768)", "ipad4_ios7_safari_portrait(768*1024)", 
			"chromebook(1366*768)"
			);

	public static HashMap<String, Object> getDeviceEmulationData(String deviceName) {

		HashMap<String, Object> deviceMetrics = new HashMap<String, Object>();
		HashMap<String, Object> mobileEmulation = new HashMap<String, Object>();

		switch (deviceName) {

		case "galaxy_tab4_android5.1_chrome_landscape(1280*800)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1280));
			deviceMetrics.put(HEIGHT, Integer.valueOf(800));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 5.1.1; SM-T335 Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Safari/537.36");
			break;

		case "galaxy_tab4_android5.1_chrome_portrait(800*1280)":
			deviceMetrics.put(WIDTH, Integer.valueOf(800));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1280));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 5.1.1; SM-T335 Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Safari/537.36");
			break;

		case "galaxy_tab4_7_android4.4.2_chrome_landscape(961*600)":
			deviceMetrics.put(WIDTH, Integer.valueOf(961));
			deviceMetrics.put(HEIGHT, Integer.valueOf(600));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.4.2; SM-T230NU Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Safari/537.36");
			break;

		case "galaxy_tab4_7_android4.4.2_chrome_portrait(600*961)":
			deviceMetrics.put(WIDTH, Integer.valueOf(600));
			deviceMetrics.put(HEIGHT, Integer.valueOf(961));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.4.2; SM-T230NU Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Safari/537.36");
			break;

		case "galaxy_tab4_android4.4.2_chrome_landscape(1280*800)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1280));
			deviceMetrics.put(HEIGHT, Integer.valueOf(800));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.4.2; SM-T531 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.84 Safari/537.36");
			break;

		case "galaxy_tab4_android4.4.2_chrome_portrait(800*1280)":
			deviceMetrics.put(WIDTH, Integer.valueOf(800));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1280));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.4.2; SM-T531 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.84 Safari/537.36");
			break;

		case "galaxy_tab3_android4.2.2_chrome_landscape(1280*800)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1280));
			deviceMetrics.put(HEIGHT, Integer.valueOf(800));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.2.2; SM-T110 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.94 Safari/537.36");
			break;

		case "galaxy_tab3_android4.2.2_chrome_portrait(800*1280)":
			deviceMetrics.put(WIDTH, Integer.valueOf(800));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1280));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (Linux; Android 4.2.2; SM-T110 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.94 Safari/537.36");
			break;

		case "ipad4_ios10_safari_landscape(1024*768)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1024));
			deviceMetrics.put(HEIGHT, Integer.valueOf(768));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A456 Safari/602.1");
			break;

		case "ipad4_ios10_safari_portrait(768*1024)":
			deviceMetrics.put(WIDTH, Integer.valueOf(768));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1024));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A456 Safari/602.1");
			break;

		case "ipad4_ios9_safari_landscape(1024*768)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1024));
			deviceMetrics.put(HEIGHT, Integer.valueOf(768));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G34 Safari/601.1");
			break;

		case "ipad4_ios9_safari_portrait(768*1024)":
			deviceMetrics.put(WIDTH, Integer.valueOf(768));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1024));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G34 Safari/601.1");
			break;

		case "ipad4_ios8_safari_landscape(1024*768)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1024));
			deviceMetrics.put(HEIGHT, Integer.valueOf(768));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4");
			break;

		case "ipad4_ios8_safari_portrait(768*1024)":
			deviceMetrics.put(WIDTH, Integer.valueOf(768));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1024));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4");
			break;

		case "ipad4_ios7_safari_landscape(1024*768)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1024));
			deviceMetrics.put(HEIGHT, Integer.valueOf(768));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");
			break;

		case "ipad4_ios7_safari_portrait(768*1024)":
			deviceMetrics.put(WIDTH, Integer.valueOf(768));
			deviceMetrics.put(HEIGHT, Integer.valueOf(1024));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53");
			break;

		case "chromebook(1366*768)":
			deviceMetrics.put(WIDTH, Integer.valueOf(1366));
			deviceMetrics.put(HEIGHT, Integer.valueOf(768));
			deviceMetrics.put(PIXELRATIO, Double.valueOf(2));
			mobileEmulation.put(DEVICEMETRICS, deviceMetrics);
			mobileEmulation.put(USERAGENT, "Mozilla/5.0 (X11; CrOS x86_64 8350.68.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			break;

		default:
			throw new RuntimeException("UserAgent data not available for " + deviceName);
		}

		return mobileEmulation;
	}

}

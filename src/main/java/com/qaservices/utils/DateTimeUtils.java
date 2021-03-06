package com.qaservices.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

	public static String convertMillisecondsToDuration(long milliseconds) {
		int seconds = Math.round(milliseconds / 1000) % 60;
		int minutes = Math.round(milliseconds / (1000 * 60)) % 60;
		int hours = Math.round(milliseconds / (1000 * 60 * 60));
		String time = (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
		return time;
	}

	public static String convertMillisecondsToTime(long milliseconds) {
		return new SimpleDateFormat("HH:mm:ss").format(convertMillisecondsToDate(milliseconds));
	}

	public static Date convertMillisecondsToDate(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		return calendar.getTime();
	}

	public static String getCurrentTime(String format) {
		return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
	}
}

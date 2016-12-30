package com.qaservices.utils;

public class StopWatch extends org.apache.commons.lang3.time.StopWatch {

	private static final long MILLIS_2_SECONDS = 1000L;

	/**
	 * <p>
	 * This method is used to find out whether the StopWatch is running. The
	 * stopwatch which's started and explicitly not stopped or suspended is
	 * considered as running.
	 * </p>
	 *
	 * @return boolean If the StopWatch is running.
	 * @since 3.2
	 */
	public boolean isRunning() {
		return isStarted() && !isStopped() && !isSuspended();
	}

	/**
	 * <p>
	 * Get the time on the stopwatch.
	 * </p>
	 * 
	 * <p>
	 * This is either the time between the start and the moment this method is
	 * called, or the amount of time between start and stop.
	 * </p>
	 * 
	 * @return the time in seconds
	 */
	public long getTimeInSeconds() {
		return getTime() / MILLIS_2_SECONDS;
	}
}

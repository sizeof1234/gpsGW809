package com.jsecode.utils;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Simple Timer
 * 
 * @author Jadic
 * @created 2012-11-14
 */
public class KKSimpleTimer {

	private ScheduledExecutorService scheduleService;
	private long initialDelay;
	private long period;
	private Runnable command;

	/**
	 * in default initialDelay is 60 seconds, period is 60 seconds
	 */
	public KKSimpleTimer(Runnable command) {
		this(command, 60, 60);
	}

	/**
	 * when the timer is started, execute the command after the initial delay
	 * seconds
	 * <p>
	 * time unit is seconds
	 */
	public KKSimpleTimer(Runnable command, long initialDelay, long period) {
		this.command = command;
		this.initialDelay = (initialDelay > 0 ? initialDelay : 60);
		this.period = (period > 0 ? period : 60);
	}

	/**
	 * run the simple timer
	 */
	public void start() {
		scheduleService = new ScheduledThreadPoolExecutor(2);
		final ScheduledFuture<?> future = scheduleService.scheduleAtFixedRate(command, initialDelay, period, TimeUnit.SECONDS);
		scheduleService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					future.get();
				} catch (CancellationException e) {
					//KKLog.info("timer cancelled:" + KKTool.getExceptionTip(e));
				} catch (InterruptedException e) {
					KKLog.info("timer interrupted:" + KKTool.getExceptionTip(e));
				} catch (ExecutionException e) {
					KKLog.info("timer execution err:" + KKTool.getExceptionTip(e));
				}
			}
		}, initialDelay, period, TimeUnit.SECONDS);
	}

	/**
	 * stop the simple timer
	 */
	public void stop() {
		scheduleService.shutdown();
	}

}

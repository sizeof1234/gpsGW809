/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.utils;

import java.lang.Thread.UncaughtExceptionHandler;

public class MyExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
        KKLog.error("An exception has been captured\n");
        KKLog.error(String.format("Thread:%s\n", t.getName()));
        KKLog.error(String.format("Exception: %s: %s:\n", e.getClass().getName(), KKTool.getExceptionTip(e)));
        KKLog.error(String.format("Thread status:%s\n", t.getState()));
	}

}

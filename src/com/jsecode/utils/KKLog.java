package com.jsecode.utils;

import org.apache.log4j.Logger;

public class KKLog {
	
	private final static Logger debugLogger;
	private final static Logger infoLogger;
	private final static Logger warnLogger;
	private final static Logger errorLogger;
	private final static Logger devCmdLogger;
	private final static Logger recvDataLogger;
	private final static Logger sendDataLogger;
	
	static {
		devCmdLogger = Logger.getLogger("devCmdLogger");
		recvDataLogger = Logger.getLogger("recvDataLogger");
		sendDataLogger = Logger.getLogger("sendDataLogger");
		debugLogger = Logger.getLogger("debugLogger");
		infoLogger = Logger.getLogger("infoLogger");
		warnLogger = Logger.getLogger("warnLogger");
		errorLogger = Logger.getLogger("errorLogger");
	}

	public static void main(String[] args) {
		debug("this is debug msg");
		info("this is info msg");
		warn("this is warn msg");
		error("this is error msg");
		devCmd("this is devReg Msg");
		recvData("this is recv data msg");
		sendData("this is send data msg");
	}
	
	public static void debug(Object message) {
		debugLogger.debug(message);
	}
	
	public static void info(Object message) {
		infoLogger.info(message);
	}
	
	public static void warn(Object message) {
		warnLogger.warn(message);
	}
	
	public static void error(Object message) {
		errorLogger.error(message);
	}
	
	public static void devCmd(Object message) {
		devCmdLogger.info(message);
	}
	
	public static void recvData(Object message) {
		recvDataLogger.info(message);
	}
	
	public static void sendData(Object message) {
		sendDataLogger.info(message);
	}
}

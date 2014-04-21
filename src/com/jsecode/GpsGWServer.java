package com.jsecode;

import superior.com.jsecode.SuperiorSimulator;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.SysParams;

/**
 * GPS服务启动统一入口
 * @author 	Jadic
 * @created 2014-4-17
 */
public class GpsGWServer {
	
	final static int SERVER_MODE_INFERIOR 					= 0;//只启动标准下级服务
	final static int SERVER_MODE_INFERIOR_AND_SUPERIOR 	= 1;//同时启动标准下级平台服务与模拟上级平台服务 
	final static int SERVER_MODE_SUPERIOR_STATNDALONE 		= 2;//只启动模拟上级平台服务

	public GpsGWServer() {
		int serverMode = SysParams.getInstance().getServerMode();
		if (serverMode > SERVER_MODE_SUPERIOR_STATNDALONE) {
			serverMode = 0;
		}
		
		switch (serverMode) {
		case SERVER_MODE_INFERIOR:
			KKLog.info("Start standard inferior 809 server...");
			new GW809Server();
			break;
		case SERVER_MODE_INFERIOR_AND_SUPERIOR:
			KKLog.info("Start standard inferior 809 server and superior simulator server...");
			GW809Server gw809Server = new GW809Server();
			SuperiorSimulator superiorSimulator = new SuperiorSimulator(gw809Server);
			superiorSimulator.start();
			break;
		case SERVER_MODE_SUPERIOR_STATNDALONE:
			KKLog.info("Start superior simulator server...");
			SuperiorSimulator superiorSimulator2 = new SuperiorSimulator(null);
			superiorSimulator2.start();
			break;
		default:
			KKLog.info("ServerMode value[" + serverMode + "] is invalid, please check config file");
			break;
		}
	}
	
	public static void main(String[] args) {
		new GpsGWServer();
	}
}

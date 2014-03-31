/**
 * @author 	Jadic
 * @created 2014-3-3
 */
package com.jsecode.utils;

import java.util.Map;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.jsecode.bean.DriverBean;
import com.jsecode.bean.TerminalBean;

/**
 * 所有公共的变量集
 */
public final class GlobalVar {
	
	public final static Map<String, TerminalBean> terminalMap = new ConcurrentHashMap<String, TerminalBean>();
	public final static Map<String, DriverBean> driverMap = new ConcurrentHashMap<String, DriverBean>();

	private GlobalVar() {
	}

}

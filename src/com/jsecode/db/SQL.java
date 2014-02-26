/**
 * @author 	Jadic
 * @created 2014-2-21
 */
package com.jsecode.db;

public final class SQL {
	
	//车辆信息
	public final static String CAR_INFO = "";
	
	//终端信息
	public final static String TERMINAL_INFO = "";
	
	public final static String CAR_MAX_ROWSCN = "select max(ora_rowscn) as maxr from tab_carinfo";

	private SQL() {
	}

}

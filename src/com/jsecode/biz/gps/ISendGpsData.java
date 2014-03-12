/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.biz.gps;

import java.util.Date;

import com.jsecode.biz.ISendData;

public interface ISendGpsData<T> extends ISendData<T>{
	
	public Date getDBLastUpdateSysTime();
	public long getGpsDataSendCount();

}

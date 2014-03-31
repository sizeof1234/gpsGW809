/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.biz.gps;

import java.util.Date;

import com.jsecode.biz.ISendData;

public interface ISendGpsData<T> extends ISendData<T>{
	
	/**
	 * 获取数据库相关数据最新更新时间
	 * @return
	 */
	public Date getDBLastUpdateSysTime();
	
	/**
	 * 获取发送线程已发送的GPS总数
	 * @return
	 */
	public long getGpsDataSendCount();
	
	/**
	 * 当前线程中未发送GPS总数
	 * @return
	 */
	public int getUnSendGpsDataCount();

}

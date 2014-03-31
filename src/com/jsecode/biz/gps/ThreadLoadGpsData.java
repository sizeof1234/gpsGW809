/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.biz.gps;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.jsecode.bean.GpsBean;
import com.jsecode.db.DBOper;
import com.jsecode.utils.KKLog;

/**
 * 数据库数据加载线程
 */
public class ThreadLoadGpsData implements Runnable {
	
	private ISendGpsData<GpsBean> iSendGpsData;
	private CountDownLatch dataLoadedSingal;
	private final static long FIRST_DB_SYS_TIME_AHEAD = 1000L * 60 * 60 * 24 * 30;

	public ThreadLoadGpsData(ISendGpsData<GpsBean> iSendGpsData, CountDownLatch dataLoadedSingal) {
		this.iSendGpsData = iSendGpsData;
		this.dataLoadedSingal = dataLoadedSingal;
	}

	@Override
	public void run() {
		try {//wait the terminal info loaded
			this.dataLoadedSingal.await();
		} catch (InterruptedException e) {
		}
		
		Date dbLastUpdateSysTime = this.iSendGpsData.getDBLastUpdateSysTime();
		if (dbLastUpdateSysTime.getTime() == 0) {
			Date dbSysTime = DBOper.getDBOper().getDBsysTime();
			dbLastUpdateSysTime = new Date(dbSysTime.getTime() - FIRST_DB_SYS_TIME_AHEAD);
		}
		List<GpsBean> list = DBOper.getDBOper().queryCurrentGpsData(dbLastUpdateSysTime);
		
		addGpsDataToQueue(list);
		KKLog.info("current load gps data count:" + list.size() 
				+ ",send count:" + this.iSendGpsData.getGpsDataSendCount()
				+ ",unsend count:" + this.iSendGpsData.getUnSendGpsDataCount());
	}
	
	private void addGpsDataToQueue(List<GpsBean> list) {
		if (list != null && list.size() > 0) {
			this.iSendGpsData.addListToQueue(list);
		}
	}

}

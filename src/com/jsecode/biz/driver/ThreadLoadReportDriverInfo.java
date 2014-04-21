/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz.driver;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.jsecode.bean.DriverBean;
import com.jsecode.biz.IAddDataToQueue;

/**
 * 定时获取实时上报驾驶员信息
 */
public class ThreadLoadReportDriverInfo implements Runnable {
	
	private IAddDataToQueue<DriverBean> sendData;
	private CountDownLatch dataLoadSignal;
	
	public ThreadLoadReportDriverInfo(IAddDataToQueue<DriverBean> sendData, CountDownLatch dataLoadSignal) {
		this.sendData = sendData;
		this.dataLoadSignal = dataLoadSignal;
	}

	@Override
	public void run() {
		try {//wait the terminal info loaded
			this.dataLoadSignal.await();
		} catch (InterruptedException e) {
		}
		
		List<DriverBean> list = null;
		//TODO load report driver info from database or memcached
		
		addReportDriverInfoToQueue(list);
	}

	private void addReportDriverInfoToQueue(List<DriverBean> list) {
		if (list != null && list.size() > 0) {
			this.sendData.addListToQueue(list);
		}
	}
	
}

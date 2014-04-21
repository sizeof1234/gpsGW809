/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz.applyForMonitor;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.jsecode.bean.IApplyForMonitorBean;
import com.jsecode.biz.IAddDataToQueue;

/**
 * 定时获取实时申请/取消交换车辆定位信息请求信息
 */
public class ThreadLoadApplyForMonitor implements Runnable {
	
	private IAddDataToQueue<IApplyForMonitorBean> sendApplyForMonitor;
	private CountDownLatch dataLoadSignal;

	public ThreadLoadApplyForMonitor(IAddDataToQueue<IApplyForMonitorBean> sendApplyForMonitor, CountDownLatch dataLoadSignal) {
		this.sendApplyForMonitor = sendApplyForMonitor;
		this.dataLoadSignal = dataLoadSignal;
	}

	@Override
	public void run() {
		try {//wait the terminal info loaded
			this.dataLoadSignal.await();
		} catch (InterruptedException e) {
		}
		
		List<IApplyForMonitorBean> list = null;
		//TODO load applyForMonitor beans from database or memcached
		
		addApplyForMonitorBeansToQueue(list);
	}

	private void addApplyForMonitorBeansToQueue(List<IApplyForMonitorBean> list) {
		if (list != null && list.size() > 0) {
			this.sendApplyForMonitor.addListToQueue(list);
		}
	}
}

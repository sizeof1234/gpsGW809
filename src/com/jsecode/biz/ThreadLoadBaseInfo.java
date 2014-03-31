/**
 * @author 	Jadic
 * @created 2014-3-3
 */
package com.jsecode.biz;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.jsecode.bean.DriverBean;
import com.jsecode.bean.TerminalBean;
import com.jsecode.db.DBOper;
import com.jsecode.utils.GlobalVar;

/**
 * 加载基础信息，在其他需要用到基础数据线程前先加载<p>
 * 所有加载的基础信息内容放在{@link GlobalVar}中
 */
public class ThreadLoadBaseInfo implements Runnable {

	private CountDownLatch dataLoadedSingal;
	
	public ThreadLoadBaseInfo(CountDownLatch dataLoadedSingal) {
		this.dataLoadedSingal = dataLoadedSingal;
	}

	@Override
	public void run() {
		List<TerminalBean> terminalList = DBOper.getDBOper().queryTerminals();
		updateTerminalInfo(terminalList);
		
		List<DriverBean> driverList = DBOper.getDBOper().queryDrivers();
		updateDriverInfo(driverList);
		
		this.dataLoadedSingal.countDown();
	}
	
	private void updateTerminalInfo(List<TerminalBean> terminalList) {
		if (terminalList != null) {
			Map<String, TerminalBean> terminalMap = GlobalVar.terminalMap;
			terminalMap.clear();
			for (TerminalBean terminalBean : terminalList) {
				terminalMap.put(terminalBean.getHostNo(), terminalBean);
			}
		}
	}

	private void updateDriverInfo(List<DriverBean> driverList) {
		if (driverList != null) {
			Map<String, DriverBean> driverMap = GlobalVar.driverMap;
			driverMap.clear();
			for (DriverBean driverBean : driverList) {
				driverMap.put(driverBean.getVehicleNo(), driverBean);
			}
		}
	}

}

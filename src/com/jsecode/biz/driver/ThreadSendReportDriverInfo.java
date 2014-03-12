/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz.driver;

import com.jsecode.IGW809;
import com.jsecode.bean.DriverBean;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.cmd.bean.TerminalBean;
import com.jsecode.cmd.up.req.CmdUpExgMsgReportDriverInfo;
import com.jsecode.utils.Const;
import com.jsecode.utils.GlobalVar;
import com.jsecode.utils.KKTool;

public class ThreadSendReportDriverInfo extends AbstractThreadSendData<DriverBean>{

	public ThreadSendReportDriverInfo(IGW809 gw809) {
		super(gw809, ThreadSendReportDriverInfo.class.getName());
	}

	@Override
	public void run() {
		DriverBean driver = null;
		while(!isInterrupted()) {
			while((driver = queue.poll()) != null) {
				sendReportDriverInfo(driver);
			}
			waitNewData();
		}
	}

	private boolean sendReportDriverInfo(DriverBean driver) {
		TerminalBean terminal = GlobalVar.terminalMap.get(driver.getVehicleNo());
		if (terminal != null) {
			CmdUpExgMsgReportDriverInfo cmdUpExgMsgReportDriverInfo = new CmdUpExgMsgReportDriverInfo();
			cmdUpExgMsgReportDriverInfo.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgReportDriverInfo.setSubMsgId(Const.UP_EXG_MSG_REPORT_DRIVER_INFO);
			cmdUpExgMsgReportDriverInfo.setVehicleNo(KKTool.toFixedLenGBKBytes(driver.getVehicleNo(), 21));
			cmdUpExgMsgReportDriverInfo.setVehicleColor(terminal.getHostPlateColor());
			cmdUpExgMsgReportDriverInfo.setDriverName(KKTool.toFixedLenGBKBytes(driver.getName(), 16));
			cmdUpExgMsgReportDriverInfo.setDriverId(KKTool.toFixedLenGBKBytes(driver.getId(), 20));
			cmdUpExgMsgReportDriverInfo.setLicence(KKTool.toFixedLenGBKBytes(driver.getLicence(), 40));
			cmdUpExgMsgReportDriverInfo.setOrgName(KKTool.toFixedLenGBKBytes(driver.getOrgName(), 200));
		}
		return false;
	}

}

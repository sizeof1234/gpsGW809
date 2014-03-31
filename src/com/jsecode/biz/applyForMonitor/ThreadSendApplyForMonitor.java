/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz.applyForMonitor;

import com.jsecode.IGW809;
import com.jsecode.bean.ApplyForMonitorEndBean;
import com.jsecode.bean.ApplyForMonitorStartupBean;
import com.jsecode.bean.IApplyForMonitorBean;
import com.jsecode.bean.TerminalBean;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.cmd.up.req.CmdUpExgMsgApplyForMonitorEnd;
import com.jsecode.cmd.up.req.CmdUpExgMsgApplyForMonitorStartup;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.GlobalVar;
import com.jsecode.utils.KKTool;

/**
 * 发送申请/取消交换指定车辆定位请求信息
 */
public class ThreadSendApplyForMonitor extends AbstractThreadSendData<IApplyForMonitorBean>{
	
	public ThreadSendApplyForMonitor(IGW809 gw809) {
		super(gw809, ThreadSendApplyForMonitor.class.getName());
	}

	public void run() {
		IApplyForMonitorBean applyForMonitorBean = null;
		while (!isInterrupted()) {
			while ((applyForMonitorBean = queue.poll()) != null) {
				if (applyForMonitorBean instanceof ApplyForMonitorStartupBean) {
					sendApplyForMonitorStartup((ApplyForMonitorStartupBean)applyForMonitorBean);
				} else if (applyForMonitorBean instanceof ApplyForMonitorEndBean) {
					sendApplyForMonitorEnd((ApplyForMonitorEndBean)applyForMonitorBean);
				}
			}
			waitNewData();
		}
	}
	
	private boolean sendApplyForMonitorStartup(ApplyForMonitorStartupBean startup) {
		TerminalBean terminal = GlobalVar.terminalMap.get(startup.getVehicleNo());
		if (terminal != null) {
			CmdUpExgMsgApplyForMonitorStartup cmdUpExgMsgApplyForMonitorStartup = new CmdUpExgMsgApplyForMonitorStartup();
			cmdUpExgMsgApplyForMonitorStartup.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgApplyForMonitorStartup.setSubMsgId(Const.UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP);
			byte[] vehicleNoBytes = KKTool.toFixedLenGBKBytes(startup.getVehicleNo(), 21);
			cmdUpExgMsgApplyForMonitorStartup.setVehicleNo(vehicleNoBytes);
			cmdUpExgMsgApplyForMonitorStartup.setVehicleColor(terminal.getHostPlateColor());
			cmdUpExgMsgApplyForMonitorStartup.setStartTime(KKTool.getUTC(startup.getStartTime()));
			cmdUpExgMsgApplyForMonitorStartup.setEndTime(KKTool.getUTC(startup.getEndTime()));
			
			IMainSubLink mLink = getMainLink(true);
			return mLink.sendData(cmdUpExgMsgApplyForMonitorStartup.getSendBuffer()) != null;
		}
		return false;
	}

	private boolean sendApplyForMonitorEnd(ApplyForMonitorEndBean end) {
		TerminalBean terminal = GlobalVar.terminalMap.get(end.getVehicleNo());
		if (terminal != null) {
			CmdUpExgMsgApplyForMonitorEnd cmdUpExgMsgApplyForMonitorEnd = new CmdUpExgMsgApplyForMonitorEnd();
			cmdUpExgMsgApplyForMonitorEnd.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgApplyForMonitorEnd.setSubMsgId(Const.UP_EXG_MSG_APPLY_FOR_MONITOR_END);
			byte[] vehicleNoBytes = KKTool.toFixedLenGBKBytes(end.getVehicleNo(), 21);
			cmdUpExgMsgApplyForMonitorEnd.setVehicleNo(vehicleNoBytes);
			cmdUpExgMsgApplyForMonitorEnd.setVehicleColor(terminal.getHostPlateColor());
			
			IMainSubLink mLink = getMainLink(true);
			return mLink.sendData(cmdUpExgMsgApplyForMonitorEnd.getSendBuffer()) != null;
		}
		return false;
	}
	
}

package com.jsecode.biz;

import com.jsecode.IGW809;
import com.jsecode.bean.TerminalCmdBean;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;

/**
 * 处理数据库中Terminal_Command表中相应数据的处理结果
 * @author 	Jadic
 * @created 2014-4-2
 */
public class ThreadDisposeCmdFromDB extends AbstractThreadDisposeDataFromQueue<TerminalCmdBean> {

	private IGW809 gw809;
	
	public ThreadDisposeCmdFromDB(IGW809 gw809) {
		super(ThreadDisposeCmdFromDB.class.getName());
		this.gw809 = gw809;
	}

	public void run() {
		TerminalCmdBean cmd = null;
		while (!isInterrupted()) {
			while ((cmd = getQueuePollData()) != null) {
				disposeCmd(cmd);
			}
			waitNewData();
		}
	}
	
	private void disposeCmd(TerminalCmdBean cmd) {
		if (cmd == null) {
			return ;
		}
		
		short cmdFlagId = (short)cmd.getCmdFlagId();
		switch (cmd.getCmdFlagId()) {

		// 平台间信息交互消息
		case Const.DOWN_PLATFORM_MSG_POST_QUERY_REQ:// 平台查岗请求
			dealCmdDownPlatformMsgPostQueryReq(cmd);
			break;

		// 报警信息交互消息
		case Const.DOWN_WARN_MSG_URGE_TODO_REQ:// 报警督办请求
			dealCmdDownWarnMsgUrgeTodoReq(cmd);
			break;

		// 车辆监管消息
		case Const.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ:// 车辆单向监听请求
			dealCmdDownCtrlMsgMonitorVehicleReq(cmd);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_PHOTO_REQ:// 车辆拍照请求
			dealCmdDownCtrlMsgTakePhotoReq(cmd);
			break;
		case Const.DOWN_CTRL_MSG_TEXT_INFO:// 下发车辆报文请求
			dealCmdDownCtrlMsgTextInfo(cmd);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ:// 上报车辆行驶记录请求
			dealCmdDownCtrlMsgTakeTravelReq(cmd);
			break;
		case Const.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ:// 车辆应急接入监管平台请求
			dealCmdDownCtrlMsgEmergencyMonitoringReq(cmd);
			break;
		default:
			break;
		}
	}

	// 平台查岗请求
	private void dealCmdDownPlatformMsgPostQueryReq(TerminalCmdBean cmd) {
		
	}
	
	// 报警督办请求
	private void dealCmdDownWarnMsgUrgeTodoReq(TerminalCmdBean cmd) {
		
	}
	
	// 车辆单向监听请求
	private void dealCmdDownCtrlMsgMonitorVehicleReq(TerminalCmdBean cmd) {
		
	}
	
	// 车辆拍照请求
	private void dealCmdDownCtrlMsgTakePhotoReq(TerminalCmdBean cmd) {
		
	}
	
	// 下发车辆报文请求
	private void dealCmdDownCtrlMsgTextInfo(TerminalCmdBean cmd) {
		
	}
	
	// 上报车辆行驶记录请求
	private void dealCmdDownCtrlMsgTakeTravelReq(TerminalCmdBean cmd) {
		
	}
	
	// 车辆应急接入监管平台请求
	private void dealCmdDownCtrlMsgEmergencyMonitoringReq(TerminalCmdBean cmd) {
		
	}
	
	private IMainSubLink getMainLink(boolean isSwitchedIfNotConnected) {
		return gw809.getMainLink(isSwitchedIfNotConnected);
	}
	
	private IMainSubLink getSubLink(boolean isSwitchedIfNotConnected) {
		return gw809.getSubLink(isSwitchedIfNotConnected);
	}
}

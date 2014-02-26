package com.jsecode;

import java.util.Date;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.jsecode.cmd.down.req.CmdDownBaseMsgVehicleAdded;
import com.jsecode.cmd.down.req.CmdDownCloselinkInform;
import com.jsecode.cmd.down.req.CmdDownConnectReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgEmergencyMonitoringReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgMonitorVehicleReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTakePhotoReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTakeTravelReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTextInfo;
import com.jsecode.cmd.down.req.CmdDownDisconnectInform;
import com.jsecode.cmd.down.req.CmdDownDisconnectReq;
import com.jsecode.cmd.down.req.CmdDownExgMsgApplyForMonitorEndAck;
import com.jsecode.cmd.down.req.CmdDownExgMsgApplyForMonitorStartupAck;
import com.jsecode.cmd.down.req.CmdDownExgMsgApplyHisgnssdataAck;
import com.jsecode.cmd.down.req.CmdDownExgMsgCarInfo;
import com.jsecode.cmd.down.req.CmdDownExgMsgCarLocation;
import com.jsecode.cmd.down.req.CmdDownExgMsgHistoryArcossarea;
import com.jsecode.cmd.down.req.CmdDownExgMsgReportDriverInfo;
import com.jsecode.cmd.down.req.CmdDownExgMsgReturnEnd;
import com.jsecode.cmd.down.req.CmdDownExgMsgReturnStartup;
import com.jsecode.cmd.down.req.CmdDownExgMsgTakeEwaybillReq;
import com.jsecode.cmd.down.req.CmdDownLinktestReq;
import com.jsecode.cmd.down.req.CmdDownPlatformMsgInfoReq;
import com.jsecode.cmd.down.req.CmdDownPlatformMsgPostQueryReq;
import com.jsecode.cmd.down.req.CmdDownTotalRecvBackMsgReq;
import com.jsecode.cmd.down.req.CmdDownWarnMsgExgInform;
import com.jsecode.cmd.down.req.CmdDownWarnMsgInformTips;
import com.jsecode.cmd.down.req.CmdDownWarnMsgUrgeTodoReq;
import com.jsecode.cmd.down.resp.CmdDownConnectResp;
import com.jsecode.cmd.down.resp.CmdDownDisconnectResp;
import com.jsecode.cmd.down.resp.CmdDownLinktestResp;
import com.jsecode.cmd.up.req.CmdUpDisconnectInform;
import com.jsecode.cmd.up.req.CmdUpExgMsgReportDriverInfoAck;
import com.jsecode.cmd.up.resp.CmdUpConnectResp;
import com.jsecode.cmd.up.resp.CmdUpDisconnectResp;
import com.jsecode.cmd.up.resp.CmdUpLinkTestResp;
import com.jsecode.link.IMainSubLink;
import com.jsecode.tcp.client.TcpClient;
import com.jsecode.tcp.server.TcpServer;
import com.jsecode.tcp.telnet.TelnetServer;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;

public class GW809Server implements IGW809, ITelnetServer {

	private TcpClient mainLink;// 主链路
	private TcpServer subLink;// 从链路
	private TelnetServer telnetServer;

	private SysParams sysParams;

	public static void main(String[] args) {
		new GW809Server();
	}

	public GW809Server() {
		sysParams = SysParams.getInstance();
		this.startMainLink();
		this.startSubLink();
		this.startTelnetServer();
	}

	private void startMainLink() {
		mainLink = new TcpClient(sysParams.getMainLinkIp(),	sysParams.getMainLinkPort(), this);
		mainLink.start();
	}
	
	private void startSubLink() {
		subLink = new TcpServer(sysParams.getSubLinkPort(), this);
		subLink.start();
	}
	
	private void startTelnetServer() {
		telnetServer = new TelnetServer(sysParams.getTelnetPort(), this);
		telnetServer.start();
	}
	
	@Override
	public void dispose809Data(ChannelBuffer channelBuffer, IMainSubLink link) {
		if (channelBuffer == null || channelBuffer.readableBytes() < Const.CMD_MIN_SIZE || link == null) {
			return;
		}

		short cmdFlag = channelBuffer.getShort(channelBuffer.readerIndex() + 9);
		switch (cmdFlag) {
		case Const.UP_CONNECT_RSP: // 主链路登录应答消息 主链路
			dealCmdUpConnectRep(channelBuffer, link);
			break;
		case Const.UP_DISCONNECT_RSP: // 主链路注销应答消息 主链路
			dealCmdUpDisconnectRsp(channelBuffer, link);
			break;
		case Const.UP_LINKTEST_RSP: // 主链路连接保持应答消息 主链路
			dealCmdUpLinkTestRsp(channelBuffer, link);
			break;
		case Const.DOWN_CONNECT_REQ: // 从链路连接请求消息 从链路
			dealCmdDownConnectReq(channelBuffer, link);
			break;
		case Const.DOWN_DISCONNECT_REQ: // 从链路注销请求消息 从链路
			dealCmdDownDisconnectReq(channelBuffer, link);
			break;
		case Const.DOWN_LINKTEST_REQ: // 从链路连接保持请求消息 从链路
			dealCmdDownLinkTestReq(channelBuffer, link);
			break;
		case Const.DOWN_DISCONNECT_INFORM: // 从链路断开通知消息 从链路
			dealCmdDownDisconnectInform(channelBuffer, link);
			break;
		case Const.DOWN_CLOSELINK_INFORM: // 上级平台主动关闭链路通知消息 主链路
			dealCmdDownCloseLinkInform(channelBuffer, link);
			break;
		case Const.DOWN_TOTAL_RECV_BACK_MSG:// 接收定位信息数量通知消息 从链路
			dealCmdDownTotalRecvBackMsg(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG: // 从链路动态信息交换消息 从链路
		case Const.DOWN_PLATFORM_MSG: // 从链路平台间信息交互消息 从链路
		case Const.DOWN_WARN_MSG: // 从链路报警信息交互消息 从链路
		case Const.DOWN_CTRL_MSG: // 从链路车辆监管消息 从链路
		case Const.DOWN_BASE_MSG: // 从链路静态信息交换消息 从链路
			short subBizCmdFlag = 0;
			dispose809SubBizData(subBizCmdFlag, channelBuffer, link);
			break;
		default:
			KKLog.warn("Unknown command flag:" + KKTool.byteArrayToHexStr(KKTool.short2BytesBigEndian(cmdFlag)));
			break;
		}
	}

	// 主链路登录应答消息
	private void dealCmdUpConnectRep(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdUpConnectResp cmdUpConnectResp = new CmdUpConnectResp();
		if (cmdUpConnectResp.disposeData(channelBuffer)) {
			byte ret = cmdUpConnectResp.getRet();
			int verifyCode = cmdUpConnectResp.getVerifyCode();
			link.setVerifyCode(verifyCode);
			KKLog.info("main link connect response，ret:" + KKTool.getLoginRespRet(ret) + "，verifyCode:" + verifyCode);
		}
	}

	// 主链路注销应答消息
	private void dealCmdUpDisconnectRsp(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdUpDisconnectResp cmdUpDisconnectResp = new CmdUpDisconnectResp();
		if (cmdUpDisconnectResp.disposeData(channelBuffer)) {
			//
			IMainSubLink mLink = getMainLink(false);
			mLink.closeLink();		
			
			//send disconnect inform from sub link
			IMainSubLink sLink = getMainLink(false);
			CmdUpDisconnectInform cmdUpDisconnectInform = new CmdUpDisconnectInform();
			cmdUpDisconnectInform.setMsgFlagId(Const.UP_DISCONNECT_INFORM);
			cmdUpDisconnectInform.setErrorCode(Const.UP_DISCONNECT_INFORM_ERR_DISCONNECT);
			sLink.sendData(cmdUpDisconnectInform.getSendBuffer());
		}
	}

	// 主链路连接保持应答消息
	private void dealCmdUpLinkTestRsp(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdUpLinkTestResp cmdUpLinkTestResp = new CmdUpLinkTestResp();
		if (cmdUpLinkTestResp.disposeData(channelBuffer)) {
			KKLog.debug("main link response");
		}
	}

	// 从链路连接请求消息
	private void dealCmdDownConnectReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownConnectReq cmdDownConnectReq = new CmdDownConnectReq();
		if (cmdDownConnectReq.disposeData(channelBuffer)) {
			IMainSubLink sLink = getSubLink(false);
			IMainSubLink mLink = getMainLink(false);
			
			byte ret = Const.DOWN_CONNECT_RESP_RET_OK;
			if (mLink.getVerifyCode() != cmdDownConnectReq.getVerifyCode()) {
				ret = Const.DOWN_CONNECT_RESP_RET_VERIFY_CODE_ERR;
			}
			
			CmdDownConnectResp cmdDownConnectResp = new CmdDownConnectResp();
			cmdDownConnectResp.setMsgFlagId(Const.UP_CONNECT_RSP);
			cmdDownConnectResp.setResult(ret);
			sLink.sendData(cmdDownConnectResp.getSendBuffer());
		}
	}

	// 从链路注销请求消息
	private void dealCmdDownDisconnectReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownDisconnectReq cmdDownDisconnectReq = new CmdDownDisconnectReq();
		if (cmdDownDisconnectReq.disposeData(channelBuffer)) {
			CmdDownDisconnectResp cmdDownDisconnectResp = new CmdDownDisconnectResp();
			cmdDownDisconnectResp.setMsgFlagId(Const.DOWN_DISCONNECT_RSP);
			IMainSubLink sLink = getSubLink(true);
			sLink.sendData(cmdDownDisconnectResp.getSendBuffer());
		}
	}

	// 从链路连接保持请求消息
	private void dealCmdDownLinkTestReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownLinktestReq cmdDownLinktestReq = new CmdDownLinktestReq();
		if (cmdDownLinktestReq.disposeData(channelBuffer)) {
			CmdDownLinktestResp cmdDownLinktestResp = new CmdDownLinktestResp();
			cmdDownLinktestReq.setMsgFlagId(Const.DOWN_LINKTEST_RSP);
			IMainSubLink sLink = getSubLink(true);
			sLink.sendData(cmdDownLinktestResp.getSendBuffer());
		}
	}

	// 从链路断开通知消息
	private void dealCmdDownDisconnectInform(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownDisconnectInform cmdDisconnectInform = new CmdDownDisconnectInform();
		if (cmdDisconnectInform.disposeData(channelBuffer)) {
			KKLog.info("sub link disconnect inform, error_code:" + KKTool.byteToHexStr(cmdDisconnectInform.getErrorCode()));
		}
	}

	// 上级平台主动关闭链路通知消息
	private void dealCmdDownCloseLinkInform(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCloselinkInform cmdDownCloselinkInform = new CmdDownCloselinkInform();
		if (cmdDownCloselinkInform.disposeData(channelBuffer)) {
			KKLog.info("superior platform notice to close link, reason code:" + KKTool.byteToHexStr(cmdDownCloselinkInform.getReasonCode()));
		}
	}

	// 接收定位信息数量通知消息
	private void dealCmdDownTotalRecvBackMsg(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownTotalRecvBackMsgReq cmdDownTotalRecvBackMsgReq = new CmdDownTotalRecvBackMsgReq();
		if (cmdDownTotalRecvBackMsgReq.disposeData(channelBuffer)) {
			int recvCount = cmdDownTotalRecvBackMsgReq.getDynamicInfoTotal();
			Date sDate = KKTool.getDateFromUTC(cmdDownTotalRecvBackMsgReq.getStartTime());
			Date eDate = KKTool.getDateFromUTC(cmdDownTotalRecvBackMsgReq.getEndTime());
			KKLog.info("superior platform notice total recv msg info,count:" + recvCount 
																 + ",sTime:" + KKTool.getFormatDateTime(sDate)
																 + ",eTime:" + KKTool.getFormatDateTime(eDate));
		}
	}

	/**
	 * 子业务分拣处理
	 * 
	 * @param subBizCmdFlag	子业务命令字
	 * @param channelBuffer
	 * @param link
	 */
	private void dispose809SubBizData(short subBizCmdFlag, ChannelBuffer channelBuffer, IMainSubLink link) {
		switch (subBizCmdFlag) {
		// 车辆动态信息交换消息
		case Const.DOWN_EXG_MSG_CAR_LOCATION://交换车辆定位信息
			dealCmdDownExgMsgCarLocation(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_HISTORY_ARCOSSAREA://车辆定位信息交换补发
			dealCmdDownExgMsgHistoryArcossarea(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_CAR_INFO://交换车辆静态信息
			dealCmdDownExgMsgCarInfo(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_RETURN_STARTUP://启动车辆定位信息交换请求
			dealCmdDownExgMsgReturnStartup(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_RETURN_END://结束车辆定位信息交换请求
			dealCmdDownExgMsgReturnEnd(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK://申请交换指定车辆定位信息应答
			dealCmdDownExgMsgApplyForMonitorStartupAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK://取消申请交换指定车辆定位信息应答
			dealCmdDownExgMsgApplyForMonitorEndAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK://补发车辆定位信息应答
			dealCmdDownExgMsgApplyHisGnssDataAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_REPORT_DRIVER_INFO://上报驾驶员身份信息请求
			dealCmdDownExgMsgReportDriverInfo(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_TAKE_EWAYBILL_REQ://上报车辆电子运单请求
			dealCmdDownExgMsgTakeEWaybillReq(channelBuffer, link);
			break;

		// 平台间信息交互消息
		case Const.DOWN_PLATFORM_MSG_POST_QUERY_REQ://平台查岗请求
			dealCmdDownPlatformMsgPostQueryReq(channelBuffer, link);
			break;
		case Const.DOWN_PLATFORM_MSG_INFO_REQ://下发平台间报文请求
			dealCmdDownPlatformMsgInfoReq(channelBuffer, link);
			break;

		// 报警信息交互消息
		case Const.DOWN_WARN_MSG_URGE_TODO_REQ://报警督办请求
			dealCmdDownWarnMsgUrgeTodoReq(channelBuffer, link);
			break;
		case Const.DOWN_WARN_MSG_INFORM_TIPS://报警预警
			dealCmdDownWarnMsgInformTips(channelBuffer, link);
			break;
		case Const.DOWN_WARN_MSG_EXG_INFORM://实时交换报警信息
			dealCmdDownWarnMsgExgInform(channelBuffer, link);
			break;

		// 车辆监管消息
		case Const.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ://车辆单向监听请求
			dealCmdDownCtrlMsgMonitorVehicleReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_PHOTO_REQ://车辆拍照请求
			dealCmdDownCtrlMsgTakePhotoReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TEXT_INFO://下发车辆报文请求
			dealCmdDownCtrlMsgTextInfo(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ://上报车辆行驶记录请求
			dealCmdDownCtrlMsgTakeTravelReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ://车辆应急接入监管平台请求
			dealCmdDownCtrlMsgEmergencyMonitoringReq(channelBuffer, link);
			break;

		// 静态信息交换消息
		case Const.DOWN_BASE_MSG_VEHICLE_ADDED://补报车辆静态信息请求
			dealCmdDownBaseMsgVehicleAdded(channelBuffer, link);
			break;
		default:
			KKLog.warn("Unknown sub biz command flag:" + KKTool.byteArrayToHexStr(KKTool.short2BytesBigEndian(subBizCmdFlag)));
			break;
		}
	}

	//交换车辆定位信息
	private void dealCmdDownExgMsgCarLocation(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgCarLocation cmdDownExgMsgCarLocation = new CmdDownExgMsgCarLocation();
		if (cmdDownExgMsgCarLocation.disposeData(channelBuffer)) {
			
		}
	}

	//车辆定位信息交换补发
	private void dealCmdDownExgMsgHistoryArcossarea(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgHistoryArcossarea cmdDownExgMsgHistoryArcossarea = new CmdDownExgMsgHistoryArcossarea();
		if (cmdDownExgMsgHistoryArcossarea.disposeData(channelBuffer)) {
			
		}
	}

	//交换车辆静态信息
	private void dealCmdDownExgMsgCarInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgCarInfo cmdDownExgMsgCarInfo = new CmdDownExgMsgCarInfo();
		if (cmdDownExgMsgCarInfo.disposeData(channelBuffer)) {
			
		}
	}

	//启动车辆定位信息交换请求
	private void dealCmdDownExgMsgReturnStartup(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReturnStartup cmdDownExgMsgReturnStartup = new CmdDownExgMsgReturnStartup();
		if (cmdDownExgMsgReturnStartup.disposeData(channelBuffer)) {
			
		}
	}

	//结束车辆定位信息交换请求
	private void dealCmdDownExgMsgReturnEnd(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReturnEnd cmdDownExgMsgReturnEnd = new CmdDownExgMsgReturnEnd();
		if (cmdDownExgMsgReturnEnd.disposeData(channelBuffer)) {
			
		}
	}

	//申请交换指定车辆定位信息应答
	private void dealCmdDownExgMsgApplyForMonitorStartupAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyForMonitorStartupAck cmdDownExgMsgApplyForMonitorStartupAc = new CmdDownExgMsgApplyForMonitorStartupAck();
		if (cmdDownExgMsgApplyForMonitorStartupAc.disposeData(channelBuffer)) {
			
		}
	}

	//取消申请交换指定车辆定位信息应答
	private void dealCmdDownExgMsgApplyForMonitorEndAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyForMonitorEndAck cmdDownExgMsgApplyForMonitorEndAck = new CmdDownExgMsgApplyForMonitorEndAck();
		if (cmdDownExgMsgApplyForMonitorEndAck.disposeData(channelBuffer)) {
			
		}
	}

	//补发车辆定位信息应答
	private void dealCmdDownExgMsgApplyHisGnssDataAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyHisgnssdataAck cmdDownExgMsgApplyHisgnssdataAck = new CmdDownExgMsgApplyHisgnssdataAck();
		if (cmdDownExgMsgApplyHisgnssdataAck.disposeData(channelBuffer)) {
			
		}
	}

	//上报驾驶员身份信息请求
	private void dealCmdDownExgMsgReportDriverInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReportDriverInfo cmdDownExgMsgReportDriverInfo = new CmdDownExgMsgReportDriverInfo();
		if (cmdDownExgMsgReportDriverInfo.disposeData(channelBuffer)) {
			CmdUpExgMsgReportDriverInfoAck cmdUpExgMsgReportDriverInfoAck = new CmdUpExgMsgReportDriverInfoAck();
			
			//fill cmd
			
			IMainSubLink mainLink = getMainLink(true);
			if (mainLink.isChannelConnected()) {
				mainLink.sendData(cmdUpExgMsgReportDriverInfoAck.getSendBuffer());
			}
		}
	}

	//上报车辆电子运单请求
	private void dealCmdDownExgMsgTakeEWaybillReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgTakeEwaybillReq cmdDownExgMsgTakeEWaybillReq = new CmdDownExgMsgTakeEwaybillReq();
		if (cmdDownExgMsgTakeEWaybillReq.disposeData(channelBuffer)) {
			
		}
	}

	//平台查岗请求
	private void dealCmdDownPlatformMsgPostQueryReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownPlatformMsgPostQueryReq cmdDownPlatformMsgPostQueryReq = new CmdDownPlatformMsgPostQueryReq();
		if (cmdDownPlatformMsgPostQueryReq.disposeData(channelBuffer)) {
			
		}
	}

	//下发平台间报文请求
	private void dealCmdDownPlatformMsgInfoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownPlatformMsgInfoReq cmdDownPlatformMsgInfoReq = new CmdDownPlatformMsgInfoReq();
		if (cmdDownPlatformMsgInfoReq.disposeData(channelBuffer)) {
			
		}
	}

	//报警督办请求
	private void dealCmdDownWarnMsgUrgeTodoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgUrgeTodoReq cmdDownWarnMsgUrgeTodoReq = new CmdDownWarnMsgUrgeTodoReq();
		if (cmdDownWarnMsgUrgeTodoReq.disposeData(channelBuffer)) {
			
		}
	}

	//报警预警
	private void dealCmdDownWarnMsgInformTips(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgInformTips cmdDownWarnMsgInformTips = new CmdDownWarnMsgInformTips();
		if (cmdDownWarnMsgInformTips.disposeData(channelBuffer)) {
			
		}
	}

	//实时交换报警信息
	private void dealCmdDownWarnMsgExgInform(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgExgInform cmdDownWarnMsgExgInform = new CmdDownWarnMsgExgInform();
		if (cmdDownWarnMsgExgInform.disposeData(channelBuffer)) {
			
		}
	}

	//车辆单向监听请求
	private void dealCmdDownCtrlMsgMonitorVehicleReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgMonitorVehicleReq cmdDownCtrlMsgMonitorVehicleReq = new CmdDownCtrlMsgMonitorVehicleReq();
		if (cmdDownCtrlMsgMonitorVehicleReq.disposeData(channelBuffer)) {
			
		}
	}

	//车辆拍照请求
	private void dealCmdDownCtrlMsgTakePhotoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTakePhotoReq cmdDownCtrlMsgTakePhotoReq = new CmdDownCtrlMsgTakePhotoReq();
		if (cmdDownCtrlMsgTakePhotoReq.disposeData(channelBuffer)) {
			
		}
	}

	//下发车辆报文请求
	private void dealCmdDownCtrlMsgTextInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTextInfo cmdDownCtrlMsgTextInfo = new CmdDownCtrlMsgTextInfo();
		if (cmdDownCtrlMsgTextInfo.disposeData(channelBuffer)) {
			
		}
	}

	//上报车辆行驶记录请求
	private void dealCmdDownCtrlMsgTakeTravelReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTakeTravelReq cmdDownCtrlMsgTakeTravelReq = new CmdDownCtrlMsgTakeTravelReq();
		if (cmdDownCtrlMsgTakeTravelReq.disposeData(channelBuffer)) {
			
		}
	}

	//车辆应急接入监管平台请求
	private void dealCmdDownCtrlMsgEmergencyMonitoringReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgEmergencyMonitoringReq cmdDownCtrlMsgEmergencyMonitoringReq = new CmdDownCtrlMsgEmergencyMonitoringReq();
		if (cmdDownCtrlMsgEmergencyMonitoringReq.disposeData(channelBuffer)) {
			
		}
	}

	//补报车辆静态信息请求
	private void dealCmdDownBaseMsgVehicleAdded(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownBaseMsgVehicleAdded cmdDownBaseMsgVehicleAdded = new CmdDownBaseMsgVehicleAdded();
		if (cmdDownBaseMsgVehicleAdded.disposeData(channelBuffer)) {
			
		}
	}

	/**
	 * 获取主链路
	 * @param isSwitchedIfNotConnected 如果主链接已断开，是否返回从链路
	 * @return
	 */
	public IMainSubLink getMainLink(boolean isSwitchedIfNotConnected) {
		return isSwitchedIfNotConnected && !mainLink.isChannelConnected() ? subLink : mainLink; 
	}
	
	/**
	 * 获取从链路
	 * @param isSwitchedIfNotConnected 如果从链路已断开，是否返回主链路
	 * @return 
	 */
	public IMainSubLink getSubLink(boolean isSwitchedIfNotConnected) {
		return isSwitchedIfNotConnected && !subLink.isChannelConnected() ? mainLink : subLink;
	}
	
	@Override
	public void doOnTelnetConnection(Channel channel) {
		if (channel != null) {
			String response = "Welcome to 809 server!\r\n"
							+ "Input 'help' for all supported commands.\r\n\r\n"
							+ "809$ ";
			channel.write(response);
		}
	}

	@Override
	public void disposeTelnetData(Channel channel, String request) {
		if (channel != null) {
			if (!KKTool.isStrNullOrBlank(request)) {
				boolean isQuit = false;
				String response = Const.EMPTY_STR;
				
				if (request.equals(Const.TELNET_CMD_QUIT)) {
					response = "You close the connection, have a good day, bye\r\n\r\n";
					isQuit = true;
				} else if (request.equals(Const.TELNET_CMD_HELP)) {
					response = KKTool.getTelnetHelpContent() + "\r\n";
				} else if (request.equals(Const.TELNET_CMD_START_MAINLINK)) {
					if (mainLink.isStopped()) {
						response = "starting main link\r\n";
						this.startMainLinkManually();
						response = response + "main link started\r\n\r\n";
					} else {
						response = "main link has started\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_END_MAINLINK)) {
					if (mainLink.isStopped()) {
						response = "main link has ended\r\n\r\n";
					} else {
						response = "ending main link\r\n";
						this.stopMainLinkManually();
						response = response + "main link ended\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_START_SUBLINK)) {
					if (subLink.isStopped()) {
						response = "starting sub lnik\r\n";
						this.startSubLinkManually();
						response = response + "main link ended\r\n\r\n";
					} else {
						response = "sub link has started\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_END_SUBLINK)) {
					if (subLink.isStopped()) {
						response = "sub link has ended\r\n\r\n";
					} else {
						response = "ending sublink\r\n";
						this.stopSubLinkManually();
						response = response + "sub link ended\r\n\r\n";
					}
				} else {
					response = "Your input command[" + request + "] is not in command list.\r\n" 
							 + "Input 'help' for all supported commands\r\n\r\n";
				}
				response = response + "809$ ";
				ChannelFuture future = channel.write(response);
				if (isQuit) {
					future.addListener(ChannelFutureListener.CLOSE);
				}
			} else {
				channel.write("Your input is empty, please input valid command mentioned above\r\n\r\n");
			}
		}
	}

	/**
	 * 手动关闭主链路
	 */
	private void stopMainLinkManually() {
		if (!mainLink.isStopped()) {
			mainLink.closeLink();
		}
	}
	
	/**
	 * 手动启动主链路
	 */
	private void startMainLinkManually() {
		if (mainLink.isStopped()) {
			mainLink.start();
		}
	}
	
	/**
	 * 手动关闭从链路
	 */
	private void stopSubLinkManually() {
		if (!subLink.isStopped()) {
			subLink.closeLink();
		}
	}
	
	/**
	 * 手动启动从链路
	 */
	private void startSubLinkManually() {
		if (subLink.isStopped()) {
			subLink.start();
		}
	}
}

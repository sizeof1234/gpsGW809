/**
 * @author 	Jadic
 * @created 2014-2-27
 */
package com.jsecode.biz;

import java.util.Date;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.IGW809;
import com.jsecode.bean.DriverBean;
import com.jsecode.cmd.bean.GpsCmdBean;
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
import com.jsecode.cmd.up.req.CmdUpBaseMsgVehicleAddedAck;
import com.jsecode.cmd.up.req.CmdUpDisconnectInform;
import com.jsecode.cmd.up.req.CmdUpExgMsgReportDriverInfoAck;
import com.jsecode.cmd.up.req.CmdUpExgMsgReturnEndAck;
import com.jsecode.cmd.up.req.CmdUpExgMsgReturnStartupAck;
import com.jsecode.cmd.up.req.CmdUpExgMsgTakeEWayBillAck;
import com.jsecode.cmd.up.resp.CmdUpConnectResp;
import com.jsecode.cmd.up.resp.CmdUpDisconnectResp;
import com.jsecode.cmd.up.resp.CmdUpLinkTestResp;
import com.jsecode.db.DBOper;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.GlobalVar;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

/**
 * 所有809命令处理
 */
public final class GW809CmdDisposer {

	private IGW809 gw809;
	private DBOper dbOper;

	public GW809CmdDisposer(IGW809 gw809) {
		this.gw809 = gw809;
		this.dbOper = DBOper.getDBOper();
	}

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
			short subBizCmdFlag = channelBuffer.getShort(channelBuffer.readerIndex() + 9 + 2 + 4 + 3 + 1 + 4 + 22);
			if (cmdFlag == Const.DOWN_PLATFORM_MSG) {
				subBizCmdFlag = channelBuffer.getShort(channelBuffer.readerIndex() + 9 + 2 + 4 + 3 + 1 + 4);
			}
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
			IMainSubLink mLink = this.gw809.getMainLink(false);
			mLink.closeLink();

			// send disconnect inform from sub link
			CmdUpDisconnectInform cmdUpDisconnectInform = new CmdUpDisconnectInform();
			cmdUpDisconnectInform.setMsgFlagId(Const.UP_DISCONNECT_INFORM);
			cmdUpDisconnectInform.setErrorCode(Const.UP_DISCONNECT_INFORM_ERR_DISCONNECT);
			sendFromSubLink(false, cmdUpDisconnectInform.getSendBuffer());
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
			IMainSubLink mLink = this.gw809.getMainLink(false);

			byte ret = Const.DOWN_CONNECT_RESP_RET_OK;
			if (mLink.getVerifyCode() != cmdDownConnectReq.getVerifyCode()) {
				ret = Const.DOWN_CONNECT_RESP_RET_VERIFY_CODE_ERR;
			}

			CmdDownConnectResp cmdDownConnectResp = new CmdDownConnectResp();
			cmdDownConnectResp.setMsgFlagId(Const.UP_CONNECT_RSP);
			cmdDownConnectResp.setResult(ret);
			sendFromSubLink(false, cmdDownConnectResp.getSendBuffer());
		}
	}

	// 从链路注销请求消息
	private void dealCmdDownDisconnectReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownDisconnectReq cmdDownDisconnectReq = new CmdDownDisconnectReq();
		if (cmdDownDisconnectReq.disposeData(channelBuffer)) {
			CmdDownDisconnectResp cmdDownDisconnectResp = new CmdDownDisconnectResp();
			cmdDownDisconnectResp.setMsgFlagId(Const.DOWN_DISCONNECT_RSP);
			sendFromSubLink(true, cmdDownDisconnectResp.getSendBuffer());
		}
	}

	// 从链路连接保持请求消息
	private void dealCmdDownLinkTestReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownLinktestReq cmdDownLinktestReq = new CmdDownLinktestReq();
		if (cmdDownLinktestReq.disposeData(channelBuffer)) {
			CmdDownLinktestResp cmdDownLinktestResp = new CmdDownLinktestResp();
			cmdDownLinktestReq.setMsgFlagId(Const.DOWN_LINKTEST_RSP);
			sendFromSubLink(true, cmdDownLinktestResp.getSendBuffer());
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
			KKLog.info("superior platform notice to close link, reason code:"
					+ KKTool.byteToHexStr(cmdDownCloselinkInform.getReasonCode()));
		}
	}

	// 接收定位信息数量通知消息
	private void dealCmdDownTotalRecvBackMsg(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownTotalRecvBackMsgReq cmdDownTotalRecvBackMsgReq = new CmdDownTotalRecvBackMsgReq();
		if (cmdDownTotalRecvBackMsgReq.disposeData(channelBuffer)) {
			int recvCount = cmdDownTotalRecvBackMsgReq.getDynamicInfoTotal();
			Date sDate = KKTool.getDateFromUTC(cmdDownTotalRecvBackMsgReq.getStartTime());
			Date eDate = KKTool.getDateFromUTC(cmdDownTotalRecvBackMsgReq.getEndTime());
			KKLog.info("superior platform notice total recv msg info,count:" + recvCount + ",sTime:"
					+ KKTool.getFormatDateTime(sDate) + ",eTime:" + KKTool.getFormatDateTime(eDate));
		}
	}

	/**
	 * 子业务分拣处理
	 *
	 * @param subBizCmdFlag
	 *            子业务命令字
	 * @param channelBuffer
	 * @param link
	 */
	private void dispose809SubBizData(short subBizCmdFlag, ChannelBuffer channelBuffer, IMainSubLink link) {
		switch (subBizCmdFlag) {
		// 车辆动态信息交换消息
		case Const.DOWN_EXG_MSG_CAR_LOCATION:// 交换车辆定位信息
			dealCmdDownExgMsgCarLocation(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_HISTORY_ARCOSSAREA:// 车辆定位信息交换补发
			dealCmdDownExgMsgHistoryArcossarea(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_CAR_INFO:// 交换车辆静态信息
			dealCmdDownExgMsgCarInfo(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_RETURN_STARTUP:// 启动车辆定位信息交换请求
			dealCmdDownExgMsgReturnStartup(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_RETURN_END:// 结束车辆定位信息交换请求
			dealCmdDownExgMsgReturnEnd(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK:// 申请交换指定车辆定位信息应答
			dealCmdDownExgMsgApplyForMonitorStartupAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK:// 取消申请交换指定车辆定位信息应答
			dealCmdDownExgMsgApplyForMonitorEndAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK:// 补发车辆定位信息应答
			dealCmdDownExgMsgApplyHisGnssDataAck(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_REPORT_DRIVER_INFO:// 上报驾驶员身份信息请求
			dealCmdDownExgMsgReportDriverInfo(channelBuffer, link);
			break;
		case Const.DOWN_EXG_MSG_TAKE_EWAYBILL_REQ:// 上报车辆电子运单请求
			dealCmdDownExgMsgTakeEWaybillReq(channelBuffer, link);
			break;

		// 平台间信息交互消息
		case Const.DOWN_PLATFORM_MSG_POST_QUERY_REQ:// 平台查岗请求
			dealCmdDownPlatformMsgPostQueryReq(channelBuffer, link);
			break;
		case Const.DOWN_PLATFORM_MSG_INFO_REQ:// 下发平台间报文请求
			dealCmdDownPlatformMsgInfoReq(channelBuffer, link);
			break;
		// 报警信息交互消息
		case Const.DOWN_WARN_MSG_URGE_TODO_REQ:// 报警督办请求
			dealCmdDownWarnMsgUrgeTodoReq(channelBuffer, link);
			break;
		case Const.DOWN_WARN_MSG_INFORM_TIPS:// 报警预警
			dealCmdDownWarnMsgInformTips(channelBuffer, link);
			break;
		case Const.DOWN_WARN_MSG_EXG_INFORM:// 实时交换报警信息
			dealCmdDownWarnMsgExgInform(channelBuffer, link);
			break;

		// 车辆监管消息
		case Const.DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ:// 车辆单向监听请求
			dealCmdDownCtrlMsgMonitorVehicleReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_PHOTO_REQ:// 车辆拍照请求
			dealCmdDownCtrlMsgTakePhotoReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TEXT_INFO:// 下发车辆报文请求
			dealCmdDownCtrlMsgTextInfo(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_TAKE_TRAVEL_REQ:// 上报车辆行驶记录请求
			dealCmdDownCtrlMsgTakeTravelReq(channelBuffer, link);
			break;
		case Const.DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ:// 车辆应急接入监管平台请求
			dealCmdDownCtrlMsgEmergencyMonitoringReq(channelBuffer, link);
			break;

		// 静态信息交换消息
		case Const.DOWN_BASE_MSG_VEHICLE_ADDED:// 补报车辆静态信息请求
			dealCmdDownBaseMsgVehicleAdded(channelBuffer, link);
			break;
		default:
			KKLog.warn("Unknown sub biz command flag:" + KKTool.byteArrayToHexStr(KKTool.short2BytesBigEndian(subBizCmdFlag)));
			break;
		}
	}

	// 交换车辆定位信息
	private void dealCmdDownExgMsgCarLocation(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgCarLocation cmdDownExgMsgCarLocation = new CmdDownExgMsgCarLocation();
		if (cmdDownExgMsgCarLocation.disposeData(channelBuffer)) {
			GpsCmdBean gpsCmdBean = cmdDownExgMsgCarLocation.getGnssData();
			KKLog.info("recv gps data:[" + cmdDownExgMsgCarLocation.getVehicleNoStr()
					+ ",lon=" + gpsCmdBean.getLon() + ",lat=" + gpsCmdBean.getLat() + "]");

			// TODO the gps data to database or send to memcached
		}
	}

	// 车辆定位信息交换补发
	private void dealCmdDownExgMsgHistoryArcossarea(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgHistoryArcossarea cmdDownExgMsgHistoryArcossarea = new CmdDownExgMsgHistoryArcossarea();
		if (cmdDownExgMsgHistoryArcossarea.disposeData(channelBuffer)) {
			List<GpsCmdBean> gpsList = cmdDownExgMsgHistoryArcossarea.getGnssDataList();
			KKLog.info("recv gps his data:[" + cmdDownExgMsgHistoryArcossarea.getVehicleNoStr() + ", size:" + gpsList.size() + "]");

			// TODO save the gps data list to database or send to memcached
		}
	}

	// 交换车辆静态信息
	private void dealCmdDownExgMsgCarInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgCarInfo cmdDownExgMsgCarInfo = new CmdDownExgMsgCarInfo();
		if (cmdDownExgMsgCarInfo.disposeData(channelBuffer)) {
			KKLog.info("recv car info:" + KKTool.toGBKStr(cmdDownExgMsgCarInfo.getCarInfo()));

			// TODO save the car info to database or send to memcached
		}
	}

	// 启动车辆定位信息交换请求
	private void dealCmdDownExgMsgReturnStartup(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReturnStartup cmdDownExgMsgReturnStartup = new CmdDownExgMsgReturnStartup();
		if (cmdDownExgMsgReturnStartup.disposeData(channelBuffer)) {
			//TODO save this command to database or send to memcached

			CmdUpExgMsgReturnStartupAck cmdUpExgMsgReturnStartupAck = new CmdUpExgMsgReturnStartupAck();
			cmdUpExgMsgReturnStartupAck.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgReturnStartupAck.setSubMsgId(Const.UP_EXG_MSG_RETURN_STARTUP_ACK);
			cmdUpExgMsgReturnStartupAck.setVehicleNo(cmdDownExgMsgReturnStartup.getVehicleNo());
			cmdUpExgMsgReturnStartupAck.setVehicleColor(cmdDownExgMsgReturnStartup.getVehicleColor());

			sendFromMainLink(true, cmdUpExgMsgReturnStartupAck.getSendBuffer());

			KKLog.info("recv exg msg return startup:[" + cmdDownExgMsgReturnStartup.getVehicleNoStr()
					+ ",reason code:" + KKTool.byteToHexStr(cmdDownExgMsgReturnStartup.getReasonCode()) + "]");
		}
	}

	// 结束车辆定位信息交换请求
	private void dealCmdDownExgMsgReturnEnd(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReturnEnd cmdDownExgMsgReturnEnd = new CmdDownExgMsgReturnEnd();
		if (cmdDownExgMsgReturnEnd.disposeData(channelBuffer)) {
			//TODO save this command to database or send to memcached

			CmdUpExgMsgReturnEndAck cmdUpExgMsgReturnEndAck = new CmdUpExgMsgReturnEndAck();
			cmdUpExgMsgReturnEndAck.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgReturnEndAck.setSubMsgId(Const.UP_EXG_MSG_RETURN_STARTUP_ACK);
			cmdUpExgMsgReturnEndAck.setVehicleNo(cmdDownExgMsgReturnEnd.getVehicleNo());
			cmdUpExgMsgReturnEndAck.setVehicleColor(cmdDownExgMsgReturnEnd.getVehicleColor());

			sendFromMainLink(true, cmdUpExgMsgReturnEndAck.getSendBuffer());

			KKLog.info("recv exg msg return end:[" + cmdDownExgMsgReturnEnd.getVehicleNoStr()
					+ ",reason code:" + KKTool.byteToHexStr(cmdDownExgMsgReturnEnd.getReasonCode()) + "]");
		}
	}

	// 申请交换指定车辆定位信息应答
	private void dealCmdDownExgMsgApplyForMonitorStartupAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyForMonitorStartupAck cmdDownExgMsgApplyForMonitorStartupAck = new CmdDownExgMsgApplyForMonitorStartupAck();
		if (cmdDownExgMsgApplyForMonitorStartupAck.disposeData(channelBuffer)) {
			//TODO send command result to database or send to memcached

			KKLog.info("recv exg msg apply for monitor startup ack:[" + cmdDownExgMsgApplyForMonitorStartupAck.getVehicleNoStr()
					+ ",result code:" + KKTool.byteToHexStr(cmdDownExgMsgApplyForMonitorStartupAck.getResult()) + "]");
		}
	}

	// 取消申请交换指定车辆定位信息应答
	private void dealCmdDownExgMsgApplyForMonitorEndAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyForMonitorEndAck cmdDownExgMsgApplyForMonitorEndAck = new CmdDownExgMsgApplyForMonitorEndAck();
		if (cmdDownExgMsgApplyForMonitorEndAck.disposeData(channelBuffer)) {
			//TODO send command result to database or send to memcached

			KKLog.info("recv exg msg apply for monitor end ack:[" + cmdDownExgMsgApplyForMonitorEndAck.getVehicleNoStr()
					+ ",result code:" + KKTool.byteToHexStr(cmdDownExgMsgApplyForMonitorEndAck.getResult()) + "]");
		}
	}

	// 补发车辆定位信息应答
	private void dealCmdDownExgMsgApplyHisGnssDataAck(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgApplyHisgnssdataAck cmdDownExgMsgApplyHisgnssdataAck = new CmdDownExgMsgApplyHisgnssdataAck();
		if (cmdDownExgMsgApplyHisgnssdataAck.disposeData(channelBuffer)) {
			//TODO send command result to database or send to memcached

			KKLog.info("recv exg msg apply his gnss data ack:[" + cmdDownExgMsgApplyHisgnssdataAck.getVehicleNoStr()
					+ ",result code:" + KKTool.byteToHexStr(cmdDownExgMsgApplyHisgnssdataAck.getResult()) + "]");
		}
	}

	// 上报驾驶员身份信息请求
	private void dealCmdDownExgMsgReportDriverInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgReportDriverInfo cmdDownExgMsgReportDriverInfo = new CmdDownExgMsgReportDriverInfo();
		if (cmdDownExgMsgReportDriverInfo.disposeData(channelBuffer)) {
			String vehicleNo = cmdDownExgMsgReportDriverInfo.getVehicleNoStr();
			DriverBean driverBean = GlobalVar.driverMap.get(vehicleNo);

			CmdUpExgMsgReportDriverInfoAck cmdUpExgMsgReportDriverInfoAck = new CmdUpExgMsgReportDriverInfoAck();
			cmdUpExgMsgReportDriverInfoAck.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgReportDriverInfoAck.setSubMsgId(Const.UP_EXG_MSG_REPORT_DRIVER_INFO_ACK);
			cmdUpExgMsgReportDriverInfoAck.setVehicleNo(cmdDownExgMsgReportDriverInfo.getVehicleNo());
			cmdUpExgMsgReportDriverInfoAck.setVehicleColor(cmdDownExgMsgReportDriverInfo.getVehicleColor());
			cmdUpExgMsgReportDriverInfoAck.setDriverName(KKTool.toGBKBytes(driverBean.getName()));
			cmdUpExgMsgReportDriverInfoAck.setDriverId(KKTool.toGBKBytes(driverBean.getId()));
			cmdUpExgMsgReportDriverInfoAck.setLicence(KKTool.toGBKBytes(driverBean.getLicence()));
			cmdUpExgMsgReportDriverInfoAck.setOrgName(KKTool.toGBKBytes(driverBean.getOrgName()));

			sendFromMainLink(true, cmdUpExgMsgReportDriverInfoAck.getSendBuffer());
		}
	}

	// 上报车辆电子运单请求
	private void dealCmdDownExgMsgTakeEWaybillReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownExgMsgTakeEwaybillReq cmdDownExgMsgTakeEWaybillReq = new CmdDownExgMsgTakeEwaybillReq();
		if (cmdDownExgMsgTakeEWaybillReq.disposeData(channelBuffer)) {
			String vehicleNo = cmdDownExgMsgTakeEWaybillReq.getVehicleNoStr();
			String eWayBill = dbOper.getEWayBill(vehicleNo);
			if (eWayBill != null) {
				CmdUpExgMsgTakeEWayBillAck cmdUpExgMsgTakeEWayBillAck = new CmdUpExgMsgTakeEWayBillAck();
				cmdUpExgMsgTakeEWayBillAck.setMsgFlagId(Const.UP_EXG_MSG);
				cmdUpExgMsgTakeEWayBillAck.setSubMsgId(Const.UP_EXG_MSG_TAKE_EWAYBILL_ACK);
				cmdUpExgMsgTakeEWayBillAck.setVehicleNo(cmdDownExgMsgTakeEWaybillReq.getVehicleNo());
				cmdUpExgMsgTakeEWayBillAck.setVehicleColor(cmdDownExgMsgTakeEWaybillReq.getVehicleColor());
				cmdUpExgMsgTakeEWayBillAck.seteWayBillData(KKTool.toGBKBytes(eWayBill));
				sendFromMainLink(true, cmdUpExgMsgTakeEWayBillAck.getSendBuffer());
			}
		}
	}

//平台查岗请求
	private void dealCmdDownPlatformMsgPostQueryReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownPlatformMsgPostQueryReq cmdDownPlatformMsgPostQueryReq = new CmdDownPlatformMsgPostQueryReq();
		if (cmdDownPlatformMsgPostQueryReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownPlatformMsgPostQueryReq(cmdDownPlatformMsgPostQueryReq);
        }
	}
	//下发平台间报文请求
	private void dealCmdDownPlatformMsgInfoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownPlatformMsgInfoReq cmdDownPlatformMsgInfoReq = new CmdDownPlatformMsgInfoReq();
		if (cmdDownPlatformMsgInfoReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownPlatformMsgInfoReq(cmdDownPlatformMsgInfoReq);
        }
	}
	//报警督办请求
	private void dealCmdDownWarnMsgUrgeTodoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgUrgeTodoReq cmdDownWarnMsgUrgeTodoReq = new CmdDownWarnMsgUrgeTodoReq();
		if (cmdDownWarnMsgUrgeTodoReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownWarnMsgUrgeTodo(cmdDownWarnMsgUrgeTodoReq);
        }
	}

	//报警预警
	private void dealCmdDownWarnMsgInformTips(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgInformTips cmdDownWarnMsgInformTips = new CmdDownWarnMsgInformTips();
		if (cmdDownWarnMsgInformTips.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownWarnMsgInformTips(cmdDownWarnMsgInformTips);
		}
	}

	//实时交换报警信息
	private void dealCmdDownWarnMsgExgInform(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownWarnMsgExgInform cmdDownWarnMsgExgInform = new CmdDownWarnMsgExgInform();
		if (cmdDownWarnMsgExgInform.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownWarnMsgExgInform(cmdDownWarnMsgExgInform);
		}
	}

	//车辆单向监听请求
	private void dealCmdDownCtrlMsgMonitorVehicleReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgMonitorVehicleReq cmdDownCtrlMsgMonitorVehicleReq = new CmdDownCtrlMsgMonitorVehicleReq();
		if (cmdDownCtrlMsgMonitorVehicleReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownCtrlMsgMonitorVehicle(cmdDownCtrlMsgMonitorVehicleReq);
		}
	}

	//车辆拍照请求
	private void dealCmdDownCtrlMsgTakePhotoReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTakePhotoReq cmdDownCtrlMsgTakePhotoReq = new CmdDownCtrlMsgTakePhotoReq();
		if (cmdDownCtrlMsgTakePhotoReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownCtrlMsgTakePhoto(cmdDownCtrlMsgTakePhotoReq);
		}
	}

	//下发车辆报文请求
	private void dealCmdDownCtrlMsgTextInfo(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTextInfo cmdDownCtrlMsgTextInfo = new CmdDownCtrlMsgTextInfo();
		if (cmdDownCtrlMsgTextInfo.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownCtrlMsgTextInfo(cmdDownCtrlMsgTextInfo);
		}
	}

	//上报车辆行驶记录请求
	private void dealCmdDownCtrlMsgTakeTravelReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgTakeTravelReq cmdDownCtrlMsgTakeTravelReq = new CmdDownCtrlMsgTakeTravelReq();
		if (cmdDownCtrlMsgTakeTravelReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownCtrlMsgTakeTravel(cmdDownCtrlMsgTakeTravelReq);
		}
	}

	//车辆应急接入监管平台请求
	private void dealCmdDownCtrlMsgEmergencyMonitoringReq(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownCtrlMsgEmergencyMonitoringReq cmdDownCtrlMsgEmergencyMonitoringReq = new CmdDownCtrlMsgEmergencyMonitoringReq();
		if (cmdDownCtrlMsgEmergencyMonitoringReq.disposeData(channelBuffer)) {
            DBOper.getDBOper().insertDownCtrlMsgEmergencyMonitoring(cmdDownCtrlMsgEmergencyMonitoringReq);
		}
	}


	// 补报车辆静态信息请求
	private void dealCmdDownBaseMsgVehicleAdded(ChannelBuffer channelBuffer, IMainSubLink link) {
		CmdDownBaseMsgVehicleAdded cmdDownBaseMsgVehicleAdded = new CmdDownBaseMsgVehicleAdded();
		if (cmdDownBaseMsgVehicleAdded.disposeData(channelBuffer)) {
			//String vehicleNo = cmdDownBaseMsgVehicleAdded.getVehicleNoStr();
			//TODO add vehicle whole info,need to edit table structure
			CmdUpBaseMsgVehicleAddedAck cmdUpBaseMsgVehicleAddedAck = new CmdUpBaseMsgVehicleAddedAck();
			cmdUpBaseMsgVehicleAddedAck.setMsgFlagId(Const.UP_BASE_MSG);
			cmdUpBaseMsgVehicleAddedAck.setSubMsgId(Const.UP_BASE_MSG_VEHICLE_ADDED_ACK);
			cmdUpBaseMsgVehicleAddedAck.setVehicleNo(cmdDownBaseMsgVehicleAdded.getVehicleNo());
			cmdUpBaseMsgVehicleAddedAck.setVehicleColor(cmdDownBaseMsgVehicleAdded.getVehicleColor());

			sendFromMainLink(true, cmdUpBaseMsgVehicleAddedAck.getSendBuffer());
		}
	}

	/**
	 * 默认由主链路发送数据
	 * @param isSwitchedIfNotConnected  true:主链路断开，启用从链路发送
	 * @param buffer
	 */
	private void sendFromMainLink(boolean isSwitchedIfNotConnected, ChannelBuffer buffer) {
		IMainSubLink mLink = this.gw809.getMainLink(isSwitchedIfNotConnected);
		mLink.sendData(buffer);
	}

	/**
	 * 默认由从链路发送数据
	 * @param isSwitchedIfNotConnected	true:从链路断开，启用主链路发送
	 * @param buffer
	 */
	private void sendFromSubLink(boolean isSwitchedIfNotConnected, ChannelBuffer buffer) {
		IMainSubLink sLink = this.gw809.getSubLink(isSwitchedIfNotConnected);
		sLink.sendData(buffer);
	}

}

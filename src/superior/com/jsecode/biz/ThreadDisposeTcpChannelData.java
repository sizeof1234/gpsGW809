package superior.com.jsecode.biz;

import org.jboss.netty.buffer.ChannelBuffer;

import superior.com.jsecode.tcp.TcpChannel;

import com.jsecode.cmd.CmdHead;
import com.jsecode.cmd.ICmd;
import com.jsecode.cmd.up.req.CmdUpConnectReq;
import com.jsecode.cmd.up.req.CmdUpDisconnectReq;
import com.jsecode.cmd.up.req.CmdUpExgMsgHistoryLocationReq;
import com.jsecode.cmd.up.req.CmdUpExgMsgRealLocationReq;
import com.jsecode.cmd.up.req.CmdUpExgMsgRegisterReq;
import com.jsecode.cmd.up.req.CmdUpLinkTestReq;
import com.jsecode.cmd.up.resp.CmdUpConnectResp;
import com.jsecode.cmd.up.resp.CmdUpDisconnectResp;
import com.jsecode.cmd.up.resp.CmdUpLinkTestResp;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

/**
 * @author Jadic
 * @created 2014-4-14
 */
public class ThreadDisposeTcpChannelData implements Runnable {

	private TcpChannel tcpChannel;

	final static int MAX_DISPOSE_COUNT = 150;// 线程处理每个通道一次最多连续处理次数

	public ThreadDisposeTcpChannelData(TcpChannel tcpChannel) {
		this.tcpChannel = tcpChannel;
	}

	@Override
	public void run() {
		boolean isSucc = tcpChannel.checkAndSetDisposeFlag();
		if (isSucc) {
			ChannelBuffer buffer = null;
			int disposeCount = 0;
			while ((buffer = tcpChannel.getNextBuffer()) != null) {
				disposeBuffer(buffer);
				disposeCount++;

				// if tcp channel is closed, dispose all the buffer data in the
				// queue
				if (!tcpChannel.isClosed()) {
					if (disposeCount >= MAX_DISPOSE_COUNT) {
						KKLog.warn(tcpChannel + " dispose buffer over " + MAX_DISPOSE_COUNT + " times, left size:" + tcpChannel.getBufferQueueSize());
						break;
					}
				} 
			}
			if (!tcpChannel.isClosed()) {
				tcpChannel.setDisposing(false);
			} else {
				tcpChannel = null;
			}
			/*if (disposeCount > 1) {
				KKLog.info(tcpChannel + " dispose buffer " + disposeCount + " times");
			}*/
		}
	}

	private void disposeBuffer(ChannelBuffer buffer) {
		if (buffer == null || buffer.readableBytes() < Const.CMD_MIN_SIZE) {
			return;
		}

		short cmdFlag = buffer.getShort(buffer.readerIndex() + 9);
		switch (cmdFlag) {
		case Const.UP_CONNECT_REQ: // 主链路登录请求消息 主链路
			dealCmdUpConnectReq(buffer);
			break;
		case Const.UP_DISCONNECE_REQ: // 主链路注销请求消息 主链路
			dealCmdUpDisconnectReq(buffer);
			break;
		case Const.UP_LINKETEST_REQ: // 主链路连接保持请求消息 主链路
			dealCmdUpLinkTestReq(buffer);
			break;
		case Const.UP_EXG_MSG: // 主链路动态信息交换消息 主链路
		case Const.UP_PLAFORM_MSG: // 主链路平台间信息交互消息 主链路
		case Const.UP_WARN_MSG: // 主链路报警信息交互消息 主链路
		case Const.UP_CTRL_MSG: // 主链路车辆监管消息 主链路
		case Const.UP_BASE_MSG: // 主链路静态信息交换消息 主链路
			short subBizCmdFlag = buffer.getShort(buffer.readerIndex() + 9 + 2 + 4 + 3 + 1 + 4 + 22);
			if (cmdFlag == Const.DOWN_PLATFORM_MSG) {
				subBizCmdFlag = buffer.getShort(buffer.readerIndex() + 9 + 2 + 4 + 3 + 1 + 4);
			}
			dispose809SubBizData(subBizCmdFlag, buffer);
			break;
		default:
			KKLog.warn("Unknown command flag:" + KKTool.byteArrayToHexStr(KKTool.short2BytesBigEndian(cmdFlag)));
			break;
		}
	}

	private void dealCmdUpConnectReq(ChannelBuffer buffer) {
		CmdUpConnectReq cmdUpConnectReq = new CmdUpConnectReq();
		if (cmdUpConnectReq.disposeData(buffer)) {
			// TODO check user auth

			CmdUpConnectResp cmdUpConnectResp = new CmdUpConnectResp();
			cmdUpConnectResp.setMsgFlagId(Const.UP_CONNECT_RSP);
			cmdUpConnectResp.setRet((byte) 0);
			cmdUpConnectResp.setVerifyCode(1);
			this.sendRespCmd(cmdUpConnectResp);
			KKLog.info(tcpChannel + " log in");
		}
	}

	private void dealCmdUpDisconnectReq(ChannelBuffer buffer) {
		CmdUpDisconnectReq cmdUpDisconnectReq = new CmdUpDisconnectReq();
		if (cmdUpDisconnectReq.disposeData(buffer)) {
			CmdUpDisconnectResp cmdUpDisconnectResp = new CmdUpDisconnectResp();
			cmdUpDisconnectResp.setMsgFlagId(Const.UP_DISCONNECT_RSP);
			this.sendRespCmd(cmdUpDisconnectResp);
		}
	}

	private void dealCmdUpLinkTestReq(ChannelBuffer buffer) {
		CmdUpLinkTestReq cmdUpLinkTestReq = new CmdUpLinkTestReq();
		if (cmdUpLinkTestReq.disposeData(buffer)) {
			CmdUpLinkTestResp cmdUpLinkTestResp = new CmdUpLinkTestResp();
			cmdUpLinkTestResp.setMsgFlagId(Const.UP_LINKTEST_RSP);
			this.sendRespCmd(cmdUpLinkTestResp);
		}
	}

	private void dispose809SubBizData(short subBizCmdFlag, ChannelBuffer buffer) {
		switch (subBizCmdFlag) {
		case Const.UP_EXG_MSG_REGISTER: // 上传车辆注册信息
			dealCmdUpExgMsgRegister(buffer);
			break;
		case Const.UP_EXG_MSG_REAL_LOCATION: // 事实上传车辆定位信息
			dealCmdUpExgMsgRealLocation(buffer);
			break;
		case Const.UP_EXG_MSG_HISTORY_LOCATION: // 车辆定位信息自动补报
			dealCmdUpExgMsgHistoryLocation(buffer);
			break;
		default:
			KKLog.warn("Unknown subBizCmd flag:" + KKTool.byteArrayToHexStr(KKTool.short2BytesBigEndian(subBizCmdFlag)));
			break;
		}
	}
	
	private void dealCmdUpExgMsgRegister(ChannelBuffer buffer) {
		CmdUpExgMsgRegisterReq cmdUpExgMsgRegisterReq = new CmdUpExgMsgRegisterReq();
		if (cmdUpExgMsgRegisterReq.disposeData(buffer)) {
			this.forwardCmd(cmdUpExgMsgRegisterReq);
			KKLog.info(tcpChannel + " register");
		}
	}
	
	private void dealCmdUpExgMsgRealLocation(ChannelBuffer buffer) {
		CmdUpExgMsgRealLocationReq cmdUpExgMsgRealLocationReq = new CmdUpExgMsgRealLocationReq();
		if (cmdUpExgMsgRealLocationReq.disposeData(buffer)) {
			this.forwardCmd(cmdUpExgMsgRealLocationReq);
			KKLog.info(tcpChannel + " upload gps data");
		}
	}
	
	private void dealCmdUpExgMsgHistoryLocation(ChannelBuffer buffer) {
		CmdUpExgMsgHistoryLocationReq cmdUpExgMsgHistoryLocationReq = new CmdUpExgMsgHistoryLocationReq();
		if (cmdUpExgMsgHistoryLocationReq.disposeData(buffer)) {
			this.forwardCmd(cmdUpExgMsgHistoryLocationReq);
			KKLog.info(tcpChannel + " upload history location");
		}
	}
	
	/**
	 * send response command
	 * @param cmd
	 */
	private void sendRespCmd(ICmd cmd) {
		this.tcpChannel.sendData(cmd.getSendBuffer());
	}
	
	private void forwardCmd(CmdHead cmd) {
		this.tcpChannel.getTcpServer().forwardData(cmd);
	}

}

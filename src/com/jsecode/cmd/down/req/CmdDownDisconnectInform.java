package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * 
 * 项目名称：gpsGW809 类名称：CmdDownDisconnectInformReq 类描述：
 ***************************** 
 * 4.5.1.15 从链路断开通知消息 链路类型：主链路。 消息方向：上级平台往下级平台。 业务数据类型标识：DOWN_DISCONNECT_INFORM。
 * 描述：情景1：上级平台与下级平台的从链路中断后，重连三次仍未成功时，上级平台通过主链路发送本消息给下级平台。
 * 情景2：上级平台作为客户端向下级平台登录时，根据之前收到的IP地址及端口无法连接到下级平台服务端时发送本 消息通知下级平台
 ***************************** 
 * 创建人：zhaorg 创建时间：2014-2-17 上午10:15:59 修改人： 修改时间： 修改备注：
 * 
 * @version
 * 
 * 
 */
public class CmdDownDisconnectInform extends CmdHead {

	/*
	 * 错误代码{0x00：无法连接下级平台指定的服务IP与端口， 0x01：上级平台客户端与下级平台服务端断开； 0x02：其他原因。}
	 */
	private byte errorCode;

	public CmdDownDisconnectInform() {

	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.errorCode = channelBuffer.readByte();
		KKLog.info("DOWN_DISCONNECT_INFORM------4.5.1.15 从链路断开通知消息 ");
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.errorCode);
	}

	@Override
	protected int getCmdBodySize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(byte errorCode) {
		this.errorCode = errorCode;
	}

}

/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

/**
 * 主链路登录请求消息
 * @author Jadic
 */
public class CmdUpConnectReq extends CmdHead{
	
	private int userId;//用户名
	private byte[] userPass;//用户密码 8字节
	private byte[] downLinkIp;//下级平台提供对应的从链路服务端IP地址 32字节
	private short downLinkPort;//下级平台提供对应的从链路服务器端口号
	
	public CmdUpConnectReq() {
		this.userPass = new byte[8];
		this.downLinkIp = new byte[32];
	}

	@Override
	protected int getCmdBodySize() {
		return 4 + this.userPass.length + this.downLinkIp.length + 2;
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.userId = channelBuffer.readInt();
		channelBuffer.readBytes(this.userPass);
		channelBuffer.readBytes(this.downLinkIp);
		this.downLinkPort = channelBuffer.readShort();
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.userId);
		channelBuffer.writeBytes(this.userPass);
		channelBuffer.writeBytes(this.downLinkIp);
		channelBuffer.writeShort(this.downLinkPort);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getUserPass() {
		return userPass;
	}

	public void setUserPass(byte[] userPass) {
		if (isByteArraySameSize(this.userPass, userPass)) {
			this.userPass = userPass;
		}
	}

	public byte[] getDownLinkIp() {
		return downLinkIp;
	}

	public void setDownLinkIp(byte[] downLinkIp) {
		if (isByteArraySameSize(this.downLinkIp, downLinkIp)) {
			this.downLinkIp = downLinkIp;
		}
	}

	public short getDownLinkPort() {
		return downLinkPort;
	}

	public void setDownLinkPort(short downLinkPort) {
		this.downLinkPort = downLinkPort;
	}

}

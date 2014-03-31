/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

/**
 * 主链路注销请求消息
 * 
 * 描述：下级平台在中断与上级平台的主链路连接时，
 * 应向上级平台发送主链路注销请求消息
 * @author Jadic
 */
public class CmdUpDisconnectReq extends CmdHead {
	
	private int userId;
	private byte[] userPass;//8字节密码
	
	public CmdUpDisconnectReq() {
		this.userPass = new byte[8];
	}

	@Override
	protected int getCmdBodySize() {
		return 4 + this.userPass.length;
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.userId);
		channelBuffer.writeBytes(this.userPass);
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

}

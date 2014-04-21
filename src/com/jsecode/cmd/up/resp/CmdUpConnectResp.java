/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.up.resp;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

/**
 * 主链路登录应答消息
 * @author Jadic
 */
public class CmdUpConnectResp extends CmdHead {
	
	private byte ret;		//验证结果
	private int verifyCode;	//校验码
	
	public CmdUpConnectResp() {
	}
	
	@Override
	protected int getCmdBodySize() {
		return 1 + 4;
	}
	
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.ret = channelBuffer.readByte();
		this.verifyCode = channelBuffer.readInt();
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.ret);
		channelBuffer.writeInt(this.verifyCode);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getRet() {
		return ret;
	}

	public void setRet(byte ret) {
		this.ret = ret;
	}

	public int getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(int verifyCode) {
		this.verifyCode = verifyCode;
	}

}

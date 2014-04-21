/**
 * @author 	Jadic
 * @created 2014-2-14
 */
/**
 * 
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

/**
 * 主链路断开通知消息
 * 
 * 描述：当主链路中断后，下级平台可通过从链路向上级平台
 * 发送本消息通知上级平台主链路中断
 * @author Jadic
 */
public class CmdUpDisconnectInform extends CmdHead {
	
	private byte errorCode;//错误代码  0x00:主链路断开；0x01：其他原因

	public CmdUpDisconnectInform() {
	}

	@Override
	protected int getCmdBodySize() {
		return 1;
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.errorCode = channelBuffer.readByte();
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.errorCode);
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

/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.up.resp;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

public class CmdUpCloseLinkInform extends CmdHead {
	
	private byte errorCode;//错误代码：定义如下	0x00:网关重启；	0x01：其他原因

	public CmdUpCloseLinkInform() {
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

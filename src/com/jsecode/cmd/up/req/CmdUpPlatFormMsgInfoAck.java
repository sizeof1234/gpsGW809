/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithoutCar;

/**
 * 下发平台间报文应答消息
 * 
 * 描述：下级平台收到上级平台发送的下发平台间报文请求消息后，发送应答消息
 * @author Jadic
 */
public class CmdUpPlatFormMsgInfoAck extends CmdHeadSubBizWithoutCar {

	private int infoId;//收到信息的ID
	
	public CmdUpPlatFormMsgInfoAck() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 4;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.infoId);
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

}

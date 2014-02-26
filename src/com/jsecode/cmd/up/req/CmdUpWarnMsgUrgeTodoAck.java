/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 报警督办应答消息
 * 
 * 描述：下级平台应答上级平台下发的报警督办请求消息，
 * 向上级平台上报车辆的报警处理结果
 * @author Jadic
 */
public class CmdUpWarnMsgUrgeTodoAck extends CmdHeadSubBizWithCar {
	
	private int supervisonId;	//报警督办ID
	private byte result;		//报警处理结果

	public CmdUpWarnMsgUrgeTodoAck() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 4 + 1;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.supervisonId);
		channelBuffer.writeByte(this.result);
	}

	public int getSupervisonId() {
		return supervisonId;
	}

	public void setSupervisonId(int supervisonId) {
		this.supervisonId = supervisonId;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}
	
}

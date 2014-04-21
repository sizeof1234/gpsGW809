/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 车辆单向监听应答消息
 * 
 * 描述：下级平台向上级平台上传车辆单向监听请求消息的应答
 * @author Jadic
 */
public class CmdUpCtrlMsgMonitorVehicleAck extends CmdHeadSubBizWithCar {
	
	private byte result;//车辆单向监听应答结果  0x00:监听成功  0x01:监听失败

	public CmdUpCtrlMsgMonitorVehicleAck() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.result = channelBuffer.readByte();
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.result);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

}

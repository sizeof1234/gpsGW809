/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 车辆应急接入监管平台应答消息
 * 
 * 描述：下级平台应答上级平台下发的车辆应急接入监管平台请求消息应答
 * @author Jadic
 */
public class CmdCtrlMsgEmergencyMonitoringAck extends CmdHeadSubBizWithCar {
	
	private byte result;//0x00:车载终端成功收到该命令  0x01:无该车辆  0x02:其他原因失败

	public CmdCtrlMsgEmergencyMonitoringAck() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.result);
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

}

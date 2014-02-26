/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 补报车辆静态信息应答消息
 * 
 * 描述：下级平台应答上级平台发送的补报车辆静态信息请求消息
 * @author Jadic
 */
public class CmdUpBaseMsgVehicleAddedAck extends CmdHeadSubBizWithCar {
	
	private byte[] vehicleInfo;//车辆信息

	public CmdUpBaseMsgVehicleAddedAck() {
		this.vehicleInfo = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return this.vehicleInfo.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeBytes(this.vehicleInfo);
	}

	public byte[] getVehicleInfo() {
		return vehicleInfo;
	}

	public void setVehicleInfo(byte[] vehicleInfo) {
		if (vehicleInfo != null) {
			this.vehicleInfo = vehicleInfo;
		}
	}

}

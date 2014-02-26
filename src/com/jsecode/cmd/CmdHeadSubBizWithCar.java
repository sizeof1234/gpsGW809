/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd;

import org.jboss.netty.buffer.ChannelBuffer;


/**
 * 带车辆信息的子业务命令公共头
 */
public abstract class CmdHeadSubBizWithCar extends CmdHeadSubBizWithoutCar {
	
	private byte[] vehicleNo;
	private byte vehicleColor;
	
	public CmdHeadSubBizWithCar() {
		this.vehicleNo = new byte[21];
	}

	@Override
	protected int getCmdBodySize() {
		return this.vehicleNo.length + 1 + 2 + 1 + getCmdSubBizDataSize();
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.readBytes(this.vehicleNo);
		this.vehicleColor = channelBuffer.readByte();
		this.setSubMsgId(channelBuffer.readShort());
		this.setSubDataSize(channelBuffer.readInt());
		disposeCmdSubBizData(channelBuffer);
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeBytes(this.vehicleNo);
		channelBuffer.writeByte(this.vehicleColor);
		channelBuffer.writeShort(this.getSubMsgId());
		channelBuffer.writeInt(this.getSubDataSize());
		fillCmdSubBizData(channelBuffer);
	}

	public byte[] getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(byte[] vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public byte getVehicleColor() {
		return vehicleColor;
	}

	public void setVehicleColor(byte vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

}

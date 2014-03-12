/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;


/**
 * 带车辆信息的子业务命令公共头
 */
public abstract class CmdHeadSubBizWithCar extends CmdHeadSubBizWithoutCar {
	
	private byte[] vehicleNo;
	private byte vehicleColor;
	
	private String vehicleNoStr = null;//该字段无需dispose、fill操作
	
	public CmdHeadSubBizWithCar() {
		this.vehicleNo = new byte[21];
	}

	@Override
	protected int getCmdBodySize() {
		return this.vehicleNo.length + 1 + 2 + 4 + getCmdSubBizDataSize();
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
		
		this.setSubDataSize(this.getCmdSubBizDataSize());
		
		channelBuffer.writeInt(this.getSubDataSize());
		fillCmdSubBizData(channelBuffer);
	}

	public byte[] getVehicleNo() {
		return vehicleNo;
	}
	
	public String getVehicleNoStr() {
		if (this.vehicleNoStr == null) {
			this.vehicleNoStr = new String(this.vehicleNo, Charset.forName("GBK")).trim();
		}
		return this.vehicleNoStr;
	}

	public void setVehicleNo(byte[] vehicleNo) {
		if (isByteArraySameSize(this.vehicleNo, vehicleNo)) {
			this.vehicleNo = vehicleNo;
		}
	}

	public byte getVehicleColor() {
		return vehicleColor;
	}

	public void setVehicleColor(byte vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

}

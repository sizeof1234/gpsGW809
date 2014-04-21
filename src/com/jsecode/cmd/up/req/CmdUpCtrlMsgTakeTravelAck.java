/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 上报车辆行驶记录应答消息
 * 
 * 描述：下级平台应答上级平台下发的上报车辆行驶记录请求消息，
 * 将车辆行驶记录数据上传至上级平台
 * @author Jadic
 */
public class CmdUpCtrlMsgTakeTravelAck extends CmdHeadSubBizWithCar {
	
	private int travelDataSize;//车辆行驶记录数据体长度
	private byte[] travelData;//行驶记录信息

	public CmdUpCtrlMsgTakeTravelAck() {
		this.travelData = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 4;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.travelDataSize = channelBuffer.readInt();
		this.travelData = new byte[this.travelDataSize];
		channelBuffer.readBytes(this.travelData);
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.travelDataSize);
		channelBuffer.writeBytes(this.travelData);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int getTravelDataSize() {
		return travelDataSize;
	}

	public byte[] getTravelData() {
		return travelData;
	}

	public void setTravelData(byte[] travelData) {
		if (travelData != null) {
			this.travelData = travelData;
			this.travelDataSize = this.travelData.length;
		}
	}

}

/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 上报车辆电子运单应答消息
 * 
 * 描述：下级平台应答上级平台发送的上报车辆电子运单请求消息，
 * 向上级平台上传车辆当前电子运单
 * @author Jadic
 */
public class CmdUpExgMsgTakeEWayBillAck extends CmdHeadSubBizWithCar {
	
	private int eWayBillDataSize;
	private byte[] eWayBillData;

	public CmdUpExgMsgTakeEWayBillAck() {
		eWayBillData = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return eWayBillDataSize + eWayBillData.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		this.eWayBillDataSize = this.eWayBillData.length;
		
		channelBuffer.writeInt(eWayBillDataSize);
		channelBuffer.writeBytes(eWayBillData);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int geteWayBillDataSize() {
		return eWayBillDataSize;
	}

	public byte[] geteWayBillData() {
		return eWayBillData;
	}

	public void seteWayBillData(byte[] eWayBillData) {
		if (eWayBillData != null) {
			this.eWayBillData = eWayBillData;
		}
	}

}

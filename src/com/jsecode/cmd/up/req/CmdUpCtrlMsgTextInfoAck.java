/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 下发车辆报文应答消息
 * 
 * 描述：下级平台应答上级平台下发的报文是否成功到达指定车辆
 * @author Jadic
 */
public class CmdUpCtrlMsgTextInfoAck extends CmdHeadSubBizWithCar {
	
	private int msgId;	//对应“下发车辆报文请求信息”中的msgId
	private byte result;//0x00:下发成功  0x01:下发失败

	public CmdUpCtrlMsgTextInfoAck() {
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
		channelBuffer.writeInt(this.msgId);
		channelBuffer.writeByte(this.result);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

}

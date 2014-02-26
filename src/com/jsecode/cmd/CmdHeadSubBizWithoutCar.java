/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 不带车辆信息的子业务命令公共头
 */
public abstract class CmdHeadSubBizWithoutCar extends CmdHead {
	
	private short subMsgId;//子业务类型
	private int subDataSize;//子业务数据长度（后续数据长度）

	public CmdHeadSubBizWithoutCar() {
	}

	@Override
	protected int getCmdBodySize() {
		return 2 + 1 + getCmdSubBizDataSize();
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.subMsgId = channelBuffer.readShort();
		this.subDataSize = channelBuffer.readInt();
		this.disposeCmdSubBizData(channelBuffer);
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeShort(this.subMsgId);
		channelBuffer.writeInt(this.subDataSize);
		this.fillCmdSubBizData(channelBuffer);
	}
	//子业务数据长度
	protected abstract int getCmdSubBizDataSize();
	
	//解析子业务命令数据
	protected abstract void disposeCmdSubBizData(ChannelBuffer channelBuffer);

	//填充子业务命令数据
	protected abstract void fillCmdSubBizData(ChannelBuffer channelBuffer);

	public short getSubMsgId() {
		return subMsgId;
	}

	public void setSubMsgId(short subMsgId) {
		this.subMsgId = subMsgId;
	}

	public int getSubDataSize() {
		return subDataSize;
	}

	public void setSubDataSize(int subDataSize) {
		this.subDataSize = subDataSize;
	}
	
}

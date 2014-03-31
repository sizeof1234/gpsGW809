/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 上报报警信息消息
 * 
 * 描述：下级平台向上级平台上报某车辆的报警信息.
 * 本条消息上级平台无需应答
 * @author Jadic
 */
public class CmdUpWarnMsgAdptInfo extends CmdHeadSubBizWithCar {
	
	private byte warnSrc;		//报警来源
	private short warnType;		//报警类型
	private long warnTime;		//报警时间
	private int infoId;			//信息ID
	private int infoSize;		//信息类型长度
	private byte[] infoContent;	//信息内容

	public CmdUpWarnMsgAdptInfo() {
		this.infoContent = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1 + 2 + 8 + 4 + 4 + this.infoContent.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.warnSrc);
		channelBuffer.writeShort(this.warnType);
		channelBuffer.writeLong(this.warnTime);
		channelBuffer.writeInt(this.infoId);
		
		this.infoSize = this.infoContent.length;
		channelBuffer.writeInt(infoSize);
		channelBuffer.writeBytes(infoContent);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getWarnSrc() {
		return warnSrc;
	}

	public void setWarnSrc(byte warnSrc) {
		this.warnSrc = warnSrc;
	}

	public short getWarnType() {
		return warnType;
	}

	public void setWarnType(short warnType) {
		this.warnType = warnType;
	}

	public long getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(long warnTime) {
		this.warnTime = warnTime;
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public int getInfoSize() {
		return infoSize;
	}

	public byte[] getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(byte[] infoContent) {
		if (infoContent != null) {
			this.infoContent = infoContent;
		}
	}
	
}

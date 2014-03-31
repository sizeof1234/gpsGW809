/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithoutCar;

/**
 * 平台查岗应答消息
 * 
 * 描述：下级平台根据查岗对象的类型将上级平台发送的不定期平台查岗消息发送到
 * 不同的查岗对象，并将不同查岗对象的应答分别转发给上级平台
 * @author Jadic
 */
public class CmdUpPlatFormMsgPostQueryAck extends CmdHeadSubBizWithoutCar {
	
	private byte objectType;	//1 BYTE 查岗对象的类型
	private byte[] objectId;	//12 Octet String 查岗对象的ID，长度不足时后补0X00
	private int infoId;			//4 uint32_t 信息ID
	private int infoSize;		//4 uint32_t 数据长度
	private byte[] infoContent;	//Octet String 应答内容

	public CmdUpPlatFormMsgPostQueryAck() {
		this.objectId = new byte[12];
		infoContent = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1 + this.objectId.length + 4 + 4 + this.infoContent.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.objectType);
		channelBuffer.writeBytes(this.objectId);
		channelBuffer.writeInt(this.infoId);
		
		this.infoSize = this.infoContent.length;
		channelBuffer.writeInt(this.infoSize);
		channelBuffer.writeBytes(this.infoContent);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getObjectType() {
		return objectType;
	}

	public void setObjectType(byte objectType) {
		this.objectType = objectType;
	}

	public byte[] getObjectId() {
		return objectId;
	}

	public void setObjectId(byte[] objectId) {
		if (isByteArraySameSize(this.objectId, objectId)) {
			this.objectId = objectId;
		}
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

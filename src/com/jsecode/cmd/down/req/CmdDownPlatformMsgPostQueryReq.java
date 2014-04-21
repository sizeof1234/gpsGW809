package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithoutCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 项目名称：gpsGW809
 * 类名称：CmdDownPlatformMsgPostQueryReq
 * 类描述：
 * ****************************
 * 4.5.4.2.2平台查岗请求消息
 * 子业务类型标识：DOWN_PLATFORM_MSG_POST_QUERY_REQ。
 * 描述：上级平台不定期向下级平台发送平台查岗信息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：2014-2-17 下午03:34:49
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CmdDownPlatformMsgPostQueryReq extends CmdHeadSubBizWithoutCar {

    private byte objectType;     //查岗对象的类型
    private byte[] objectId;     //查岗对象的ID
    private int infoId;             //信息ID
    private int infoLength;         //信息长度
    private byte[] infoContent;     //信息内容

    public CmdDownPlatformMsgPostQueryReq() {
        objectId = new byte[12];
        infoContent = ZERO_BYTES;
    }

    @Override
    protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
        this.objectType = channelBuffer.readByte();
        channelBuffer.readBytes(objectId);
        this.infoId = channelBuffer.readInt();
        this.infoLength = channelBuffer.readInt();
        //初始化信息内容字节数组
        infoContent = new byte[infoLength];
        channelBuffer.readBytes(infoContent);
        KKLog.info("DOWN_PLATFORM_MSG_POST_QUERY_REQ------4.5.4.2.2平台查岗请求消息");

    }

    @Override
    protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
    	channelBuffer.writeByte(this.objectType);
    	channelBuffer.writeBytes(this.objectId);
    	channelBuffer.writeInt(this.infoId);
    	channelBuffer.writeInt(this.infoLength);
    	channelBuffer.writeBytes(this.infoContent);
    }

    @Override
    protected int getCmdSubBizDataSize() {
        return 1 + this.objectId.length + 4 + 4 + this.infoLength;
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

    public int getInfoLength() {
        return infoLength;
    }

    public byte[] getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(byte[] infoContent) {
        if (infoContent != null) {
            this.infoContent = infoContent;
            this.infoLength = this.infoContent.length;
        }
    }
}

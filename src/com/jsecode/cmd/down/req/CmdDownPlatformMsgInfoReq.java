package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownPlatformMsgInfoReq   
 * 类描述：   
 ***************************** 
 * 4.5.4.2.3下发平台间报文请求消息
 * 子业务类型标识：DOWN_PLATFORM_MSG_INFO_REQ
 * 描述：上级平台不定期向下级平台下发平台间报文
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午03:52:35   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownPlatformMsgInfoReq extends CmdHead {

	private short dataType;        //子业务标识
	private int dataLength;		 //后续数据长度
	private byte objectType;	 //查岗对象的类型
	private byte [] objectId;	 //查岗对象的ID
	private int infoId;			 //信息ID
	private int infoLength;		 //信息长度
	private byte[] infoContent;	 //信息内容
	public CmdDownPlatformMsgInfoReq(){
		objectId = new byte[12];
        infoContent = ZERO_BYTES;
		
	}
	
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.dataType = channelBuffer.readShort();
		this.dataLength = channelBuffer.readInt();
		this.objectType = channelBuffer.readByte();
		channelBuffer.readBytes(objectId);
		this.infoId = channelBuffer.readInt();
		this.infoLength = channelBuffer.readInt();
		//初始化信息内容字节数组
		infoContent = new byte[infoLength];
		channelBuffer.readBytes(infoContent);
        KKLog.info("DOWN_PLATFORM_MSG_INFO_REQ------4.5.4.2.3下发平台间报文请求消息");
		
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdBodySize() {
		return 2+4+1+this.objectId.length+4+4+this.infoLength;
	}

    public short getDataType() {
        return dataType;
    }

    public void setDataType(short dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
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
        this.objectId = objectId;
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

    public void setInfoLength(int infoLength) {
        this.infoLength = infoLength;
    }

    public byte[] getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(byte[] infoContent) {
        if (isByteArraySameSize(this.infoContent, infoContent)) {
            this.infoContent = infoContent;
        }
    }
}

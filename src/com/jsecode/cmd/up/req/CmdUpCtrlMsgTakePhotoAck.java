/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.cmd.bean.GpsCmdBean;

/**
 * 车辆拍照应答消息
 * 
 * 描述：下级平台应答上级平台发送的车辆拍照请求消息，上传图片信息到上级平台
 * @author Jadic
 */
public class CmdUpCtrlMsgTakePhotoAck extends CmdHeadSubBizWithCar {
	
	private byte photoRespFlag;	//拍照应答标识
	private GpsCmdBean gpsBean;	//拍照时的位置信息
	private byte lensId;		//镜头ID
	private int photoLen;		//照片长度
	private byte sizeType;		//照片尺寸类型
	private byte photoType;		//照片格式类型
	private byte[] photoData;	//照片数据内容

	public CmdUpCtrlMsgTakePhotoAck() {
		this.gpsBean = EMPTY_GPS_BEAN;
		this.photoData = ZERO_BYTES;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1 
			 + gpsBean.getBeanSize() 
			 + 1 
			 + 4 
			 + 1 
			 + 1 
			 + photoData.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.photoRespFlag = channelBuffer.readByte();
		this.gpsBean = new GpsCmdBean();
		this.gpsBean.disposeData(channelBuffer);
		this.lensId = channelBuffer.readByte();
		this.photoLen = channelBuffer.readInt();
		this.sizeType = channelBuffer.readByte();
		this.photoType = channelBuffer.readByte();
		this.photoData = new byte[this.getSubDataSize() - 1 - GpsCmdBean.getCmdBeanSize() - 1 - 4 - 1 - 1];
		channelBuffer.readBytes(this.photoData);
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
        this.photoLen = this.photoData.length;
		channelBuffer.writeByte(this.photoRespFlag);
		this.gpsBean.fillChannelBuffer(channelBuffer);
		channelBuffer.writeByte(this.lensId);
		channelBuffer.writeInt(this.photoLen);
		channelBuffer.writeByte(this.sizeType);
		channelBuffer.writeByte(this.photoType);
		channelBuffer.writeBytes(this.photoData);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getPhotoRespFlag() {
		return photoRespFlag;
	}

	public void setPhotoRespFlag(byte photoRespFlag) {
		this.photoRespFlag = photoRespFlag;
	}

	public byte getLensId() {
		return lensId;
	}

	public void setLensId(byte lensId) {
		this.lensId = lensId;
	}

	public int getPhotoLen() {
		return photoLen;
	}

	public byte getSizeType() {
		return sizeType;
	}

	public void setSizeType(byte sizeType) {
		this.sizeType = sizeType;
	}

	public byte getPhotoType() {
		return photoType;
	}

	public void setPhotoType(byte photoType) {
		this.photoType = photoType;
	}

	public byte[] getPhotoData() {
		return photoData;
	}

	public void setPhotoData(byte[] photoData) {
		if (photoData != null) {
			this.photoData = photoData;
		}
	}

	public void setGpsBean(GpsCmdBean gpsBean) {
		if (gpsBean != null) {
			this.gpsBean = gpsBean;
		}
	}

	public GpsCmdBean getGpsBean() {
		return gpsBean;
	}

}

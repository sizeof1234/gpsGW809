package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownCtrlMsgTakePhotoReq   
 * 类描述：   
 ***************************** 
 * 4.5.6.2.3车辆拍照请求消息
 * 子业务类型标识：DOWN_CTRL_MSG_TAKE_PHOTO_REQ
 * 描述：上级平台向下级平台下发对某指定车辆的拍照请求消息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:39:12   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownCtrlMsgTakePhotoReq extends CmdHeadSubBizWithCar {

	private byte lensId;	//镜头id
	private byte size;		//照片大小

    public CmdDownCtrlMsgTakePhotoReq() {

	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.lensId = channelBuffer.readByte();
		this.size = channelBuffer.readByte();
		KKLog.info("DOWN_CTRL_MSG_TAKE_PHOTO_REQ------4.5.6.2.3车辆拍照请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1+1;
	}

    public byte getLensId() {
        return lensId;
    }

    public void setLensId(byte lensId) {
        this.lensId = lensId;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

}

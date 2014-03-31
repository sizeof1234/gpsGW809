package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownCtrlMsgTextInfo   
 * 类描述：   
 ***************************** 
 * 4.5.6.2.4下发车辆报文请求消息
 * 子业务类型标识：DOWN_CTRL_MSG_TEXT_INFO
 * 描述：用于上级平台向下级平台下发报文到某指定车辆
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:43:21   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownCtrlMsgTextInfo extends CmdHeadSubBizWithCar {

	private int msgSequnce;      //消息id序号
	private byte msgPriority;    //报文优先级{00x0 紧急，00x1 一般}
	private int msgLength;       //报文信息长度
	private byte [] msgContent;  //报文信息内容
	public CmdDownCtrlMsgTextInfo() {
        msgContent = ZERO_BYTES;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.msgSequnce = channelBuffer.readInt();
		this.msgPriority = channelBuffer.readByte();
		this.msgLength = channelBuffer.readInt();
        msgContent = new byte[this.msgLength];
		channelBuffer.readBytes(msgContent);
		KKLog.info("DOWN_CTRL_MSG_TEXT_INFO------4.5.6.2.4下发车辆报文请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 4+1+4+this.msgLength;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public int getMsgSequnce() {
        return msgSequnce;
    }

    public void setMsgSequnce(int msgSequnce) {
        this.msgSequnce = msgSequnce;
    }

    public byte getMsgPriority() {
        return msgPriority;
    }

    public void setMsgPriority(byte msgPriority) {
        this.msgPriority = msgPriority;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public byte[] getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(byte[] msgContent) {
        if (isByteArraySameSize(this.msgContent, msgContent)) {
            this.msgContent = msgContent;
        }

    }
}

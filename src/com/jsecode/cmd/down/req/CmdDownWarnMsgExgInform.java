package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownWarnMsgExgInform   
 * 类描述：   
 ***************************** 
 * 4.5.5.2.4实时交换报警信息消息
 * 子业务类型标识：DOWN_WARN_MSG_EXG_INFORM
 * 描述：用于上级平台向车辆跨域目的地下级平台下发相关车辆的当前报警情况
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:19:21   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownWarnMsgExgInform extends CmdHeadSubBizWithCar {
	
	private byte warnSrc;		 	// 报警信息来源
	private short warnType;		 	// 报警类型
	private long warnTime;		 	// 报警时间
	private int warnLength;   		// 报警信息长度
	private byte [] warnContent;	// 报警信息内容
	
	public CmdDownWarnMsgExgInform() {
        warnContent = ZERO_BYTES;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.warnSrc = channelBuffer.readByte();
		this.warnType = channelBuffer.readShort();
		this.warnTime = channelBuffer.readLong();
		this.warnLength = channelBuffer.readInt();
		//初始化报警信息长度
		warnContent = new byte[this.warnLength];
		channelBuffer.readBytes(warnContent);
		KKLog.info("DOWN_WARN_MSG_EXG_INFORM------4.5.5.2.4实时交换报警信息消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.warnSrc);
		channelBuffer.writeShort(this.warnType);
		channelBuffer.writeLong(this.warnTime);
		channelBuffer.writeInt(this.warnLength);
		channelBuffer.writeBytes(this.warnContent);
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1+2+8+4+this.warnLength;
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

    public int getWarnLength() {
        return warnLength;
    }

    public byte[] getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(byte[] warnContent) {
        if (warnContent != null) {
            this.warnContent = warnContent;
            this.warnLength = this.warnContent.length;
        }
    }
}

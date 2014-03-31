package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownWarnMsgInformTips   
 * 类描述：   
 ***************************** 
 * 4.5.5.2.3报警预警消息
 * 子业务类型标识：DOWN_WARN_MSG_INFORM_TIPS
 * 描述：用于上级平台向车辆归属或车辆跨域下级平台下发相关车辆的报警预警或运行提示信息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:16:46   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownWarnMsgInformTips extends CmdHeadSubBizWithCar {
	private byte warnSrc;		 	// 报警信息来源
	private short warnType;		 	// 报警类型
	private int warnTime;		 	// 报警时间
	private int warnLength;   		// 报警信息长度
	private byte [] warnContent;	// 报警信息内容
	public CmdDownWarnMsgInformTips() {
        this.warnContent = ZERO_BYTES;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.warnSrc = channelBuffer.readByte();
		this.warnType = channelBuffer.readShort();
		this.warnTime = channelBuffer.readInt();
		this.warnLength = channelBuffer.readInt();
		//初始化报警信息长度
		warnContent = new byte[this.warnLength];
		channelBuffer.readBytes(warnContent);
		KKLog.info("DOWN_WARN_MSG_INFORM_TIPS------4.5.5.2.3报警预警消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

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

    public int getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(int warnTime) {
        this.warnTime = warnTime;
    }

    public int getWarnLength() {
        return warnLength;
    }

    public void setWarnLength(int warnLength) {
        this.warnLength = warnLength;
    }

    public byte[] getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(byte[] warnContent) {
        if (isByteArraySameSize(this.warnContent, warnContent)) {
            this.warnContent = warnContent;
        }
    }
}

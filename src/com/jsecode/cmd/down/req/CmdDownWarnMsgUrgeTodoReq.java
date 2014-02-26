package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownWarnMsgUrgeTodoReq   
 * 类描述：   
 ***************************** 
 * 4.5.5.2.2报警督办请求消息
 * 子业务类型标识：DOWN_WARN_MSG_URGE_TODO_REQ。
 * 描述：上级平台向车辆归属下级平台下发本消息，催促其及时处理相关车辆的报警信息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:12:06   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownWarnMsgUrgeTodoReq extends CmdHeadSubBizWithCar {
	private byte warnSrc;		 	// 报警信息来源
	private short warnType;		 	// 报警类型
	private long warnTime;		 	// 报警时间
	private int supervisionId;   	// 报警督办id
	private long supervisionEndTime;// 督办截止时间
	private byte supervisionLevel;  // 督办级别{0x00 紧急，0x01 一般}
	private byte [] superVisor;		// 督办人
	private byte [] superVisorTel;  // 督办联系电话
	private byte [] superVisorEmail;// 督办联系电子邮件
	
	
	
	public CmdDownWarnMsgUrgeTodoReq() {
		superVisor = new byte[16];
		superVisorTel = new byte[20];
		superVisorEmail = new byte[32];
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.warnSrc = channelBuffer.readByte();
		this.warnType = channelBuffer.readShort();
		this.warnTime = channelBuffer.readLong();
		this.supervisionId = channelBuffer.readInt();
		this.supervisionEndTime = channelBuffer.readLong();
		this.supervisionLevel = channelBuffer.readByte();
		channelBuffer.readBytes(superVisor);
		channelBuffer.readBytes(superVisorTel);
		channelBuffer.readBytes(superVisorEmail);
		KKLog.info("DOWN_WARN_MSG_URGE_TODO_REQ-----4.5.5.2.2报警督办请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1+2+8+4+8+1+this.superVisor.length+this.superVisorTel.length+this.superVisorEmail.length;
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

    public int getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(int supervisionId) {
        this.supervisionId = supervisionId;
    }

    public long getSupervisionEndTime() {
        return supervisionEndTime;
    }

    public void setSupervisionEndTime(long supervisionEndTime) {
        this.supervisionEndTime = supervisionEndTime;
    }

    public byte getSupervisionLevel() {
        return supervisionLevel;
    }

    public void setSupervisionLevel(byte supervisionLevel) {
        this.supervisionLevel = supervisionLevel;
    }

    public byte[] getSuperVisor() {
        return superVisor;
    }

    public void setSuperVisor(byte[] superVisor) {
        if (isByteArraySameSize(this.superVisor, superVisor)) {
            this.superVisor = superVisor;
        }
    }

    public byte[] getSuperVisorTel() {
        return superVisorTel;
    }

    public void setSuperVisorTel(byte[] superVisorTel) {
        if (isByteArraySameSize(this.superVisorTel, superVisorTel)) {
            this.superVisorTel = superVisorTel;
        }
    }

    public byte[] getSuperVisorEmail() {
        return superVisorEmail;
    }

    public void setSuperVisorEmail(byte[] superVisorEmail) {
        if (isByteArraySameSize(this.superVisorEmail, superVisorEmail)) {
            this.superVisorEmail = superVisorEmail;
        }
    }
}

package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;


/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownCloselinkInformReq   
 * 类描述：   
 ***************************** 
 * 4.5.1.16 上级平台主动关闭主从链路通知消息
 * 链路类型：主链路。
 * 消息方向：上级平台往下级平台。
 * 业务数据类型标识：DOWN_CLOSELINK_INFORM。
 * 描述：上级平台作为服务端，发现主链路出现异常时，上级平台通过主链路向下级平台
 *		发送本消息，通知下级平台上级平台即将关闭主从链路，其数据体规定见表16。本条消息
 *		无需被通知方应答。
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 上午10:14:54   
 * 修改人：   
 * 修改时间：
 * 修改备注：   
 * @version
 *    
 *
 */
public class CmdDownCloselinkInform extends CmdHead {
	private byte reasonCode ;//链路关闭原因{0x00：网关重启,0x01：其他原因}

    public CmdDownCloselinkInform(){
    }
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.reasonCode = channelBuffer.readByte();
		KKLog.info("DOWN_CLOSELINK_INFORM------4.5.1.16 上级平台主动关闭主从链路通知消息 ");
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.reasonCode);
	}

	@Override
	protected int getCmdBodySize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}
	
	public byte getReasonCode() {
		return reasonCode;
	}
	
	public void setReasonCode(byte reasonCode) {
		this.reasonCode = reasonCode;
	}

}

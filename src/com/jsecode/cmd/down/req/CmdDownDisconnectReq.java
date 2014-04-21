/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * 
 * 项目名称：gpsGW809 类名称：CmdDownDisconnectReq 类描述：
 ***************************** 
 * 4.5.1.11从链路注销请求消息 链路类型：从链路。 消息方向：上级平台往下级平台。 业务数据类型标识：DOWN_DISCONNECT_REQ。
 * 描述：从链路建立后，上级平台在取消该链路时，应向下级平台发送从链路注销请求消息
 ***************************** 
 * 创建人：zhaorg 创建时间：2014-2-17 上午10:12:54 修改人： 修改时间： 修改备注：
 * 
 * @version
 * 
 * 
 */
public class CmdDownDisconnectReq extends CmdHead {

	private int verifyCode;// 校验码,上级平台发送注销请求参数

	public CmdDownDisconnectReq() {
	}

	@Override
	protected int getCmdBodySize() {
		return 4;
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.verifyCode = channelBuffer.readInt();
		KKLog.info("DOWN_DISCONNECT_REQ------4.5.1.11从链路注销请求消息");
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.verifyCode);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(int verifyCode) {
		this.verifyCode = verifyCode;
	}

}

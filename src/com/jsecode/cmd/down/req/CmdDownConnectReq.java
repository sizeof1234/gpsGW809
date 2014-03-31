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
 * 项目名称：gpsGW809
 * 类名称：CmdDownConnectReq
 * 类描述：
 *****************************
 * 4.5.1.9从链路连接请求消息
 * 链路类型：从链路。
 * 消息方向：上级平台往下级平台。
 * 业务数据类型标识：DOWN_CONNECT_REQ。
 * 描述：业务数据类型标识：DOWN_CONNECT_REQ。
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
public class CmdDownConnectReq extends CmdHead {
	
	private int verifyCode;//对应的校验码(主链路登录应答返回)
	
	public CmdDownConnectReq() {
	}
	
	@Override
	protected int getCmdBodySize() {
		return 4;
	}

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.verifyCode = channelBuffer.readInt();
        KKLog.info("DOWN_CONNECT_REQ------4.5.1.9从链路连接请求消息 ");
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
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

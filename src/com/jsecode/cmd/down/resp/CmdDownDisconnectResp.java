/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.down.resp;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 *
 * 项目名称：gpsGW809
 * 类名称：CmdDownDisconnectResp
 * 类描述：
 *****************************
 * 4.5.1.12从链路注销应答消息
 * 链路类型：从链路。
 * 消息方向：下级平台往上级平台。
 * 业务数据类型标识：DOWN_DISCONNECT_RSP。
 * 描述：下级平台在收到上级平台发送的从链路注销请求消息后，返回从链路注销应答消息，记录相关日志，中断该从链路
 *       从链路注销应答消息，数据体为空
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
public class CmdDownDisconnectResp extends CmdHead {
	
    public CmdDownDisconnectResp() {
	}
	
	@Override
	protected int getCmdBodySize() {
		return 0;
	}
	
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
        KKLog.info("DOWN_DISCONNECT_RSP------4.5.1.12从链路注销应答消息");
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

}

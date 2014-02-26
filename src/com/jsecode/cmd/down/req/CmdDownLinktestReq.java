package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownLinktestReq   
 * 类描述：   
 ***************************** 
 * 4.5.1.13从链路连接保持请求消息
 * 链路类型：从链路。 
 * 消息方向：上级平台往下级平台。 
 * 业务数据类型标识：DOWN_LINKTEST_REQ。 
 * 描述：从链路建立成功后，上级平台向下级平台发送从链路连接保持请求消息，以保持从链路的连接状态。
 *      从链路连接保持请求消息，数据体为空。 
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 上午10:20:25   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version
 *    
 *
 */
public class CmdDownLinktestReq extends CmdHead {

	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		KKLog.info("DOWN_LINKTEST_REQ------4.5.1.13从链路连接保持请求消息");
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
	}

	@Override
	protected int getCmdBodySize() {
		return 0;
	}

}

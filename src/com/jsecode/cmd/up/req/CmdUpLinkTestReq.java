/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

/**
 * 主链路连接保持请求消息
 * 
 * 描述：下级平台向上级平台发送主链路连接保持请求消息，以保持主链路的连接
 * @author Jadic
 */
public class CmdUpLinkTestReq extends CmdHead {

	public CmdUpLinkTestReq() {
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
	}

}

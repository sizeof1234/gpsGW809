/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.up.resp;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHead;

public class CmdUpDisconnectResp extends CmdHead {

	public CmdUpDisconnectResp() {
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

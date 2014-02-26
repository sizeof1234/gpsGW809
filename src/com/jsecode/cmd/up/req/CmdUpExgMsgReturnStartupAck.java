/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 启动车辆定位信息交换应答消息
 * 本条消息是下级平台对上级平台下发的DOWN_EXG_MSG_RETURN_STARTUP 消息的应答消息
 * @author Jadic
 */
public class CmdUpExgMsgReturnStartupAck extends CmdHeadSubBizWithCar {

	public CmdUpExgMsgReturnStartupAck() {
		
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 0;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

}

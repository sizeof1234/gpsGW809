/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 结束车辆定位信息交换应答消息
 * 本条消息是下级平台对上级平台服务器端下发的DOWN_EXG_MSG_RETURN_END 消息的应答消息
 * @author Jadic
 */
public class CmdUpExgMsgReturnEndAck extends CmdHeadSubBizWithCar {

	public CmdUpExgMsgReturnEndAck() {
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

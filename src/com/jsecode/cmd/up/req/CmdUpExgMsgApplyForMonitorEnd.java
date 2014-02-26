/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 取消交换指定车辆定位信息请求消息
 * 描述：下级平台上传该命令给上级平台，取消之前申请监控的特殊车辆
 * @author Jadic
 */
public class CmdUpExgMsgApplyForMonitorEnd extends CmdHeadSubBizWithCar {

	public CmdUpExgMsgApplyForMonitorEnd() {
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

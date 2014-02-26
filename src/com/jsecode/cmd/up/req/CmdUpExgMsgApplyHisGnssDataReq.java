/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 补发车辆定位信息请求消息
 * 
 * 描述：在平台间传输链路中断并重新建立连接后，下级平台向上级平台请求中断期间
 * 内上级平台需交换至下级平台的车辆定位信息时，向上级平台发出补发车辆定位信息
 * 请求，上级平台对请求应答后进行“补发车辆定位信息”
 * @author Jadic
 */
public class CmdUpExgMsgApplyHisGnssDataReq extends CmdHeadSubBizWithCar {
	
	private long startTime;	//开始时间 utc
	private long endTime;	//结束时间

	public CmdUpExgMsgApplyHisGnssDataReq() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 8 + 8;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeLong(this.startTime);
		channelBuffer.writeLong(this.endTime);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

}

/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 申请交换指定车辆定位信息请求消息
 * 
 * 当下级平台需要在特定时间段内监控特殊车辆时，可上传此命令到上级平台申请对
 * 该车辆定位数据交换到下级平台，申请成功后，此车辆定位数据将在指定时间内交
 * 换到该平台（即使该车没有进入该平台所属区域也会交换）
 * @author Jadic
 */
public class CmdUpExgMsgApplyForMonitorStartup extends CmdHeadSubBizWithCar {
	
	private long startTime;	//开始时间 utc
	private long endTime;	//结束时间

	public CmdUpExgMsgApplyForMonitorStartup() {
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 8 + 8;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.startTime = channelBuffer.readLong();
		this.endTime = channelBuffer.readLong();
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeLong(startTime);
		channelBuffer.writeLong(endTime);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
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

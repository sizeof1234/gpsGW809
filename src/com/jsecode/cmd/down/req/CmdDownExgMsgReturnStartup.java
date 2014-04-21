package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 * 
 * 项目名称：gpsGW809 类名称：CmdDownExgMsgReturnStartup 类描述：
 ***************************** 
 * 4.5.3.2.5启动车辆定位信息交换请求消息 子业务类型标识：DOWN_EXG_MSG_RETURN_STARTUP
 * 描述：在有车辆进入非归属地区地理区域、人工指定车辆定位信息交换和应急状态监控
 * 车辆时，上级平台向下级平台发出启动车辆定位信息交换请求消息。下级平台收到此命令后 需要回复启动车辆定位信息交换应答消息给上级平台，即
 * UP_EXG_MSG_RETURN_STARTUP_ACK
 * 
 * 
 ***************************** 
 * 创建人：zhaorg 创建时间：2014-2-17 下午02:30:56 修改人： 修改时间： 修改备注：
 * 
 * @version
 * 
 * 
 */
public class CmdDownExgMsgReturnStartup extends CmdHeadSubBizWithCar {

	// 启动车辆交换信息原因{0x00 车辆进入指定区域,0x01 人工指定交换,0x02 应急状态下车辆定位信息回传,0x03 其他原因}
	private byte reasonCode;

	public CmdDownExgMsgReturnStartup() {
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.reasonCode = channelBuffer.readByte();
		KKLog.info("DOWN_EXG_MSG_RETURN_STARTUP-----------4.5.3.2.5启动车辆定位信息交换请求消息 ");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.reasonCode);
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public byte getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(byte reasonCode) {
		this.reasonCode = reasonCode;
	}

}

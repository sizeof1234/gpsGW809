package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgApplyForMonitorEndAck   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.8取消申请交换指定车辆定位信息应答消息
 * 子业务类型标识：DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK。
 * 描述：应答下级平台取消申请交换指定车辆定位信息请求消息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午03:00:34   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownExgMsgApplyForMonitorEndAck extends CmdHeadSubBizWithCar {
	private byte result;// 取消申请交换指定车辆定位信息结果{0x00 取消申请成功,0x01 之前没有对应的申请信息,0x02  其他原因}
	
	public CmdDownExgMsgApplyForMonitorEndAck(){
	}
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.result = channelBuffer.readByte();
		KKLog.info("DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK------4.5.3.2.8取消申请交换指定车辆定位信息应答消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }
}

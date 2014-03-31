package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgApplyForMonitorStartupAck   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.7申请交换指定车辆定位信息应答消息
 * 子业务类型标识：DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK。 
 * 描述：应答下级平台申请交换指定车辆定位信息请求消息，即 P_EXG_MSG_APPLY_FOR_MONITOR_STARTUP。其数据体规定见表35。
 * 
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午02:50:15   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownExgMsgApplyForMonitorStartupAck extends
		CmdHeadSubBizWithCar {
	private byte result;// 申请交换指定车辆定位结果{0x00 申请成功,0x01 上级平台没有该车数据,0x02申请时间段错误,0x03 其他}
	
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.result = channelBuffer.readByte();
		KKLog.info("DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK------4.5.3.2.7申请交换指定车辆定位信息应答消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }
}

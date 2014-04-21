package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownCtrlMsgMonitorVehicleReq   
 * 类描述：   
 ***************************** 
 * 4.5.6.2.2车辆单向监听请求消息
 * 子业务类型标识：DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ
 * 描述：上级平台向下级平台下发车辆单向监听请求消息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:23:33   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownCtrlMsgMonitorVehicleReq extends CmdHeadSubBizWithCar {

	private byte[] monitorTel;//回拨电话号码
	
	public CmdDownCtrlMsgMonitorVehicleReq() {
		this.monitorTel = new byte[20];
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.readBytes(monitorTel);
		KKLog.info("DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ------4.5.6.2.2车辆单向监听请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeBytes(this.monitorTel);
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return this.monitorTel.length;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}
	
    public byte[] getMonitorTel() {
        return monitorTel;
    }

    public void setMonitorTel(byte[] monitorTel) {
        if (isByteArraySameSize(this.monitorTel, monitorTel)) {
            this.monitorTel = monitorTel;
        }
    }
}

package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownBaseMsgVehicleAdded   
 * 类描述：   
 ***************************** 
 * 4.5.7.2.2补报车辆静态信息请求消息
 * 子业务类型标识：DOWN_BASE_MSG_VEHICLE_ADDED
 * 描述：上级平台在接收到车辆定位信息后，发现该车辆静态信息在上级平台不存在，上级平台向下级平台下发补报该车辆静态信息的请求消息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:51:57   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownBaseMsgVehicleAdded extends CmdHeadSubBizWithCar {

	public CmdDownBaseMsgVehicleAdded() {
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		KKLog.info("DOWN_BASE_MSG_VEHICLE_ADDED------4.5.7.2.2补报车辆静态信息请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 0;
	}

}

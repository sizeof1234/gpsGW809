package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgTakeEwaybillReq   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.11上报车辆电子运单请求消息(请结合word查看)
 * 子业务类型标识：DOWN_EXG_MSG_TAKE_EWAYBILL_REQ。 
 * 描述：上级平台向下级平台下发上报车辆当前电子运单的请求消息
 * 
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午03:25:05   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownExgMsgTakeEwaybillReq extends CmdHeadSubBizWithCar {


	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		KKLog.info("DOWN_EXG_MSG_TAKE_EWAYBILL_REQ------4.5.3.2.11上报车辆电子运单请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 0;
	}

}

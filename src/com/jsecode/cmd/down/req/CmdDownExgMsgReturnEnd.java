package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgReturnEnd   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.6结束车辆定位信息交换请求消息
 * 子业务类型标识：DOWN_EXG_MSG_RETURN_END。 
 *	描述：在进入非归属地区地理区域的车辆离开该地理区域、人工取消指定车辆定位信息
 *		   交换和应急状态结束时，上级平台向下级平台发出结束车辆定位信息交换请求消息。下级平
 *		   台收到该命令后应回复结束车辆定位信息交换应答消息，即 UP_EXG_MSG_RETURN_END_ACK。
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午02:43:20   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version
 *    
 *
 */
public class CmdDownExgMsgReturnEnd extends CmdHeadSubBizWithCar {

	private byte reasonCode;// 启动车辆交换信息原因{0x00 车辆进入指定区域,0x01 人工指定交换,0x02 应急状态下车辆定位信息回传,0x03 其他原因}
	
	public CmdDownExgMsgReturnEnd(){ 
	}
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.reasonCode = channelBuffer.readByte();
		KKLog.info("DOWN_EXG_MSG_RETURN_END------4.5.3.2.6结束车辆定位信息交换请求消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

    public byte getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(byte reasonCode) {
        this.reasonCode = reasonCode;
    }
}

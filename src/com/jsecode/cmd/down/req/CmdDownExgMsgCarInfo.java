package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;
/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgCarInfo   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.4交换车辆静态信息消息
 * 子业务类型标识：DOWN_EXG_MSG_CAR_INFO
 * 描述：在首次启动跨域车辆定位信息交换，或者以后交换过程中车辆静态信息有更新时，
 *		由上级平台向下级平台下发一次车辆静态信息。下级平台接收后自行更新该车辆静态信息，
 *		本条消息客户端无需应答
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午02:30:03   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownExgMsgCarInfo extends CmdHeadSubBizWithCar{
	private byte[] carInfo;      // 数据部分

    public  CmdDownExgMsgCarInfo(){
        carInfo = ZERO_BYTES;
    }
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.carInfo = new byte[this.getSubDataSize()];
        channelBuffer.readBytes(carInfo);
        KKLog.info("DOWN_EXG_MSG_CAR_INFO------4.5.3.2.4交换车辆静态信息消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return this.carInfo.length;
	}

    public byte[] getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(byte[] carInfo) {
        if (carInfo != null) {
            this.carInfo = carInfo;
        }
    }
}

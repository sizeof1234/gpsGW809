package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.cmd.bean.GpsCmdBean;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgCarLocation   
 * 类描述：   
 ***************************** 
 *  4.5.3.2.2交换车辆定位信息消息
 *  链路类型：从链路。 
 *	子业务类型标识：DOWN_EXG_MSG_CAR_LOCATION。 
 * 	 描述：上级平台在以下四种情况下通过该消息不间断向下级平台发送车辆定位信息。
 *		(1)  车辆跨域时，上级平台通过该消息不间断地向车辆进入区域所属的下级平台发送车辆定
 *		位信息，直到该车辆离开该区域；
 *		(2)  人工指定车辆定位信息交换时，上级平台通过该消息不间断地向指定交换对象下级平台
 *		发送车辆定位信息，直到人工指定交换车辆定位信息结束；
 *		(3)  下级平台向上级平台申请交换指定车辆定位信息成功后，上级平台通过该消息不间断地
 *		向交换对象下级平台发送车辆定位信息，直到申请交换指定车辆定位信息结束；
 *		(4)  应急状态监控车辆时，上级平台向车辆归属下级平台通过该消息不间断地发送车辆定位
 *		信息，实现车辆定位信息回传。
 * 
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 上午11:15:08   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version
 *    
 *
 */
public class CmdDownExgMsgCarLocation extends CmdHeadSubBizWithCar{

	private GpsCmdBean gnssData;    // 数据部分
	
	public   CmdDownExgMsgCarLocation() {
		gnssData = EMPTY_GPS_BEAN;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		gnssData.disposeData(channelBuffer);
		KKLog.info("DOWN_EXG_MSG_CAR_LOCATION------4.5.3.2.2交换车辆定位信息消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		this.gnssData.fillChannelBuffer(channelBuffer);
	}
	@Override
	protected int getCmdSubBizDataSize() {
		return this.gnssData.getBeanSize();
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public GpsCmdBean getGnssData() {
        return gnssData;
    }

    public void setGnssData(GpsCmdBean gnssData) {
    	if (gnssData != null) {
    		this.gnssData = gnssData;
    	}
    }
}

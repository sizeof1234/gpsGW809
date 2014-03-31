package com.jsecode.cmd.down.req;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.cmd.bean.GpsCmdBean;
import com.jsecode.utils.KKLog;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgHistoryArcossarea   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.3车辆定位信息交换补发消息
 * 子业务类型标识：DOWN_EXG_MSG_HISTORY_ARCOSSAREA
 * 描述：本业务在DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK应答成功后，立即开始交换
 * 如果申请失败，则不进行数据转发。本条消息下级平台无需应答
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午01:52:42   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version
 *    
 *
 */
public class CmdDownExgMsgHistoryArcossarea extends CmdHeadSubBizWithCar {

	private byte gnssCnt;         		// 数据个数1<=gnssCnt<=5
	private List <GpsCmdBean> gnssDataList;// 数据部分
	//private GpsCmdBean gpsData;
	
	public CmdDownExgMsgHistoryArcossarea(){
		gnssDataList = new ArrayList<GpsCmdBean>();
	}
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.gnssCnt = channelBuffer.readByte();
		//根据gnssCnt 个数，判断数据体列表个数，放入list中
		for(int i = 0 ; i<gnssCnt;i++){
			GpsCmdBean gpsData = new GpsCmdBean();
			gpsData.disposeData(channelBuffer);
			gnssDataList.add(gpsData);
		}
		KKLog.info("DOWN_EXG_MSG_HISTORY_ARCOSSAREA------4.5.3.2.3车辆定位信息交换补发消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1 + GpsCmdBean.getCmdBeanSize() * gnssDataList.size();
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public byte getGnssCnt() {
        return gnssCnt;
    }

    public void setGnssCnt(byte gnssCnt) {
        this.gnssCnt = gnssCnt;
    }

    public List<GpsCmdBean> getGnssDataList() {
        return gnssDataList;
    }

    public void setGnssDataList(List<GpsCmdBean> gnssDataList) {
        this.gnssDataList = gnssDataList;
    }
}

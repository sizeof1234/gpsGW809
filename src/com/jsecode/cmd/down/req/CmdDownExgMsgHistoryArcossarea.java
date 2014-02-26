package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithoutCar;
import com.jsecode.cmd.bean.GpsBean;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

import java.util.ArrayList;
import java.util.List;

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
public class CmdDownExgMsgHistoryArcossarea extends CmdHeadSubBizWithoutCar {

	private byte gnssCnt;         		// 数据个数1<=gnssCnt<=5
	private List <GpsBean> gnssDataList;// 数据部分
	private GpsBean gnssData;
	
	public CmdDownExgMsgHistoryArcossarea(){
		gnssDataList = new ArrayList<GpsBean>();
	}
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.gnssCnt = channelBuffer.readByte();
		//根据gnssCnt 个数，判断数据体列表个数，放入list中
		for(int i = 0 ; i<gnssCnt;i++){
			gnssData = new GpsBean();
			gnssData.disposeData(channelBuffer);
			gnssDataList.add(gnssData);
		}
		KKLog.info("DOWN_EXG_MSG_HISTORY_ARCOSSAREA------4.5.3.2.3车辆定位信息交换补发消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1+this.gnssData.getBeanSize()*gnssDataList.size();
	}

    public byte getGnssCnt() {
        return gnssCnt;
    }

    public void setGnssCnt(byte gnssCnt) {
        this.gnssCnt = gnssCnt;
    }

    public List<GpsBean> getGnssDataList() {
        return gnssDataList;
    }

    public void setGnssDataList(List<GpsBean> gnssDataList) {
        this.gnssDataList = gnssDataList;
    }

    public GpsBean getGnssData() {
        return gnssData;
    }

    public void setGnssData(GpsBean gnssData) {
        this.gnssData = gnssData;
    }
}

/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.cmd.bean.GpsCmdBean;

/**
 * 实时上传车辆定位信息消息
 * 
 * 描述：主要描述车辆的实时定位信息,本条消息服务端无需应答
 * @author Jadic
 */
public class CmdUpExgMsgRealLocationReq extends CmdHeadSubBizWithCar {
	
	private GpsCmdBean gpsBean;

	public CmdUpExgMsgRealLocationReq() {
		gpsBean = EMPTY_GPS_BEAN;
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return gpsBean.getBeanSize();
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		gpsBean.fillChannelBuffer(channelBuffer);
	}

	public GpsCmdBean getGpsBean() {
		return gpsBean;
	}

	public void setGpsBean(GpsCmdBean gpsBean) {
		if (gpsBean != null) {
			this.gpsBean = gpsBean;
		}
	}

}

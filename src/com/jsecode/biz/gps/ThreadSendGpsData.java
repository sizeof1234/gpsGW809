/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.biz.gps;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jsecode.IGW809;
import com.jsecode.bean.GpsBean;
import com.jsecode.bean.TerminalBean;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.cmd.up.req.CmdUpExgMsgHistoryLocationReq;
import com.jsecode.cmd.up.req.CmdUpExgMsgRealLocationReq;
import com.jsecode.cmd.up.req.CmdUpExgMsgRegisterReq;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.GlobalVar;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;

public class ThreadSendGpsData extends AbstractThreadSendData<GpsBean> implements ISendGpsData<GpsBean> {
	
	private SysParams sysParams;
	private final Map<String, Long> vehicleNoTimeMap;
	private final Date dbLastUpdateSysTime;
	private long gpsDataSendCount = 0L;
	private final Queue<GpsBean> queueForLinkDisconnected;//链路断开时GPS数据转入该队列

	//断开链路后重连成功后，发送数据过程发现链路断开，则保留最后发送的GPS数据，等待再次发送
	private final List<GpsBean> gpsList;
	
	final static int QUEUE_LIMIT_SIZE = 100000;
	
	final static long MILLISECONDS_ONE_DAY = 1 * 24 * 60 * 60 * 1000L;
	
	public ThreadSendGpsData(IGW809 gw809) {
		super(gw809, ThreadSendGpsData.class.getName());
		this.queueForLinkDisconnected = new ConcurrentLinkedQueue<GpsBean>();
		this.gpsList = new ArrayList<GpsBean>();
		this.vehicleNoTimeMap = new HashMap<String, Long>();
		this.dbLastUpdateSysTime = new Date(0);
		sysParams = SysParams.getInstance();
	}

	@Override
	public void run() {
		GpsBean gpsBean = null;
		while (!isInterrupted()) {
			sendHisGpsData();
			
			boolean isLinkConnected = true;
			while((gpsBean = getQueuePollData()) != null) {
				if (!isVehicleNoValid(gpsBean) || isDataRepeated(gpsBean) 
					|| !isLonLatValid(gpsBean.getLon(), gpsBean.getLat())
					|| !isGpsTimeValid(gpsBean)) {
					continue;
				}
				
				isLinkConnected = sendGpsData(gpsBean);
				if (isLinkConnected) {
					this.gpsDataSendCount ++;
					if (this.gpsList.size() > 0 || this.queueForLinkDisconnected.size() > 0) {
						sendHisGpsData();//send his gps data when link connected
					}
				} else {
					if (this.queueForLinkDisconnected.size() >= QUEUE_LIMIT_SIZE) {
						this.queueForLinkDisconnected.poll();
					}
					this.queueForLinkDisconnected.offer(gpsBean);
				}
			}
			
			if (isLinkConnected) {
				waitNewData();
			} else {
				KKLog.info("thread-send-gps-data detected the main and sub link disconnected, please check ...");
				waitLinkConnected();
			}
		}
	}
	
	/**
	 * 补报车辆定位信息,按协议要求最多每5条一发
	 */
	private void sendHisGpsData() {
		if (gpsList.size() > 0) {
			if (sendGpsDataList(gpsList)) {
				this.gpsDataSendCount += gpsList.size();
				gpsList.clear();
			} else {
				return;
			}
		}
		GpsBean gpsBean = null;
		boolean isLinkDisconnected = false;
		while((gpsBean = queueForLinkDisconnected.poll()) != null) {
			gpsList.add(gpsBean);
			if (gpsList.size() >= 5) {
				if (sendGpsDataList(gpsList)) {
					this.gpsDataSendCount += gpsList.size();
					gpsList.clear();
				} else {
					isLinkDisconnected = true;
					break;
				}
			}
		}
		
		if (!isLinkDisconnected) {
			sendGpsDataList(gpsList);
		}
	}
	
	/**
	 * 补发GPS数据列表
	 * @param list
	 * @return
	 */
	private boolean sendGpsDataList(List<GpsBean> list) {
		if (list == null || list.size() <= 0) {
			return false;
		}
		IMainSubLink mLink = getMainLink(true);
		if (mLink.isChannelConnected()) {
			CmdUpExgMsgHistoryLocationReq cmdUpExgMsgHistoryLocationReq = new CmdUpExgMsgHistoryLocationReq();
			cmdUpExgMsgHistoryLocationReq.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgHistoryLocationReq.setSubMsgId(Const.UP_EXG_MSG_HISTORY_LOCATION);
			
			//TODO set vehicle info to empty
			
			for (GpsBean gpsBean : gpsList) {
				cmdUpExgMsgHistoryLocationReq.addGpsData(gpsBean.getGpsCmdBean());
			}
			return mLink.sendData(cmdUpExgMsgHistoryLocationReq.getSendBuffer()) != null;
		}
		return false;
	}
	
	/**
	 * 全球经纬度的取值范围为：纬度-90~90，经度-180~180
	 * 中国的经纬度范围大约为：纬度3.86~53.55，经度73.66~135.05
	 * @param lon
	 * @param lat
	 * @return
	 */
	private boolean isLonLatValid(double lon, double lat) {
		return lon > 45 && lon < 145 && lat > 0 && lat < 90;
	}
	
	private boolean isVehicleNoValid(GpsBean gpsBean) {
		if (gpsBean != null && !KKTool.isStrNullOrBlank(gpsBean.getVehicleNo())) {
			int vehicleNoGBKSize = KKTool.toGBKBytes(gpsBean.getVehicleNo()).length;
			return vehicleNoGBKSize >= sysParams.getMinVehicleNoGBKSize() && vehicleNoGBKSize <= sysParams.getMaxVehicleNoGBKSize();
		}
		return false;
	}
	
	/**
	 * GPS时间是否太过超前<br>
	 * 默认不超过当前服务器时间1天的都算合法数据 
	 * @param gpsBean
	 * @return
	 */
	private boolean isGpsTimeValid(GpsBean gpsBean) {
		boolean isValid = false;
		if (gpsBean != null) {
			long gpsTime = gpsBean.getGpsTime().getTime();
			isValid = gpsTime - System.currentTimeMillis() < MILLISECONDS_ONE_DAY;
			if (!isValid) {
				StringBuilder sBuilder = new StringBuilder("gps time is not valid:");
				sBuilder.append(gpsBean.getVehicleNo());
				sBuilder.append(",");
				sBuilder.append(KKTool.getFormatDateTime(gpsBean.getGpsTime()));
				KKLog.warn(sBuilder.toString());
			}
		}
		return isValid;
	}
	
	/**
	 * 该定位数据是否是重复数据<p>
	 * 检测车牌号和GPS时间
	 * @param gpsBean
	 * @return
	 */
	private boolean isDataRepeated(GpsBean gpsBean) {
		this.setDBLastUpdateSysTime(gpsBean.getDbSysTime());
		String vehicleNo = gpsBean.getVehicleNo();
		Long currGpsTime = gpsBean.getGpsTime().getTime();
		Long oldTime = this.vehicleNoTimeMap.put(vehicleNo, currGpsTime);
		if (oldTime == null) {//map doesn't contain this vehicle, send vehicle register info
			TerminalBean terminal = GlobalVar.terminalMap.get(vehicleNo);
			if (terminal != null) {
				sendVehicleRegisterInfo(terminal);
			}
			return false;
		} else {
			return currGpsTime == oldTime;
		}
	}
	
	private void setDBLastUpdateSysTime(Date sysTime) {
		if (sysTime != null && this.dbLastUpdateSysTime.before(sysTime)) {
			this.dbLastUpdateSysTime.setTime(sysTime.getTime());
		}
	}
	
	/**
	 * 发送车辆注册信息
	 * @param terminal
	 * @return
	 */
	private boolean sendVehicleRegisterInfo(TerminalBean terminal) {
		//KKLog.info("sending vehicle register info:" + terminal.getHostNo());
		IMainSubLink link = getMainLink(true);
		if (link.isChannelConnected()) {
			CmdUpExgMsgRegisterReq cmdUpExgMsgRegisterReq = new CmdUpExgMsgRegisterReq();
			cmdUpExgMsgRegisterReq.setSubMsgId(Const.UP_EXG_MSG_REGISTER);
			cmdUpExgMsgRegisterReq.setMsgFlagId(Const.UP_EXG_MSG);
			
			byte[] vehicleNo = KKTool.toFixedLenGBKBytes(terminal.getHostNo(), 21);
			byte[] producerId = KKTool.getFixedLenBytes(terminal.getProducerId(), 11);
			byte[] terminalModelType = KKTool.getFixedLenBytes(terminal.getModelType(), 20);
			byte[] terminalId = KKTool.getFixedLenBytes(terminal.getId(), 7, '0', true, false);
			byte[] simNo = KKTool.getFixedLenBytes(terminal.getSimNo(), 12, '0', true);
			byte[] platformId = KKTool.toFixedLenGBKBytes(String.valueOf(sysParams.getGnssCenterId()), 11);
			cmdUpExgMsgRegisterReq.setVehicleNo(vehicleNo);
			cmdUpExgMsgRegisterReq.setVehicleColor(terminal.getHostPlateColor());
			cmdUpExgMsgRegisterReq.setPlatFormId(platformId);
			cmdUpExgMsgRegisterReq.setProducerId(producerId);
			cmdUpExgMsgRegisterReq.setTerminalModelType(terminalModelType);
			cmdUpExgMsgRegisterReq.setTerminalId(terminalId);
			cmdUpExgMsgRegisterReq.setTerminalSimCode(simNo);
			
			return link.sendData(cmdUpExgMsgRegisterReq.getSendBuffer()) != null;
		}
		return false;
	}
	
	/**
	 * 发送实时定位数据
	 * @param gpsBean
	 * @return true:链路未断开
	 */
	private boolean sendGpsData(GpsBean gpsBean) {
		IMainSubLink link = getMainLink(true);
		if (link.isChannelConnected()) {
			CmdUpExgMsgRealLocationReq cmdUpExgMsgRealLocationReq = new CmdUpExgMsgRealLocationReq();
			cmdUpExgMsgRealLocationReq.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgRealLocationReq.setSubMsgId(Const.UP_EXG_MSG_REAL_LOCATION);
			byte[] vehicleNo = KKTool.toFixedLenGBKBytes(gpsBean.getVehicleNo(), 21);
			cmdUpExgMsgRealLocationReq.setVehicleNo(vehicleNo);
			cmdUpExgMsgRealLocationReq.setVehicleColor(gpsBean.getPlateColor());
			cmdUpExgMsgRealLocationReq.setGpsBean(gpsBean.getGpsCmdBean());
			return link.sendData(cmdUpExgMsgRealLocationReq.getSendBuffer()) != null;
		}
		return false;
	}

	@Override
	public Date getDBLastUpdateSysTime() {
		return this.dbLastUpdateSysTime;
	}
	
	public long getGpsDataSendCount() {
		return this.gpsDataSendCount;
	}

	@Override
	public int getUnSendGpsDataCount() {
		return this.queueForLinkDisconnected.size() + this.gpsList.size();
	}
	
}

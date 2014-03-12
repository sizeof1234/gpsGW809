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
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.cmd.bean.GpsBean;
import com.jsecode.cmd.bean.TerminalBean;
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
	private Object objLinkConnected = new Object();
	private final Map<String, Long> vehicleNoTimeMap;
	private final Date dbLastUpdateSysTime;
	private long gpsDataSendCount = 0L;
	private final Queue<GpsBean> queueForLinkDisconnected;//链路断开时GPS数据转入该队列

	//断开链路后重连成功后，发送数据过程发现链路断开，则保留最后发送的GPS数据，等待再次发送
	private final List<GpsBean> gpsList;
	
	final static int QUEUE_LIMIT_SIZE = 100000;
	
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
			
			boolean isLinkConnected = false;
			while((gpsBean = queue.peek()) != null) {
				if (!isVehicleNoValid(gpsBean) || isDataRepeated(gpsBean) || !isLonLatValid(gpsBean.getLon(), gpsBean.getLat())) {
					this.queue.poll();
					continue;
				}
				
				isLinkConnected = sendGpsData(gpsBean);
				if (isLinkConnected) {
					this.gpsDataSendCount ++;
					this.queue.poll();
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
		if (!sendGpsDataList(gpsList)) {
			return;
		}
		GpsBean gpsBean = null;
		boolean isLinkDisconnected = false;
		while((gpsBean = queueForLinkDisconnected.poll()) != null) {
			gpsList.add(gpsBean);
			if (gpsList.size() >= 5) {
				if (sendGpsDataList(gpsList)) {
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
	
	private boolean isLonLatValid(double lon, double lat) {
		return lon > 70 && lon < 135 && lat > 0 && lat < 120;
	}
	
	private boolean isVehicleNoValid(GpsBean gpsBean) {
		if (gpsBean != null && !KKTool.isStrNullOrBlank(gpsBean.getVehicleNo())) {
			int vehicleNoGBKSize = KKTool.toGBKBytes(gpsBean.getVehicleNo()).length;
			return vehicleNoGBKSize >= sysParams.getMinVehicleNoGBKSize() && vehicleNoGBKSize <= sysParams.getMaxVehicleNoGBKSize();
		}
		return false;
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
			cmdUpExgMsgRegisterReq.setMsgFlagId(Const.UP_EXG_MSG);
			cmdUpExgMsgRegisterReq.setSubMsgId(Const.UP_EXG_MSG_REGISTER);
			
			byte[] vehicleNo = KKTool.getFixedLenString(terminal.getHostNo(), 21, Const.BLANK_CHAR, false).getBytes();
			byte[] producerId = KKTool.getFixedLenString(terminal.getProducerId(), 11, Const.BLANK_CHAR, false).getBytes();
			byte[] terminalModelType = KKTool.getFixedLenString(terminal.getModelType(), 20, Const.BLANK_CHAR, false).getBytes();
			byte[] terminalId = KKTool.getFixedLenString(terminal.getId(), 7, '0', true).getBytes();
			byte[] simNo = KKTool.getFixedLenString(terminal.getSimNo(), 12, '0', true).getBytes();
			cmdUpExgMsgRegisterReq.setVehicleNo(vehicleNo);
			cmdUpExgMsgRegisterReq.setVehicleColor(terminal.getHostPlateColor());
			cmdUpExgMsgRegisterReq.setPlatFormId(null);
			cmdUpExgMsgRegisterReq.setProducerId(producerId);
			cmdUpExgMsgRegisterReq.setTerminalModelType(terminalModelType);
			cmdUpExgMsgRegisterReq.setTerminalId(terminalId);
			cmdUpExgMsgRegisterReq.setTerminalSimCode(simNo);
			
			return link.sendData(cmdUpExgMsgRegisterReq.getSendBuffer()) != null;
		}
		return false;
	}
	
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

	public void noticeLinkConnected() {
		synchronized (objLinkConnected) {
			objLinkConnected.notify();
		}
	}
	
	private void waitLinkConnected() {
		synchronized (objLinkConnected) {
			try {
				objLinkConnected.wait(3 * 1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	
	@Override
	public Date getDBLastUpdateSysTime() {
		return this.dbLastUpdateSysTime;
	}
	
	public long getGpsDataSendCount() {
		return this.gpsDataSendCount;
	}
}

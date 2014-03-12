/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.cmd.bean;

import java.util.Calendar;
import java.util.Date;

import com.jsecode.utils.Const;

/**
 * 加载数据库采用该Gps类封装数据
 */
public class GpsBean {
	
	private int hostId;		//车辆唯一标识
	private String vehicleNo;	//车牌号
	private byte plateColor;	//车牌颜色
	
	private Date gpsTime;
	private double lon;
	private double lat;
	private short speed;
	private short direction;
	private Date dbSysTime;

	public GpsBean() {
		vehicleNo = Const.EMPTY_STR;
	}
	
	public GpsCmdBean getGpsCmdBean() {
		GpsCmdBean gpsCmdBean = new GpsCmdBean();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(this.gpsTime.getTime());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		byte[] date = gpsCmdBean.getDate();
		byte[] time = gpsCmdBean.getTime();
		date[0] = (byte)day;
		date[1] = (byte)month;
		date[2] = (byte)(year >> 8);
		date[3] = (byte)(year & 0xFF);
		time[0] = (byte)hour;
		time[1] = (byte)minute;
		time[2] = (byte)second;
		gpsCmdBean.setLon((int)(this.lon * 1000000));
		gpsCmdBean.setLat((int)(this.lat * 1000000));
		gpsCmdBean.setSpeed(this.speed);
		gpsCmdBean.setDirection(this.direction);
		gpsCmdBean.setState(3);
		return gpsCmdBean;
	}

	public int getHostId() {
		return hostId;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		if (vehicleNo != null) {
			this.vehicleNo = vehicleNo;
		}
	}

	public byte getPlateColor() {
		return plateColor;
	}

	public void setPlateColor(byte plateColor) {
		this.plateColor = plateColor;
	}

	public Date getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(Date gpsTime) {
		this.gpsTime = gpsTime;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public short getDirection() {
		return direction;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}

	public Date getDbSysTime() {
		return dbSysTime;
	}

	public void setDbSysTime(Date dbSysTime) {
		this.dbSysTime = dbSysTime;
	}

}

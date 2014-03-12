/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.bean;

import java.util.Date;

/**
 * 申请交换指定车辆定位信息
 */
public class ApplyForMonitorStartupBean implements IApplyForMonitorBean {
	
	private String vehicleNo;
	private Date startTime;
	private Date endTime;
	
	public ApplyForMonitorStartupBean() {
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}

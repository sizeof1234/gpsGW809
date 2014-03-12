/**
 * @author 	Jadic
 * @created 2014-3-10
 */
package com.jsecode.bean;

import com.jsecode.utils.Const;

public class DriverBean {

	private String name;		//驾驶员姓名
	private String id;			//身份证编号
	private String vehicleNo;	//车辆编号
	private String licence;		//从业资格证号
	private String orgName;		//发证机构名称
	
	public DriverBean() {
		name = Const.EMPTY_STR;
		id = Const.EMPTY_STR;
		vehicleNo = Const.EMPTY_STR;
		licence = Const.EMPTY_STR;
		orgName = Const.EMPTY_STR;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null) {
			this.id = id;
		}
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		if (vehicleNo != null) {
			this.vehicleNo = vehicleNo;
		}
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		if (licence != null) {
			this.licence = licence;
		}
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		if (orgName != null) {
			this.orgName = orgName;
		}
	}

}

/**
 * @author 	Jadic
 * @created 2014-2-28
 */
package com.jsecode.bean;

import com.jsecode.utils.Const;

/**
 * 终端基础信息
 */
public class TerminalBean {
	
	private String id;				//终端ID
	private String hostNo;			//车牌号
	private byte hostPlateColor;	//车牌颜色
	private String simNo;			//SIM卡号
	private String producerId;		//制造商ID
	private String modelType;		//终端型号
	

	public TerminalBean() {
		this.id = Const.EMPTY_STR;
		this.hostNo = Const.EMPTY_STR;
		this.simNo = Const.EMPTY_STR;
		this.producerId = Const.EMPTY_STR;
		this.modelType = Const.EMPTY_STR;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		if (id != null) {
			this.id = id;
		}
	}


	public String getHostNo() {
		return hostNo;
	}


	public void setHostNo(String hostNo) {
		if (hostNo != null) {
			this.hostNo = hostNo;
		}
	}


	public byte getHostPlateColor() {
		return hostPlateColor;
	}


	public void setHostPlateColor(byte hostPlateColor) {
		this.hostPlateColor = hostPlateColor;
	}


	public String getSimNo() {
		return simNo;
	}


	public void setSimNo(String simNo) {
		if (simNo != null) {
			this.simNo = simNo;
		}
	}


	public String getProducerId() {
		return producerId;
	}


	public void setProducerId(String producerId) {
		if (producerId != null) {
			this.producerId = producerId;
		}
	}


	public String getModelType() {
		return modelType;
	}


	public void setModelType(String modelType) {
		if (modelType != null) {
			this.modelType = modelType;
		}
	}

}

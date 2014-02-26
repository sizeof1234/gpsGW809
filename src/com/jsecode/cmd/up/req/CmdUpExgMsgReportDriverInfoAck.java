/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 上报驾驶员身份信息应答消息
 * 
 * 描述：下级平台应答上级平台发送的上报驾驶员身份信息请求消息，
 * 上传指定车辆的驾驶员身份信息数据
 * @author Jadic
 */
public class CmdUpExgMsgReportDriverInfoAck extends CmdHeadSubBizWithCar {
	
	private byte[] driverName;	//16字节   驾驶员姓名
	private byte[] driverId;	//20字节   身份证ID
	private byte[] licence;		//40字节   从业资格证号
	private byte[] orgName;		//200字节 发证机构名称
	
	public CmdUpExgMsgReportDriverInfoAck() {
		this.driverName = new byte[16];
		this.driverId = new byte[20];
		this.licence = new byte[40];
		this.orgName = new byte[200];
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return this.driverName.length 
			 + this.driverId.length 
			 + this.licence.length 
			 + this.orgName.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeBytes(this.driverName);
		channelBuffer.writeBytes(this.driverId);
		channelBuffer.writeBytes(this.licence);
		channelBuffer.writeBytes(this.orgName);
	}

	public byte[] getDriverName() {
		return driverName;
	}

	public void setDriverName(byte[] driverName) {
		if (isByteArraySameSize(this.driverName, driverName)) {
			this.driverName = driverName;
		}
	}

	public byte[] getDriverId() {
		return driverId;
	}

	public void setDriverId(byte[] driverId) {
		if (isByteArraySameSize(this.driverId, driverId)) {
			this.driverId = driverId;
		}
	}

	public byte[] getLicence() {
		return licence;
	}

	public void setLicence(byte[] licence) {
		if (isByteArraySameSize(this.licence, licence)) {
			this.licence = licence;
		}
	}

	public byte[] getOrgName() {
		return orgName;
	}

	public void setOrgName(byte[] orgName) {
		if (isByteArraySameSize(this.orgName, orgName)) {
			this.orgName = orgName;
		}
	}

}

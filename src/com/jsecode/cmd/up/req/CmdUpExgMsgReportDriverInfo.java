/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 主动上报驾驶员身份信息消息
 * 
 * 描述：下级平台在接收到车载终端上传的驾驶员身份信息后，主动向上级平台上报该信息.
 * 本条消息客户端无需应答
 * @author Jadic
 */
public class CmdUpExgMsgReportDriverInfo extends CmdHeadSubBizWithCar {
	
	private byte[] driverName;	//16字节   驾驶员姓名
	private byte[] driverId;	//20字节   身份证ID
	private byte[] licence;		//40字节   从业资格证号
	private byte[] orgName;		//200字节 发证机构名称

	public CmdUpExgMsgReportDriverInfo() {
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

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
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

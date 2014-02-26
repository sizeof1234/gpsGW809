/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithCar;

/**
 * 上传车辆注册信息消息
 * 
 * 描述：监控平台收到车载终端鉴权信息后，启动本命令向上级监管平台
 * 上传该车辆注册信息，各级监管平台再逐级向上级平台上传该信息.
 * 本条消息服务端无需应答
 * @author Jadic
 */
public class CmdUpExgMsgRegisterReq extends CmdHeadSubBizWithCar {
	
	private byte[] platFormId;//11 平台唯一编码，由平台所在地行政区划代码和平台编号组成
	private byte[] producerId;//11 车载终端厂商唯一编码，由车载终端厂商	所在地行政区划代码和制造商ID 组成
	private byte[] terminalModelType;//20 车载终端型号，不足20 位时以“\0”终结
	private byte[] terminalId;//7 车载终端编号，大写字母和数字组成
	private byte[] terminalSimCode;//12 车载终端SIM 卡电话号码。号码不足12位，则在前补充数字0

	public CmdUpExgMsgRegisterReq() {
		this.platFormId = new byte[11];
		this.producerId = new byte[11];
		this.terminalModelType = new byte[20];
		this.terminalId = new byte[7];
		this.terminalSimCode = new byte[12];
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return this.platFormId.length 
			 + this.producerId.length 
			 + this.terminalModelType.length
			 + this.terminalId.length
			 + this.terminalSimCode.length;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeBytes(this.platFormId);
		channelBuffer.writeBytes(this.producerId);
		channelBuffer.writeBytes(this.terminalModelType);
		channelBuffer.writeBytes(this.terminalId);
		channelBuffer.writeBytes(this.terminalSimCode);
	}

	public byte[] getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(byte[] platFormId) {
		if (isByteArraySameSize(this.platFormId, platFormId)) {
			this.platFormId = platFormId;
		}
	}

	public byte[] getProducerId() {
		return producerId;
	}

	public void setProducerId(byte[] producerId) {
		if (isByteArraySameSize(this.producerId, producerId)) {
			this.producerId = producerId;
		}
	}

	public byte[] getTerminalModelType() {
		return terminalModelType;
	}

	public void setTerminalModelType(byte[] terminalModelType) {
		if (isByteArraySameSize(this.terminalModelType, terminalModelType)) {
			this.terminalModelType = terminalModelType;
		}
	}

	public byte[] getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(byte[] terminalId) {
		if (isByteArraySameSize(this.terminalId, terminalId)) {
			this.terminalId = terminalId;
		}
	}

	public byte[] getTerminalSimCode() {
		return terminalSimCode;
	}

	public void setTerminalSimCode(byte[] terminalSimCode) { 
		if (isByteArraySameSize(this.terminalSimCode, terminalSimCode)) {
			this.terminalSimCode = terminalSimCode;
		}
	}

}

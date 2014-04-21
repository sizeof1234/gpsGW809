package com.jsecode.bean;

import java.util.Date;

/**
 * 对应数据库中Terminal_Command相关字段
 * @author 	Jadic
 * @created 2014-4-2
 */
public class TerminalCmdBean {
	
	private int id;					//命令自增ID
	private String vehicleNo;			//车牌号
	private String terminalId;			//终端编号
	private int cmdFlagId;				//命令标识
	private int cmdResponseContext;	//命令应答内容
	private int cmdSeqId;				//命令流水号
	private Date processTime;			//处理时间
	private int cmdSendTimes;			//命令处理次数
	private int gatewayNo;				//对应网关编号

	public TerminalCmdBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public int getCmdFlagId() {
		return cmdFlagId;
	}

	public void setCmdFlagId(int cmdFlagId) {
		this.cmdFlagId = cmdFlagId;
	}

	public int getCmdResponseContext() {
		return cmdResponseContext;
	}

	public void setCmdResponseContext(int cmdResponseContext) {
		this.cmdResponseContext = cmdResponseContext;
	}

	public int getCmdSeqId() {
		return cmdSeqId;
	}

	public void setCmdSeqId(int cmdSeqId) {
		this.cmdSeqId = cmdSeqId;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public int getCmdSendTimes() {
		return cmdSendTimes;
	}

	public void setCmdSendTimes(int cmdSendTimes) {
		this.cmdSendTimes = cmdSendTimes;
	}

	public int getGatewayNo() {
		return gatewayNo;
	}

	public void setGatewayNo(int gatewayNo) {
		this.gatewayNo = gatewayNo;
	}

}

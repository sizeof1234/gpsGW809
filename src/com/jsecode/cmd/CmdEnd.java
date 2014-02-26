package com.jsecode.cmd;

public class CmdEnd {

	private short crcCheckCode;
	private short endFlag;
	
	public CmdEnd() {
	}

	public short getCrcCheckCode() {
		return crcCheckCode;
	}

	public void setCrcCheckCode(short crcCheckCode) {
		this.crcCheckCode = crcCheckCode;
	}

	public short getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(short endFlag) {
		this.endFlag = endFlag;
	}
	
}

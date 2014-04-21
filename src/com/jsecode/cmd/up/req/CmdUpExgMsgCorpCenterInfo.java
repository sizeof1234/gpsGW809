/**
 * @author 	Jadic
 * @created 2014-2-17
 */
package com.jsecode.cmd.up.req;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.CmdHeadSubBizWithoutCar;

/**
 * 实时上传企业管理分中心签到/签退消息
 * 
 * 描述：下级平台收到企业管理分中心的签到/签退消息后，启动本命令向上级平台上传该消息
 * @author Jadic
 */
public class CmdUpExgMsgCorpCenterInfo extends CmdHeadSubBizWithoutCar {
	
	private int infoId;			//4 uint32_t 信息ID
	private byte[] platFormId;	//12 Octet String 运营商编码
	private byte[] userName;	//16 Octet String 用户名
	private byte[] corpName;	//32 Octet String 对应的企业名称
	private byte logInOut;		//1 BYTE 1‐签入/0‐签退
	private long operTime;		//8 time_t 操作时间，UTC 时间格式
	private byte[] ip;			//16 Octet String 分中心IP 地址
	private short port;			//4 uint32_t 分中心PORT

	public CmdUpExgMsgCorpCenterInfo() {
		this.platFormId = new byte[12];
		this.userName = new byte[16];
		this.corpName = new byte[32];
		this.ip = new byte[16];
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 4
			 + this.platFormId.length
			 + this.userName.length
			 + this.corpName.length
			 + 1
			 + 8
			 + this.ip.length
			 + 2;
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.infoId = channelBuffer.readInt();
		channelBuffer.readBytes(this.platFormId);
		channelBuffer.readBytes(this.userName);
		channelBuffer.readBytes(this.corpName);
		this.logInOut = channelBuffer.readByte();
		this.operTime = channelBuffer.readLong();
		channelBuffer.readBytes(this.ip);
		this.port = channelBuffer.readShort();
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeInt(this.infoId);
		channelBuffer.writeBytes(this.platFormId);
		channelBuffer.writeBytes(this.userName);
		channelBuffer.writeBytes(this.corpName);
		channelBuffer.writeByte(this.logInOut);
		channelBuffer.writeLong(this.operTime);
		channelBuffer.writeBytes(this.ip);
		channelBuffer.writeShort(this.port);
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

	public int getInfoId() {
		return infoId;
	}

	public void setInfoId(int infoId) {
		this.infoId = infoId;
	}

	public byte[] getPlatFormId() {
		return platFormId;
	}

	public void setPlatFormId(byte[] platFormId) {
		if (isByteArraySameSize(this.platFormId, platFormId)) {
			this.platFormId = platFormId;
		}
	}

	public byte[] getUserName() {
		return userName;
	}

	public void setUserName(byte[] userName) {
		if(isByteArraySameSize(this.userName, userName)) {
			this.userName = userName;
		}
	}

	public byte[] getCorpName() {
		return corpName;
	}

	public void setCorpName(byte[] corpName) {
		if (isByteArraySameSize(this.corpName, corpName)) {
			this.corpName = corpName;
		}
	}

	public byte getLogInOut() {
		return logInOut;
	}

	public void setLogInOut(byte logInOut) {
		this.logInOut = logInOut;
	}

	public long getOperTime() {
		return operTime;
	}

	public void setOperTime(long operTime) {
		this.operTime = operTime;
	}

	public byte[] getIp() {
		return ip;
	}

	public void setIp(byte[] ip) {
		if (isByteArraySameSize(this.ip, ip)) {
			this.ip = ip;
		}
	}

	public short getPort() {
		return port;
	}

	public void setPort(short port) {
		this.port = port;
	}

}

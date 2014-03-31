package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 项目名称：gpsGW809
 * 类名称：CmdDownCtrlMsgEmergencyMonitoringReq
 * 类描述：
 * ****************************
 * 4.5.6.2.6车辆应急接入监管平台请求消息
 * 子业务类型标识：UP_CTRL_MSG_EMERGENCY_MONITORING_REQ。
 * 描述：在应急情况下，政府监管平台需要及时监控该车辆时，应向该车辆归属的下级平台发送该命令
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-2-20 下午3:47
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CmdDownCtrlMsgEmergencyMonitoringReq extends CmdHeadSubBizWithCar {
    private byte[] authenicationCode;
    private byte[] accessPointName;
    private byte[] userName;
    private byte[] password;
    private byte[] serverIp;
    private short tcpPort;
    private short udpPort;
    private long endTime;

    public CmdDownCtrlMsgEmergencyMonitoringReq() {
        authenicationCode = new byte[10];
        accessPointName = new byte[20];
        userName = new byte[49];
        password = new byte[22];
        serverIp = new byte[32];
    }

    @Override
    protected int getCmdSubBizDataSize() {

        return this.authenicationCode.length + this.accessPointName.length
                + this.userName.length + this.password.length
                + this.serverIp.length + 2 + 2 + 8;
    }

    @Override
    protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
        channelBuffer.readBytes(authenicationCode);
        channelBuffer.readBytes(accessPointName);
        channelBuffer.readBytes(userName);
        channelBuffer.readBytes(password);
        channelBuffer.readBytes(serverIp);
        this.tcpPort = channelBuffer.readShort();
        this.udpPort = channelBuffer.readShort();
        this.endTime = channelBuffer.readLong();
        KKLog.info("UP_CTRL_MSG_EMERGENCY_MONITORING_REQ------4.5.6.2.6车辆应急接入监管平台请求消息 ");
    }

    @Override
    protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

    }

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}
	
    public byte[] getAuthenicationCode() {
        return authenicationCode;
    }

    public void setAuthenicationCode(byte[] authenicationCode) {
        if (isByteArraySameSize(this.authenicationCode, authenicationCode)) {
            this.authenicationCode = authenicationCode;
        }
    }

    public byte[] getAccessPointName() {
        return accessPointName;
    }

    public void setAccessPointName(byte[] accessPointName) {
        if (isByteArraySameSize(this.accessPointName, accessPointName)) {
            this.accessPointName = accessPointName;
        }
    }

    public byte[] getUserName() {
        return userName;
    }

    public void setUserName(byte[] userName) {
        if (isByteArraySameSize(this.userName, userName)) {
            this.userName = userName;
        }
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public byte[] getServerIp() {
        return serverIp;
    }

    public void setServerIp(byte[] serverIp) {
        if (isByteArraySameSize(this.serverIp, serverIp)) {
            this.serverIp = serverIp;
        }
    }

    public short getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(short tcpPort) {
        this.tcpPort = tcpPort;
    }

    public short getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(short udpPort) {
        this.udpPort = udpPort;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

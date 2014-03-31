package com.jsecode.bean.ctrlmsg;

import java.io.Serializable;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.bean.ctrlmsg.CtrlMsgEmergencyMonitoBean
 * 类描述：
 * ****************************
 * *车辆应急接入监管平台请求消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-11 下午3:13
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CtrlMsgEmergencyMonitoBean implements Serializable {

	private static final long serialVersionUID = 8771222842392044051L;
	private Long id; // 标识
    private String vehicleNo; // 车牌号
    private String vehicleColor; // 车辆颜色
    private Long dataType; // 子业务类型标识
    private int dataLength; // 后续数据长度
    private String authenicationCode; // 监管平台下发的鉴权码
    private String accessPointName; // 拨号名称，一般为服务器的APN无线通信号拨号访问点，若网络制式为CDMA，则该值为PPP连接拨号号码
    private String username; // 拨号用户名，服务器无线通信拨号用户名。
    private String password; // 拨号密码，服务器无线通信拨号密码。
    private String serverIp; // 地址，服务器IP地址或域名
    private String tcpPort; // 服务器TCP端口
    private String udpPort; // 服务器UDP端口
    private String endTime; // 结束时间
    private String reqTime; // 请求时间
    private String replyTime; // 回复时间
    private String replyContent; // 0x00：车载终端成功收到该命令；01：无该车辆、02：其他原因失败
    private String repTime; // 发送时间
    private String repStatus; // 状态：01 未应答 02 已回复 03回复消息以应答

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public String getAuthenicationCode() {
        return authenicationCode;
    }

    public void setAuthenicationCode(String authenicationCode) {
        this.authenicationCode = authenicationCode;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public void setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
        this.tcpPort = tcpPort;
    }

    public String getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(String udpPort) {
        this.udpPort = udpPort;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getRepTime() {
        return repTime;
    }

    public void setRepTime(String repTime) {
        this.repTime = repTime;
    }

    public String getRepStatus() {
        return repStatus;
    }

    public void setRepStatus(String repStatus) {
        this.repStatus = repStatus;
    }
}

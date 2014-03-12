package com.jsecode.bean.warnmsg;

import java.io.Serializable;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.cmd.bean.WarnMsgInformBean
 * 类描述：
 * ****************************
 * *
 * *WarnMsgInformBean
 * *
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-7 上午9:00
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class WarnMsgAdptInfoBean  implements Serializable{
    private Long id; // 标识
    private String reqType; // 请求类型：01报警预警消息 ，02实时交换报警信息消息
    private String vehicleNo; // 车牌号
    private String vehicleColor; // 车辆颜色
    private String dataType; // 子业务类型标识
    private int dataLength; // 后续数据长度
    private String warnSrc; // 报警信息来源
    private String warnType; // 报警类型
    private String warnTime; // 报警时间
    private Long infoId; // 信息ID
    private int infoLength; // 报警数据长度
    private String infoContent; // 上报报警信息内容
    private String replyTime; // 回复时间

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public String getWarnSrc() {
        return warnSrc;
    }

    public void setWarnSrc(String warnSrc) {
        this.warnSrc = warnSrc;
    }

    public String getWarnType() {
        return warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public String getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(String warnTime) {
        this.warnTime = warnTime;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public int getInfoLength() {
        return infoLength;
    }

    public void setInfoLength(int infoLength) {
        this.infoLength = infoLength;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}

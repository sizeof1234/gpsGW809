package com.jsecode.bean.warnmsg;

import java.io.Serializable;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.cmd.bean
 * 类描述：
 * ****************************
 * *
 * *
 * *
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-7 上午8:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class WarnMsgUrgeTodoReqBean implements Serializable {
	private static final long serialVersionUID = 5036453099897996273L;
	private Long id; // 标识
    private String vehicleNo; // 车牌号
    private String vehicleColor; // 车辆颜色
    private Long dataType; // 子业务类型标识
    private int dataLength; // 后续数据长度
    private String warnSrc; // 报警信息来源
    private String warnType; // 报警类型
    private String warnTime; // 报警时间
    private Long supervisionId; // 报警督办ID
    private String supervisionEndtime; // 督办截止时间
    private String supervisionLevel; // 督办级别：00紧急 ,01：一般
    private String supervisor; // 督办人
    private String supervisionTel; // 督办联系电话
    private String supervisionEmal; // 督办联系电子邮件
    private String reqTime; // 请求时间
    private String replyTime; // 回复时间
    private String replyContent; // 回复处理结果：00 处理中，01以处理完毕，02不做处理，03将来处理
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

    public Long getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(Long supervisionId) {
        this.supervisionId = supervisionId;
    }

    public String getSupervisionEndtime() {
        return supervisionEndtime;
    }

    public void setSupervisionEndtime(String supervisionEndtime) {
        this.supervisionEndtime = supervisionEndtime;
    }

    public String getSupervisionLevel() {
        return supervisionLevel;
    }

    public void setSupervisionLevel(String supervisionLevel) {
        this.supervisionLevel = supervisionLevel;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getSupervisionTel() {
        return supervisionTel;
    }

    public void setSupervisionTel(String supervisionTel) {
        this.supervisionTel = supervisionTel;
    }

    public String getSupervisionEmal() {
        return supervisionEmal;
    }

    public void setSupervisionEmal(String supervisionEmal) {
        this.supervisionEmal = supervisionEmal;
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

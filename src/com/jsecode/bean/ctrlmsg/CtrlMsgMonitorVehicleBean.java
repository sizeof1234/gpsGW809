package com.jsecode.bean.ctrlmsg;

import java.io.Serializable;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.bean.ctrlmsg.CtrlMsgMonitorVehicleBean
 * 类描述：
 * ****************************
 * *车辆单向监听请求消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-11 下午3:12
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CtrlMsgMonitorVehicleBean  implements Serializable {

	private static final long serialVersionUID = 2282802302202332923L;
	private Long id; // 标识
    private String vehicleNo; // 车牌号
    private String vehicleColor; // 车辆颜色
    private Long dataType; // 子业务类型标识
    private int dataLength; // 后续数据长度
    private String monitorTel; // 同拨电话号码，参数为电话号码，如有分机号码，中间用“_”分隔
    private String reqTime; // 请求时间
    private String replyTime; // 回复时间
    private String replyContent; // 车辆单向监听应答结果，定义如下：00：监听成功01：监听失败。
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

    public String getMonitorTel() {
        return monitorTel;
    }

    public void setMonitorTel(String monitorTel) {
        this.monitorTel = monitorTel;
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

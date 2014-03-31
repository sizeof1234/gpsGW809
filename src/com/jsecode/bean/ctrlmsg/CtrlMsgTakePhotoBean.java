package com.jsecode.bean.ctrlmsg;

import java.io.Serializable;
import oracle.sql.BLOB;


/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.bean.ctrlmsg.CtrlMsgTakePhotoBean
 * 类描述：
 * ****************************
 * *车辆拍照请求消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-11 下午3:10
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class CtrlMsgTakePhotoBean implements Serializable {

	private static final long serialVersionUID = 4008953449014857805L;
	private Long id; // 标识
    private String vehicleNo; // 车牌号
    private String vehicleColor; // 车辆颜色
    private Long dataType; // 子业务类型标识
    private int dataLength; // 后续数据长度
    private Long lensId; // 镜头ID
    private String size; // 照片大小，定义如下:01:320x240；02:640x480；03;:800x600；04:1024x768；05:176x 144[QCIF]；06:704x288[CIF]；07:704x288[HALF D]；08:704576[DI]
    private String photoRspFlag; // 拍照应答标识，标识拍照后的结果或原因,定义如下:00:布支持拍照相；01:完成拍照；02:完成拍照、照片数据稍后传送；03:未拍照(不在线)；04:未拍照;(无法使用指定镜头)；05:未拍照(其他原因）；09:车牌号码错误。
    private String gnssData; // 拍照位置地点
    private String type; // 图像格式，定义如下：0l:jpg:02:gif;03:tiff;04:png.
    private int photoLen; // 图片长度
    private BLOB  phone; // 图片内容
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

    public Long getLensId() {
        return lensId;
    }

    public void setLensId(Long lensId) {
        this.lensId = lensId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPhotoRspFlag() {
        return photoRspFlag;
    }

    public void setPhotoRspFlag(String photoRspFlag) {
        this.photoRspFlag = photoRspFlag;
    }

    public String getGnssData() {
        return gnssData;
    }

    public void setGnssData(String gnssData) {
        this.gnssData = gnssData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPhotoLen() {
        return photoLen;
    }

    public void setPhotoLen(int photoLen) {
        this.photoLen = photoLen;
    }

    public BLOB getPhone() {
        return phone;
    }

    public void setPhone(BLOB phone) {
        this.phone = phone;
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

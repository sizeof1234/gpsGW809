package com.jsecode.bean.platform;

import java.io.Serializable;

public class PlatformMsgBean implements Serializable {

	private static final long serialVersionUID = -4209336505969019648L;
	private Long id; // 标识
    private String reqType; // 请求类型, 01:平台查岗请求消息 02:上级平台下发平台间报文请求消息
    private Long dataType; // 子业务类型标识
    private int objectLength; // 后续数据长度
    private String objectType; // 对象类型
    private String objectId; // 查岗对象的ID
    private Long infoId; // 信息ID
    private int infoLength; // 信息长度
    private String infoContent; // 信息内容
    private String reqTime; // 请求时间
    private String repTime; // 发送时间
    private String replyTime; // 回复时间
    private String repMsg; // 应答内容
    private String repStatus; // 01客户端未回复、02客户端已回复、03 809请求已应答

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

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public int getObjectLength() {
        return objectLength;
    }

    public void setObjectLength(int objectLength) {
        this.objectLength = objectLength;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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


    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public String getRepStatus() {
        return repStatus;
    }

    public void setRepStatus(String repStatus) {
        this.repStatus = repStatus;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getRepTime() {
        return repTime;
    }

    public void setRepTime(String repTime) {
        this.repTime = repTime;
    }
}
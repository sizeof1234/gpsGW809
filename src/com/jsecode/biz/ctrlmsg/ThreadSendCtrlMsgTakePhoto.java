package com.jsecode.biz.ctrlmsg;

import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgTakePhotoBean;
import com.jsecode.cmd.up.req.CmdUpCtrlMsgTakePhotoAck;
import com.jsecode.link.IMainSubLink;
import oracle.sql.BLOB;

import java.io.IOException;
import java.io.InputStream;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadSendCtrlMsgTakePhoto
 * 类描述：
 * ****************************
 * *发送车辆拍照应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadSendCtrlMsgTakePhoto   extends AbstractThreadSendData<CtrlMsgTakePhotoBean> {

    public ThreadSendCtrlMsgTakePhoto(IGW809 gw809) {
        super(gw809, ThreadSendCtrlMsgTakePhoto.class.getName());
    }
    @Override
    public void run() {
        CtrlMsgTakePhotoBean ctrlMsgTakePhotoBean = null;
        while(!isInterrupted()) {
            while((ctrlMsgTakePhotoBean = getQueuePollData()) != null) {
                sendInfo(ctrlMsgTakePhotoBean);
            }
            waitNewData();
        }
    }
    private boolean sendInfo(CtrlMsgTakePhotoBean ctrlMsgTakePhotoBean) {
        CmdUpCtrlMsgTakePhotoAck cmdUpCtrlMsgTakePhotoAck = new CmdUpCtrlMsgTakePhotoAck();
        cmdUpCtrlMsgTakePhotoAck.setVehicleNo(ctrlMsgTakePhotoBean.getVehicleNo().getBytes());
        cmdUpCtrlMsgTakePhotoAck.setVehicleColor(Long.valueOf(ctrlMsgTakePhotoBean.getVehicleColor()).byteValue());
        cmdUpCtrlMsgTakePhotoAck.setSubMsgId(Long.valueOf(ctrlMsgTakePhotoBean.getDataType()).byteValue());
        cmdUpCtrlMsgTakePhotoAck.setLensId(ctrlMsgTakePhotoBean.getLensId().byteValue());
        cmdUpCtrlMsgTakePhotoAck.setPhotoData(blobToBytes(ctrlMsgTakePhotoBean.getPhone()));
        cmdUpCtrlMsgTakePhotoAck.setPhotoRespFlag(Long.valueOf(ctrlMsgTakePhotoBean.getPhotoRspFlag()).byteValue());
        cmdUpCtrlMsgTakePhotoAck.setSizeType(Long.valueOf(ctrlMsgTakePhotoBean.getSize()).byteValue());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpCtrlMsgTakePhotoAck.getSendBuffer()) != null;
    }
    private byte[] blobToBytes(BLOB blob){
        InputStream is = null;
        byte[] b = null;
        try {
            is = blob.getBinaryStream();
            b = new byte[(int) blob.length()];
            is.read(b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}

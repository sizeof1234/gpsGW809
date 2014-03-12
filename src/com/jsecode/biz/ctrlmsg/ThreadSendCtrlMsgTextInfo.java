package com.jsecode.biz.ctrlmsg;

import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgTextInfoBean;
import com.jsecode.cmd.up.req.CmdUpCtrlMsgTextInfoAck;
import com.jsecode.link.IMainSubLink;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg
 * 类描述：
 * ****************************
 * *
 * *
 * *
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadSendCtrlMsgTextInfo   extends AbstractThreadSendData<CtrlMsgTextInfoBean> {

    public ThreadSendCtrlMsgTextInfo(IGW809 gw809) {
        super(gw809, ThreadSendCtrlMsgTakeTravel.class.getName());
    }
    @Override
    public void run() {
        CtrlMsgTextInfoBean ctrlMsgTextInfoBean = null;
        while(!isInterrupted()) {
            while((ctrlMsgTextInfoBean = queue.poll()) != null) {
                sendInfo(ctrlMsgTextInfoBean);
            }
            waitNewData();
        }
    }
    private boolean sendInfo( CtrlMsgTextInfoBean ctrlMsgTextInfoBean) {
        CmdUpCtrlMsgTextInfoAck cmdUpCtrlMsgTextInfoAck = new CmdUpCtrlMsgTextInfoAck();
        cmdUpCtrlMsgTextInfoAck.setResult(Long.valueOf(ctrlMsgTextInfoBean.getReplyContent()).byteValue());
        cmdUpCtrlMsgTextInfoAck.setSubMsgId(ctrlMsgTextInfoBean.getDataType().byteValue());
        cmdUpCtrlMsgTextInfoAck.setVehicleColor(Long.valueOf(ctrlMsgTextInfoBean.getVehicleColor()).byteValue());
        cmdUpCtrlMsgTextInfoAck.setVehicleNo(ctrlMsgTextInfoBean.getVehicleNo().getBytes());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpCtrlMsgTextInfoAck.getSendBuffer()) != null;
    }
}

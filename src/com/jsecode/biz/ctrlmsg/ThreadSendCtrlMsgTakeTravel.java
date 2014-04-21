package com.jsecode.biz.ctrlmsg;

import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgTakeTravelBean;
import com.jsecode.cmd.up.req.CmdUpCtrlMsgTakeTravelAck;
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
 * 创建时间：14-3-12 上午9:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadSendCtrlMsgTakeTravel  extends AbstractThreadSendData<CtrlMsgTakeTravelBean> {

    public ThreadSendCtrlMsgTakeTravel(IGW809 gw809) {
        super(gw809, ThreadSendCtrlMsgTakeTravel.class.getName());
    }
    @Override
    public void run() {
        CtrlMsgTakeTravelBean ctrlMsgTakeTravelBean = null;
        while(!isInterrupted()) {
            while((ctrlMsgTakeTravelBean = getQueuePollData()) != null) {
                sendInfo(ctrlMsgTakeTravelBean);
            }
            waitNewData();
        }
    }
    private boolean sendInfo( CtrlMsgTakeTravelBean ctrlMsgTakeTravelBean) {
        CmdUpCtrlMsgTakeTravelAck cmdUpCtrlMsgTakeTravelAck = new CmdUpCtrlMsgTakeTravelAck();
        cmdUpCtrlMsgTakeTravelAck.setTravelData(ctrlMsgTakeTravelBean.getReplyContent().getBytes());
        cmdUpCtrlMsgTakeTravelAck.setSubMsgId(ctrlMsgTakeTravelBean.getDataType().byteValue());
        cmdUpCtrlMsgTakeTravelAck.setVehicleColor(Long.valueOf(ctrlMsgTakeTravelBean.getVehicleColor()).byteValue());
        cmdUpCtrlMsgTakeTravelAck.setVehicleNo(ctrlMsgTakeTravelBean.getVehicleNo().getBytes());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpCtrlMsgTakeTravelAck.getSendBuffer()) != null;
    }
}

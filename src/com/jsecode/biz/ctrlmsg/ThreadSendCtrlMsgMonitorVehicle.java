package com.jsecode.biz.ctrlmsg;

import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgMonitorVehicleBean;
import com.jsecode.cmd.up.req.CmdUpCtrlMsgMonitorVehicleAck;
import com.jsecode.link.IMainSubLink;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadSendCtrlMsgMonitorVehicle
 * 类描述：
 * ****************************
 * *发送车辆单向监听应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadSendCtrlMsgMonitorVehicle  extends AbstractThreadSendData<CtrlMsgMonitorVehicleBean> {

    public ThreadSendCtrlMsgMonitorVehicle(IGW809 gw809) {
        super(gw809, ThreadSendCtrlMsgMonitorVehicle.class.getName());
    }
    @Override
    public void run() {
        CtrlMsgMonitorVehicleBean ctrlMsgMonitorVehicleBean = null;
        while(!isInterrupted()) {
            while((ctrlMsgMonitorVehicleBean = getQueuePollData()) != null) {
                sendInfo(ctrlMsgMonitorVehicleBean);
            }
            waitNewData();
        }
    }
    private boolean sendInfo(CtrlMsgMonitorVehicleBean ctrlMsgMonitorVehicleBean) {
        CmdUpCtrlMsgMonitorVehicleAck cmdUpCtrlMsgMonitorVehicleAck = new CmdUpCtrlMsgMonitorVehicleAck();
        cmdUpCtrlMsgMonitorVehicleAck.setResult(Long.valueOf(ctrlMsgMonitorVehicleBean.getReplyContent()).byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setSubMsgId(ctrlMsgMonitorVehicleBean.getDataType().byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setVehicleColor(Long.valueOf(ctrlMsgMonitorVehicleBean.getVehicleColor()).byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setVehicleNo(ctrlMsgMonitorVehicleBean.getVehicleNo().getBytes());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpCtrlMsgMonitorVehicleAck.getSendBuffer()) != null;
    }
}

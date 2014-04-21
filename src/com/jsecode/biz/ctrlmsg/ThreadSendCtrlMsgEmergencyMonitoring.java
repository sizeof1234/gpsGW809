package com.jsecode.biz.ctrlmsg;

import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgEmergencyMonitoBean;
import com.jsecode.cmd.up.req.CmdUpCtrlMsgMonitorVehicleAck;
import com.jsecode.link.IMainSubLink;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadSendCtrlMsgEmergencyMonitoring
 * 类描述：
 * ****************************
 * *发送车辆应急接入监管平台应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadSendCtrlMsgEmergencyMonitoring extends AbstractThreadSendData<CtrlMsgEmergencyMonitoBean> {

    public ThreadSendCtrlMsgEmergencyMonitoring(IGW809 gw809) {
        super(gw809, ThreadSendCtrlMsgEmergencyMonitoring.class.getName());
    }
    @Override
    public void run() {
        CtrlMsgEmergencyMonitoBean ctrlMsgEmergencyMonitoBean = null;
        while(!isInterrupted()) {
            while((ctrlMsgEmergencyMonitoBean = getQueuePollData()) != null) {
                sendInfo(ctrlMsgEmergencyMonitoBean);
            }
            waitNewData();
        }
    }
    private boolean sendInfo(CtrlMsgEmergencyMonitoBean ctrlMsgEmergencyMonitoBean) {
        CmdUpCtrlMsgMonitorVehicleAck cmdUpCtrlMsgMonitorVehicleAck = new CmdUpCtrlMsgMonitorVehicleAck();
        cmdUpCtrlMsgMonitorVehicleAck.setResult(Long.valueOf(ctrlMsgEmergencyMonitoBean.getReplyContent()).byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setSubMsgId(ctrlMsgEmergencyMonitoBean.getDataType().byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setVehicleColor(Long.valueOf(ctrlMsgEmergencyMonitoBean.getVehicleColor()).byteValue());
        cmdUpCtrlMsgMonitorVehicleAck.setVehicleNo(ctrlMsgEmergencyMonitoBean.getVehicleNo().getBytes());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpCtrlMsgMonitorVehicleAck.getSendBuffer()) != null;
    }
}

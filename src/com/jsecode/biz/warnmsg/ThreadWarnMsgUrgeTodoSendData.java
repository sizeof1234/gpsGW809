package com.jsecode.biz.warnmsg;


import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.warnmsg.WarnMsgUrgeTodoReqBean;
import com.jsecode.cmd.up.req.CmdUpWarnMsgUrgeTodoAck;
import com.jsecode.link.IMainSubLink;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ThreadSendPlatData
 * 类描述：
 * ****************************
 * *下发车辆报警交互信息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-3 下午4:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadWarnMsgUrgeTodoSendData extends AbstractThreadSendData<WarnMsgUrgeTodoReqBean> {

    public ThreadWarnMsgUrgeTodoSendData(IGW809 gw809) {
        super(gw809, ThreadWarnMsgInformSendData.class.getName());
    }

    @Override
    public void run() {
        WarnMsgUrgeTodoReqBean warnMsgUrgeTodoReqBean = null;
        while (!isInterrupted()) {
            while ((warnMsgUrgeTodoReqBean = getQueuePollData()) != null) {
                sendInfo(warnMsgUrgeTodoReqBean);
            }
            waitNewData();
        }
    }

    private boolean sendInfo(WarnMsgUrgeTodoReqBean warnMsgUrgeTodoReqBean) {
        CmdUpWarnMsgUrgeTodoAck cmdUpWarnMsgUrgeTodoAck = new CmdUpWarnMsgUrgeTodoAck();
        cmdUpWarnMsgUrgeTodoAck.setVehicleNo(warnMsgUrgeTodoReqBean.getVehicleNo().getBytes());
        cmdUpWarnMsgUrgeTodoAck.setVehicleColor(Integer.valueOf(warnMsgUrgeTodoReqBean.getVehicleColor()).byteValue());
        cmdUpWarnMsgUrgeTodoAck.setSubMsgId(warnMsgUrgeTodoReqBean.getDataType().byteValue());
        cmdUpWarnMsgUrgeTodoAck.setSupervisonId(warnMsgUrgeTodoReqBean.getSupervisionId().byteValue());
        cmdUpWarnMsgUrgeTodoAck.setResult(Long.valueOf(warnMsgUrgeTodoReqBean.getReplyContent()).byteValue());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpWarnMsgUrgeTodoAck.getSendBuffer()) != null;
    }
}

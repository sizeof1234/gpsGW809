package com.jsecode.biz.warnmsg;


import com.jsecode.IGW809;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.bean.warnmsg.WarnMsgAdptInfoBean;
import com.jsecode.cmd.up.req.CmdUpWarnMsgAdptInfo;
import com.jsecode.link.IMainSubLink;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.platform.ThreadSendPlatData
 * 类描述：
 * ****************************
 * *下发信息交互信息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-3 下午4:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadWarnMsgInformSendData extends AbstractThreadSendData<WarnMsgAdptInfoBean> {

    public ThreadWarnMsgInformSendData(IGW809 gw809) {
        super(gw809, ThreadWarnMsgInformSendData.class.getName());
    }

    @Override
    public void run() {
        WarnMsgAdptInfoBean warnMsgAdptInfoBean = null;
        while (!isInterrupted()) {
            while ((warnMsgAdptInfoBean = queue.poll()) != null) {
                sendInfo(warnMsgAdptInfoBean);
            }
            waitNewData();
        }
    }

    private boolean sendInfo(WarnMsgAdptInfoBean warnMsgAdptInfoBean) {
        CmdUpWarnMsgAdptInfo cmdUpWarnMsgAdptInfo = new CmdUpWarnMsgAdptInfo();
        cmdUpWarnMsgAdptInfo.setSubMsgId(Long.valueOf(warnMsgAdptInfoBean.getDataType()).byteValue());
        cmdUpWarnMsgAdptInfo.setVehicleColor(Long.valueOf(warnMsgAdptInfoBean.getVehicleColor()).byteValue());
        cmdUpWarnMsgAdptInfo.setVehicleNo(warnMsgAdptInfoBean.getVehicleColor().getBytes());
        cmdUpWarnMsgAdptInfo.setInfoId(warnMsgAdptInfoBean.getInfoId().byteValue());
        cmdUpWarnMsgAdptInfo.setWarnSrc(Long.valueOf(warnMsgAdptInfoBean.getWarnSrc()).byteValue());
        cmdUpWarnMsgAdptInfo.setWarnTime(Long.valueOf(warnMsgAdptInfoBean.getWarnTime()).byteValue());
        cmdUpWarnMsgAdptInfo.setWarnType(Long.valueOf(warnMsgAdptInfoBean.getWarnType()).byteValue());
        cmdUpWarnMsgAdptInfo.setInfoContent(warnMsgAdptInfoBean.getInfoContent().getBytes());
        IMainSubLink mLink = getMainLink(true);
        return mLink.sendData(cmdUpWarnMsgAdptInfo.getSendBuffer()) != null;
    }
}

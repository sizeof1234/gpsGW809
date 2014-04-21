package com.jsecode.biz.ctrlmsg;

import com.jsecode.biz.IAddDataToQueue;
import com.jsecode.bean.ctrlmsg.CtrlMsgEmergencyMonitoBean;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadLoadCtrlMsgEmergencyMonitoring
 * 类描述：
 * ****************************
 * *加载车辆应急接入监管平台应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:40
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadLoadCtrlMsgEmergencyMonitoring implements Runnable{
    private IAddDataToQueue<CtrlMsgEmergencyMonitoBean> sendData;
    private CountDownLatch dataLoadSignal;
    public ThreadLoadCtrlMsgEmergencyMonitoring(IAddDataToQueue<CtrlMsgEmergencyMonitoBean> sendData,CountDownLatch dataLoadSignal){
        this.sendData=sendData;
        this.dataLoadSignal=dataLoadSignal;
    }
    @Override
    public void run() {
        try {//wait the terminal info loaded
            this.dataLoadSignal.await();
        } catch (InterruptedException e) {
        }

        List<CtrlMsgEmergencyMonitoBean> list = null;
        //TODO load CtrlMsgEmergencyMonito info from database or memcached

        addInfoToQueue(list);
    }
    private void addInfoToQueue(List<CtrlMsgEmergencyMonitoBean> list) {
        if (list != null && list.size() > 0) {
            this.sendData.addListToQueue(list);
        }
    }
}

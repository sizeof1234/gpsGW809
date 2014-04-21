package com.jsecode.biz.warnmsg;

import com.jsecode.biz.IAddDataToQueue;
import com.jsecode.bean.warnmsg.WarnMsgUrgeTodoReqBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.warnmsg.ThreadWarnMsgUrgeTodoLoadData
 * 类描述：
 * ****************************
 * *读取需要应答的信息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-3 下午5:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadWarnMsgUrgeTodoLoadData implements Runnable {
    private IAddDataToQueue<WarnMsgUrgeTodoReqBean> sendData;
    private CountDownLatch dataLoadSignal;

    public ThreadWarnMsgUrgeTodoLoadData(IAddDataToQueue<WarnMsgUrgeTodoReqBean> sendData, CountDownLatch dataLoadSignal) {
        this.sendData = sendData;
        this.dataLoadSignal = dataLoadSignal;
    }

    @Override
    public void run() {
        try {//wait the terminal info loaded
            this.dataLoadSignal.await();
        } catch (InterruptedException e) {
        }

        List<WarnMsgUrgeTodoReqBean> list = null;
        //TODO load WarnMsgUrgeTodoReq info from database or memcached

        addInfoToQueue(list);
    }

    private void addInfoToQueue(List<WarnMsgUrgeTodoReqBean> list) {
        if (list != null && list.size() > 0) {
            this.sendData.addListToQueue(list);
        }
    }
}

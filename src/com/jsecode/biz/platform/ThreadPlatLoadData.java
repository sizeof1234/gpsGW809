package com.jsecode.biz.platform;

import com.jsecode.bean.platform.PlatformMsgBean;
import com.jsecode.biz.IAddDataToQueue;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.biz.platform.ThreadPlatLoadData
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
public class ThreadPlatLoadData extends Thread {
    private CountDownLatch dataLoadSignal;
    private IAddDataToQueue<PlatformMsgBean> sendData;

    public ThreadPlatLoadData(IAddDataToQueue<PlatformMsgBean> sendData, CountDownLatch dataLoadSignal) {
        this.sendData = sendData;
        this.dataLoadSignal = dataLoadSignal;

    }

    public void run() {
        try {//wait the terminal info loaded
            this.dataLoadSignal.await();
        } catch (InterruptedException e) {
        }

        List<PlatformMsgBean> list = null;
        //TODO load PlatformMsgBean info from database or memcached

        addInfoToQueue(list);
    }

    private void addInfoToQueue(List<PlatformMsgBean> list) {
        if (list != null && list.size() > 0) {
            this.sendData.addListToQueue(list);
        }
    }
}

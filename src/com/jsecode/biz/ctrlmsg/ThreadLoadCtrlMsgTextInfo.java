package com.jsecode.biz.ctrlmsg;

import com.jsecode.biz.ISendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgTextInfoBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadLoadCtrlMsgTextInfo
 * 类描述：
 * ****************************
 * *加载下发车辆报文应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadLoadCtrlMsgTextInfo  implements Runnable{
    private ISendData<CtrlMsgTextInfoBean> sendData;
    private CountDownLatch dataLoadSignal;
    public ThreadLoadCtrlMsgTextInfo(ISendData<CtrlMsgTextInfoBean> sendData,CountDownLatch dataLoadSignal){
        this.sendData=sendData;
        this.dataLoadSignal=dataLoadSignal;
    }
    @Override
    public void run() {
        try {//wait the terminal info loaded
            this.dataLoadSignal.await();
        } catch (InterruptedException e) {
        }

        List<CtrlMsgTextInfoBean> list = null;
        //TODO load CtrlMsgTextInfo info from database or memcached

        addInfoToQueue(list);
    }
    private void addInfoToQueue(List<CtrlMsgTextInfoBean> list) {
        if (list != null && list.size() > 0) {
            this.sendData.addListToQueue(list);
        }
    }
}

package com.jsecode.biz.ctrlmsg;

import com.jsecode.biz.ISendData;
import com.jsecode.bean.ctrlmsg.CtrlMsgTakePhotoBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 项目名称：gpsGW809
 * 类名称： com.jsecode.biz.ctrlmsg.ThreadLoadCtrlMsgTakePhoto
 * 类描述：
 * ****************************
 * *加载车辆拍照应答消息
 * ****************************
 * 创建人：zhaorg
 * 创建时间：14-3-12 上午9:39
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class ThreadLoadCtrlMsgTakePhoto  implements Runnable{
    private ISendData<CtrlMsgTakePhotoBean> sendData;
    private CountDownLatch dataLoadSignal;
    public ThreadLoadCtrlMsgTakePhoto(ISendData<CtrlMsgTakePhotoBean> sendData,CountDownLatch dataLoadSignal){
        this.sendData=sendData;
        this.dataLoadSignal=dataLoadSignal;
    }
    @Override
    public void run() {
        try {//wait the terminal info loaded
            this.dataLoadSignal.await();
        } catch (InterruptedException e) {
        }

        List<CtrlMsgTakePhotoBean> list = null;
        //TODO load CtrlMsgTakePhoto info from database or memcached

        addInfoToQueue(list);
    }
    private void addInfoToQueue(List<CtrlMsgTakePhotoBean> list) {
        if (list != null && list.size() > 0) {
            this.sendData.addListToQueue(list);
        }
    }
}

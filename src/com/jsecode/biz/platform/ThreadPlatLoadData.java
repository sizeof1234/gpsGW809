package com.jsecode.biz.platform;

import com.jsecode.bean.platform.PlatformMsgBean;
import com.jsecode.db.DBOper;

import java.util.List;

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
    //将数据库的最大的回复时间取出作比较
    private Long compareMaxTime;
    private PlatformMsgBean comparePlatformMsg;
    private ISendPlatData sendPlatData;

    public ThreadPlatLoadData(ISendPlatData sendPlatData) {
        this.sendPlatData = sendPlatData;

    }

    /**
     * 根据compareMaxTime 判定每次读取的数据是否重复
     */
    @Override
    public void run() {
        //第一次时，获取数据库时间
        if (compareMaxTime == null) {
            comparePlatformMsg = new PlatformMsgBean();
            comparePlatformMsg.setReqType("02");
            comparePlatformMsg.setReplyTime((String) DBOper.getDBOper().findSysdate());
        }
        List<PlatformMsgBean> list = DBOper.getDBOper().findPlatformMsgDataMultiple(comparePlatformMsg, PlatformMsgBean.class);
        if (null != list && list.size() > 0) {
            PlatformMsgBean platformMsgBean = list.get(0);
            Long replayTime = Long.valueOf(platformMsgBean.getReplyTime().replaceAll("[-\\s:]", ""));
            if (compareMaxTime == null) {
                addGpsDataToQueue(list);
                compareMaxTime = replayTime;
            } else if (replayTime > compareMaxTime) {
                addGpsDataToQueue(list);
            }
            comparePlatformMsg.setReplyTime(platformMsgBean.getReplyTime());
        }
    }

    private void addGpsDataToQueue(List<PlatformMsgBean> list) {
        this.sendPlatData.addPlatformDataList(list);
    }
}

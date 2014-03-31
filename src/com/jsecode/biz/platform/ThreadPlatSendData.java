package com.jsecode.biz.platform;


import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jsecode.IGW809;
import com.jsecode.bean.platform.PlatformMsgBean;
import com.jsecode.cmd.up.req.CmdUpPlatFormMsgInfoAck;
import com.jsecode.cmd.up.req.CmdUpPlatFormMsgPostQueryAck;
import com.jsecode.db.DBOper;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.MyExceptionHandler;

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
public class ThreadPlatSendData extends Thread implements ISendPlatData {
    private final Queue<PlatformMsgBean> gpsQueue;
    private Object objNewData = new Object();
    private Object objLinkConnected = new Object();
    final static int QUEUE_LIMIT_SIZE = 100000;
    private IGW809 gw809;

    public ThreadPlatSendData(IGW809 gw809) {
        this.gpsQueue = new ConcurrentLinkedQueue<PlatformMsgBean>();
        this.gw809 = gw809;
        this.setUncaughtExceptionHandler(new MyExceptionHandler());
    }

    public void run() {
        PlatformMsgBean platformMsgBean = null;
        while (!isInterrupted()) {
            boolean isLinkConnected = false;
            while ((platformMsgBean = gpsQueue.peek()) != null) {
                isLinkConnected = sendPlatformMsg(platformMsgBean);
                if (isLinkConnected) {
                    this.gpsQueue.poll();
                } else {
                    KKLog.info("thread-platformMsg-data detected the main and sub link disconnected, please check ...");
                    if (this.gpsQueue.size() > QUEUE_LIMIT_SIZE) {
                        this.gpsQueue.poll();//if the queue's current size exceeds the default limit size, remove bean and continue loop
                    } else {
                        break;
                    }
                }
            }

            if (!isLinkConnected) {
                waitNewData();
            } else {
                waitLinkConnected();
            }
        }
    }

    private boolean sendPlatformMsg(PlatformMsgBean platformMsg) {
        boolean bl = false;
        IMainSubLink link = this.gw809.getMainLink(true);
        if (link.isChannelConnected()) {
            if ("01".equals(platformMsg.getReqType())) {
                CmdUpPlatFormMsgPostQueryAck cmdUpPlatFormMsgPostQueryAck = new CmdUpPlatFormMsgPostQueryAck();
                cmdUpPlatFormMsgPostQueryAck.setObjectType(Byte.valueOf(platformMsg.getObjectType()));
                cmdUpPlatFormMsgPostQueryAck.setInfoId(platformMsg.getInfoId().byteValue());
                cmdUpPlatFormMsgPostQueryAck.setObjectId(KKTool.getFixedLenString(platformMsg.getObjectId(), 12, Const.BLANK_CHAR, false).getBytes());
                cmdUpPlatFormMsgPostQueryAck.setInfoContent(platformMsg.getInfoContent() == null ? "".getBytes() : platformMsg.getInfoContent().getBytes());
                bl = link.sendData(cmdUpPlatFormMsgPostQueryAck.getSendBuffer()) != null;
            } else if ("02".equals(platformMsg.getReqType())) {
                CmdUpPlatFormMsgInfoAck cmdUpPlatFormMsgInfoAck = new CmdUpPlatFormMsgInfoAck();
                cmdUpPlatFormMsgInfoAck.setInfoId(platformMsg.getInfoId().byteValue());
                bl = link.sendData(cmdUpPlatFormMsgInfoAck.getSendBuffer()) != null;
            } else {
                KKLog.info("ReqType 不存在匹配值!");
            }
        }
        if (bl) {
            updateReqPlatFormMsg(platformMsg);
        }
        return bl;
    }

    /**
     * 更新应答状态
     *
     * @param platformMsg
     */
    private void updateReqPlatFormMsg(PlatformMsgBean platformMsg) {
        int cnt = DBOper.getDBOper().updatePlatformMsgPostQueryAck(platformMsg);
        if (cnt <= 0) {
            KKLog.info("根先应答状态失败，ID：" + platformMsg.getId());
        }
    }

    private void waitNewData() {
        synchronized (objNewData) {
            try {
                objNewData.wait(45);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void waitLinkConnected() {
        synchronized (objLinkConnected) {
            try {
                objLinkConnected.wait(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public boolean addPlatformDataList(List<PlatformMsgBean> list) {
        if (list == null || list.indexOf(null) >= 0) {
            KKLog.warn("addPlatformDataList(List list): list is null or list contains null object, check your damn code.");
            return false;
        }

        if (!this.gpsQueue.addAll(list)) {
            KKLog.warn("add platformMsg data to queue fail");
        } else {
            noticeNewData();
        }
        KKLog.info("current platformMsg data queue size:" + this.gpsQueue.size());
        return true;
    }

    private void noticeNewData() {
        synchronized (objNewData) {
            objNewData.notify();
        }
    }

    @Override
    public boolean addPlatformData(PlatformMsgBean platformMsgBean) {
        if (platformMsgBean == null) {
            return false;
        }

        if (!this.gpsQueue.offer(platformMsgBean)) {
            KKLog.warn("add gps platformMsg to queue fail");
        } else {
            noticeNewData();
        }
        KKLog.info("current platformMsg data queue size:" + this.gpsQueue.size());
        return true;
    }
}

package com.jsecode.biz.platform;


import com.jsecode.IGW809;
import com.jsecode.bean.platform.PlatformMsgBean;
import com.jsecode.biz.AbstractThreadSendData;
import com.jsecode.cmd.up.req.CmdUpPlatFormMsgInfoAck;
import com.jsecode.cmd.up.req.CmdUpPlatFormMsgPostQueryAck;
import com.jsecode.db.DBOper;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

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
public class ThreadPlatSendData extends AbstractThreadSendData<PlatformMsgBean> {
    public ThreadPlatSendData(IGW809 gw809) {
        super(gw809, ThreadPlatSendData.class.getName());
    }


    public void run() {
        PlatformMsgBean platformMsgBean = null;
        while (!isInterrupted()) {
            while ((platformMsgBean = getQueuePollData()) != null) {
                sendInfo(platformMsgBean);
            }
            waitNewData();
        }
    }

    private boolean sendInfo(PlatformMsgBean platformMsg) {
        IMainSubLink mLink = getMainLink(true);
        if ("01".equals(platformMsg.getReqType())) {
            CmdUpPlatFormMsgPostQueryAck cmdUpPlatFormMsgPostQueryAck = new CmdUpPlatFormMsgPostQueryAck();
            cmdUpPlatFormMsgPostQueryAck.setObjectType(Byte.valueOf(platformMsg.getObjectType()));
            cmdUpPlatFormMsgPostQueryAck.setInfoId(platformMsg.getInfoId().byteValue());
            cmdUpPlatFormMsgPostQueryAck.setObjectId(KKTool.getFixedLenString(platformMsg.getObjectId(), 12, Const.BLANK_CHAR, false).getBytes());
            cmdUpPlatFormMsgPostQueryAck.setInfoContent(platformMsg.getInfoContent() == null ? "".getBytes() : platformMsg.getInfoContent().getBytes());
            return mLink.sendData(cmdUpPlatFormMsgPostQueryAck.getSendBuffer()) != null;
        } else if ("02".equals(platformMsg.getReqType())) {
            CmdUpPlatFormMsgInfoAck cmdUpPlatFormMsgInfoAck = new CmdUpPlatFormMsgInfoAck();
            cmdUpPlatFormMsgInfoAck.setInfoId(platformMsg.getInfoId().byteValue());
            return mLink.sendData(cmdUpPlatFormMsgInfoAck.getSendBuffer()) != null;
        } else {
            KKLog.info("ReqType 不存在匹配值!");
        }
        return false;

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

}

package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownTotalRecvBackMsgReq   
 * 类描述：   
 ***************************** 
 * 4.5.2.1接收车辆定位信息数量通知消息 
 * 链路类型：从链路。
 * 消息方向：上级平台往下级平台。 
 * 业务类型标识：DOWN_TOTAL_RECV_BACK_MSG。 
 * 描述：上级平台向下级平台定量通知已经收到下级平台上传的车辆定位信息数量
 *     （如：每收到10,000条车辆定位信息通知一次）。
 *      本条消息不需下级平台应答。
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 上午10:56:25   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownTotalRecvBackMsgReq extends CmdHead {
	
	private int dynamicInfoTotal;//start_time至end_time共收到的车辆定位信息
	private long  startTime;//开始时间
	private long endTime;//结束时间

	public CmdDownTotalRecvBackMsgReq(){
	}
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.dynamicInfoTotal = channelBuffer.readInt();
		this.startTime = channelBuffer.readLong();
		this.endTime = channelBuffer.readLong();
		KKLog.info("DOWN_TOTAL_RECV_BACK_MSG------4.5.2.1接收车辆定位信息数量通知消息 ");
		
	}

	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdBodySize() {
		return 4+8+8;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public int getDynamicInfoTotal() {
        return dynamicInfoTotal;
    }

    public void setDynamicInfoTotal(int dynamicInfoTotal) {
        this.dynamicInfoTotal = dynamicInfoTotal;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}

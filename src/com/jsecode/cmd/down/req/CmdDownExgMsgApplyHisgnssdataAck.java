package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownExgMsgApplyHisgnssdataAck   
 * 类描述：   
 ***************************** 
 * 4.5.3.2.9补发车辆定位信息应答消息
 * 子业务类型标识：DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK。 
 * 描述：本条消息是上级平台应答下级平台发送的补发车辆定位信息请求消息，即 UP_EXG_MSG_APPLY_HISGNSSDATA_REQ
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午03:04:30   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 *
 */
public class CmdDownExgMsgApplyHisgnssdataAck extends CmdHeadSubBizWithCar {
	private byte result;// 补发车辆定位信息应答结果{0x00 成功 上级平台即刻补发,0x01 成功 上级平台择机补发,0x02  失败 上级平台无对应申请的定位数据，0x03 失败 申请内容不正确，0x04 其他原因}
	
	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.result = channelBuffer.readByte();
		KKLog.info("DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK------4.5.3.2.9补发车辆定位信息应答消息");
	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {

	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }
}

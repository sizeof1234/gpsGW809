/**
 * 
 */
package com.jsecode.cmd.down.req;

import com.jsecode.cmd.CmdHeadSubBizWithCar;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**   
 *    
 * 项目名称：gpsGW809   
 * 类名称：CmdDownCtrlMsgTakeTravelReq   
 * 类描述：   
 ***************************** 
 * 4.5.6.2.5 上报车辆行驶记录请求消息
 * 子业务类型标识：DOWN_CTRL_MSG_TAKE_TRAVEL_REQ
 * 描述：上级平台向下级平台下发上报车辆行驶记录请求消息
 ***************************** 
 * 创建人：zhaorg   
 * 创建时间：2014-2-17 下午04:47:28   
 * 修改人：   
 * 修改时间：   
 * 修改备注：   
 * @version    
 *    
 **/
public class CmdDownCtrlMsgTakeTravelReq extends CmdHeadSubBizWithCar {

	private byte commandType;//命令字
	public CmdDownCtrlMsgTakeTravelReq() {
	}

	@Override
	protected void disposeCmdSubBizData(ChannelBuffer channelBuffer) {
		this.commandType = channelBuffer.readByte();
		KKLog.info("DOWN_CTRL_MSG_TAKE_TRAVEL_REQ------4.5.6.2.5 上报车辆行驶记录请求消息");

	}

	@Override
	protected void fillCmdSubBizData(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.commandType);
	}

	@Override
	protected int getCmdSubBizDataSize() {
		return 1;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

    public byte getCommandType() {
        return commandType;
    }

    public void setCommandType(byte commandType) {
        this.commandType = commandType;
    }
}

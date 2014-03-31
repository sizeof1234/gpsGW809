package com.jsecode.cmd.down.resp;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;
/**
 *
 *
 * 项目名称：gpsGW809
 * 类名称：CmdDownLinktestResp
 * 类描述：
 *****************************
 * 4.5.1.14从链路连接保持应答消息
 * 链路类型：从链路。
 * 消息方向：下级平台往上级平台。
 * 业务数据类型标识：DOWN_LINKTEST_RSP。
 * 描述：下级平台收到上级平台链路连接保持请求消息后，向上级平台返回从链路连接保持应答消息，保持从链路连接状态.
 *       从链路连接保持应答消息，数据体为空.
 *****************************
 * 创建人：zhaorg
 * 创建时间：2014-2-17 下午04:12:06
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version
 *
 *
 */
public class CmdDownLinktestResp extends CmdHead {
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
	}

	@Override
    protected void fillCmdBody(ChannelBuffer channelBuffer) {
        KKLog.info("DOWN_LINKTEST_RSP------4.5.1.14从链路连接保持应答消息");
    }

	@Override
	protected int getCmdBodySize() {
		return 0;
	}

	@Override
	public String getDBSaveContent() {
		return EMPTY_STR;
	}

}

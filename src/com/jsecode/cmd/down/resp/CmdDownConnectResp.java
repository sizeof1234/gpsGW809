/**
 * @author 	Jadic
 * @created 2014-2-13
 */
package com.jsecode.cmd.down.resp;

import com.jsecode.cmd.CmdHead;
import com.jsecode.utils.KKLog;
import org.jboss.netty.buffer.ChannelBuffer;

/**
 *
 *
 * 项目名称：gpsGW809
 * 类名称：CmdDownConnectResp
 * 类描述：
 *****************************
 * 4.5.1.10从链路连接应答信息
 * 链路类型：从链路。
 * 消息方向：下级平台往上级平台。
 * 业务数据类型标识：DOWN_CONNECT_RSP。
 * 描述：下级平台作为服务端向上级平台客户端返回从链路连接应答消息，上级平台在接收到该应答消息结果后，
 *       根据结果进行链路连接处理
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
public class CmdDownConnectResp extends CmdHead {
	
	private byte result;//验证结果{00x0 成功,00x1 VERIFY_CODE 错误,00x2 资源紧张，稍后再连接,00x3 其他}
	
	public CmdDownConnectResp() {
	}
	
	@Override
	protected int getCmdBodySize() {
		return 1;
	}
	
	@Override
	protected void disposeCmdBody(ChannelBuffer channelBuffer) {
		this.result = channelBuffer.readByte();
	}
	
	@Override
	protected void fillCmdBody(ChannelBuffer channelBuffer) {
		channelBuffer.writeByte(this.result);
        KKLog.info("DOWN_CONNECT_RSP-----4.5.1.10从链路连接应答信息");
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

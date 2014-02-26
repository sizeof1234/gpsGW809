package com.jsecode.link;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelFuture;

/**
 * 主从链接公共接口
 */
public interface IMainSubLink {
	
	/**
	 * 发送数据
	 * @param channelBuffer
	 * @return
	 */
	ChannelFuture sendData(ChannelBuffer channelBuffer);
	
	/**
	 * 链路上有链接形成
	 * @return
	 */
	boolean isChannelConnected();
	
	/**
	 * 关闭链路<p>
	 * 主链路不再主动连接上级平台<p>
	 * 从链路不再接受上级平台连接
	 */
	void closeLink();
	
	/**
	 * 是否是主链路
	 * @return
	 */
	boolean isMainLink();
	
	/**
	 * 获取校验码，上级平台提供
	 * @return
	 */
	public int getVerifyCode();
	
	/**
	 * 设置校验码，上级平台提供
	 * @param verifyCode
	 */
	public void setVerifyCode(int verifyCode);
}

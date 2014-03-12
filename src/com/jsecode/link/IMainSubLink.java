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
	 * 链路最后一次收到完整协议包数据时间
	 * @return
	 */
	long getLastRecvDataTime();
	
	/**
	 * 启动链路
	 */
	void start();
	
	/**
	 * 关闭链路<p>
	 * 主链路不再主动连接上级平台<p>
	 * 从链路不再接受上级平台连接
	 */
	void closeLink();
	
	/**
	 * 链路是否被手动停止
	 * @return
	 */
	boolean isStopped();
	
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

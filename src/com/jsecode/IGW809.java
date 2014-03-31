package com.jsecode;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.link.IMainSubLink;

/**
 * 809下级平台服务
 */
public interface IGW809 {
	/**
	 * 处理链路连接
	 * @param link
	 */
	void doOnLinkConnected(IMainSubLink link);
	
	/**
	 * 处理链路断开
	 * @param link
	 */
	void doOnLinkDisconnected(IMainSubLink link);
	
	/**
	 * 处理主从链路接收到的809数据
	 * @param channelBuffer
	 * @param link
	 */
	void dispose809Data(ChannelBuffer channelBuffer, IMainSubLink link);	
	
	/**
	 * 获取主链路
	 * @param isSwitchedIfNotConnected	true:主链路断开时，返回从链路
	 * @return
	 */
	IMainSubLink getMainLink(boolean isSwitchedIfNotConnected);
	
	/**
	 * 获取从链路
	 * @param isSwitchedIfNotConnected	true:从链路断开时，返回主链路
	 * @return
	 */
	IMainSubLink getSubLink(boolean isSwitchedIfNotConnected);
	
}

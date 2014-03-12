package com.jsecode;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.link.IMainSubLink;

/**
 * 809下级平台服务
 */
public interface IGW809 {
	void doOnLinkConnected(IMainSubLink link);
	void doOnLinkDisconnected(IMainSubLink link);
	void dispose809Data(ChannelBuffer channelBuffer, IMainSubLink link);	
	IMainSubLink getMainLink(boolean isSwitchedIfNotConnected);
	IMainSubLink getSubLink(boolean isSwitchedIfNotConnected);
	
}

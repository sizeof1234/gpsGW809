package com.jsecode;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.link.IMainSubLink;

public interface IGW809 {
	
	void dispose809Data(ChannelBuffer channelBuffer, IMainSubLink mainSubLink);	
	IMainSubLink getMainLink(boolean isSwitchedIfNotConnected);
	IMainSubLink getSubLink(boolean isSwitchedIfNotConnected);
}

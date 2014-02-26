package com.jsecode.tcp.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public interface ITcpServer {
	
	public void setConnectedChannel(Channel channel);
	public void disconnectChannel(Channel channel);
	public void disposeRecvData(ChannelBuffer buffer);

}

package com.jsecode.tcp.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public interface ITcpClient {
	
	public void setConnectedChannel(Channel channel);
	public void disconnectChannel(Channel channel);
	public void disposeRecvData(ChannelBuffer buffer);

}

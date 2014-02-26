package com.jsecode.tcp.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TcpServerHandler extends SimpleChannelHandler {
	
	private ITcpServer tcpServer;
	
	public TcpServerHandler(ITcpServer tcpServer) {
		this.tcpServer = tcpServer;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
		this.tcpServer.disposeRecvData(buffer);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		this.tcpServer.disconnectChannel(ctx.getChannel());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		this.tcpServer.setConnectedChannel(ctx.getChannel());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		this.tcpServer.disconnectChannel(ctx.getChannel());
	}

}

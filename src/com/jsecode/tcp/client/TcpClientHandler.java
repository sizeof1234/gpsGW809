package com.jsecode.tcp.client;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.jsecode.utils.KKLog;

public class TcpClientHandler extends SimpleChannelHandler {
	
	private ITcpClient tcpClient;
	
	public TcpClientHandler(ITcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
		this.tcpClient.disposeRecvData(buffer); 
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		KKLog.info("exceptionCaught:" + ctx.getChannel().getId() + ",msg:" + e.getCause().getMessage());
		this.tcpClient.disconnectChannel(ctx.getChannel());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
//		KKLog.info("channelConnected:" + ctx.getChannel().getId());
		this.tcpClient.setConnectedChannel(ctx.getChannel());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
//		KKLog.info("channelDisconnected:" + ctx.getChannel().getId());
		this.tcpClient.disconnectChannel(ctx.getChannel());
	}
}

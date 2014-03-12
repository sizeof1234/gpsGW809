/**
 * @author 	Jadic
 * @created 2014-2-20
 */
package com.jsecode.tcp.telnet;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;


public class TelnetServerHandler extends SimpleChannelUpstreamHandler {
	
	private ITelnetServer iTelnetServer;
	
	public TelnetServerHandler(ITelnetServer iTelnetServer) {
		this.iTelnetServer = iTelnetServer;
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		this.iTelnetServer.doOnTelnetConnection(ctx.getChannel());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		String request = (String) e.getMessage();
		this.iTelnetServer.disposeTelnetData(ctx.getChannel(), request.toLowerCase());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getChannel().close();
	}
}

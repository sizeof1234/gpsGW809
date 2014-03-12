/**
 * @author 	Jadic
 * @created 2014-2-20
 */
package com.jsecode.tcp.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.jsecode.utils.KKLog;

public class TelnetServer {

	private final int port;
	private ITelnetServer iTelnetServer;

	public TelnetServer(int port, ITelnetServer iTelnetServer) {
		this.port = port;
		this.iTelnetServer = iTelnetServer;
	}

	public void start() {
		Executor boss = Executors.newCachedThreadPool();
		Executor worker = Executors.newCachedThreadPool();
		ChannelFactory factory = new NioServerSocketChannelFactory(boss, worker);
		ServerBootstrap bootstrap = new ServerBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
		       
				pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
		                8192, Delimiters.lineDelimiter()));
		        pipeline.addLast("decoder", new StringDecoder());
		        pipeline.addLast("encoder", new StringEncoder());
		        pipeline.addLast("handler", new TelnetServerHandler(iTelnetServer));

		        return pipeline;
			}
		});

		bootstrap.bind(new InetSocketAddress(port));
		KKLog.info("telnet server is listening on port:" + port);
	}
}

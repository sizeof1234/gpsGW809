package com.jsecode.tcp.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.jsecode.IGW809;
import com.jsecode.cmd.up.resp.CmdUpCloseLinkInform;
import com.jsecode.link.AbstractMainSubLink;
import com.jsecode.link.IMainSubLink;
import com.jsecode.tcp.TcpDataDecoder;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKSimpleTimer;
import com.jsecode.utils.KKTool;

public class TcpServer extends AbstractMainSubLink implements ITcpServer {
	
	private IGW809 gw809;
	
	private ServerBootstrap bootstrap;
	private int localPort;
	private KKSimpleTimer timer;
	
	public TcpServer(int localPort, IGW809 gw809) {
		this.gw809 = gw809;
		
		this.localPort = localPort;
	}
	
	public void start() {
		try {
			this.setStopped(false);
			ChannelFactory channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
			bootstrap = new ServerBootstrap(channelFactory);
			bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new TcpDataDecoder(), new TcpServerHandler(TcpServer.this));
				}
			});
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.bindAsync(new InetSocketAddress(localPort)).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						startTimer();
						KKLog.info("sub link server started, listening on Port:" + localPort);		
					} else {
						KKLog.info("sub link server bind port[" + localPort + "]failed");
					}
				}
			});
		} catch (Exception e) {
			KKLog.info("fail to start tcp server:" + KKTool.getExceptionTip(e));
		}
	}
	
	private void startTimer() {
		if (this.timer == null) {
			this.setLastRecvDataTime(System.currentTimeMillis());
			timer = new KKSimpleTimer(new Runnable() {
				@Override
				public void run() {
					if (isChannelConnected()) {
						if (isConnectionOvertime()) {
							sendCloseSublinkFromMainLink();
						}
					}
				}
			}, 60, 60);
			timer.start();
		}
	}
	
	private void closeTimer() {
		if (this.timer != null) {
			timer.stop();
		}
		this.timer = null;
	}	
	
	/**
	 * 从主链路通知从链路异常，即将关闭
	 */
	private void sendCloseSublinkFromMainLink() {
		IMainSubLink mainLink = this.gw809.getMainLink(false);
		CmdUpCloseLinkInform cmdUpCloseLinkInform = new CmdUpCloseLinkInform();
		cmdUpCloseLinkInform.setMsgFlagId(Const.UP_CLOSELINK_INFORM);
		cmdUpCloseLinkInform.setErrorCode((byte)0);
		ChannelFuture future = mainLink.sendData(cmdUpCloseLinkInform.getSendBuffer());
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isDone()) {
					Channel channel = getChannel();
					if (channel != null) {
						channel.close();
					}
				}
			}
		});
	}

	@Override
	public void closeLink() {
		closeTimer();
		closeChannel();
		this.setChannel(null);
		this.setStopped(true);
		bootstrap.releaseExternalResources();		
	}

	@Override
	public void setConnectedChannel(Channel channel) {
		closeChannel();
		this.setChannel(channel);
		if (isChannelConnected()) {
			KKLog.info("sub link connected");
		} else {
		}
	}

	@Override
	public void disconnectChannel(Channel channel) {
		//防止出现多个链接，误断最新链接
		Channel lastChannel = this.getChannel();
		if (lastChannel != null && lastChannel.getId() == channel.getId()) {
			this.setChannel(null);
			KKLog.info("sub link disconnected");
		}
	}

	@Override
	public void disposeRecvData(ChannelBuffer buffer) {
		this.setLastRecvDataTime(System.currentTimeMillis());
		this.gw809.dispose809Data(KKTool.getUnescapedBuffer(buffer), this);
	}

	@Override
	public boolean isMainLink() {
		return false;
	}

}

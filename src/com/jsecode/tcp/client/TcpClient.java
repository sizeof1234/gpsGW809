package com.jsecode.tcp.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import com.jsecode.IGW809;
import com.jsecode.cmd.up.req.CmdUpConnectReq;
import com.jsecode.cmd.up.req.CmdUpDisconnectReq;
import com.jsecode.cmd.up.req.CmdUpLinkTestReq;
import com.jsecode.link.AbstractMainSubLink;
import com.jsecode.tcp.TcpDataDecoder;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKSimpleTimer;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;

/**
 * 主链路
 */
public class TcpClient extends AbstractMainSubLink implements ITcpClient{
	
	private IGW809 gw809;
	private SysParams sysParams;
	
	private String serverIp;
	private int serverPort;

	private ClientBootstrap bootstrap;
	
	private KKSimpleTimer heartBeaTimer;
	private KKSimpleTimer reconnectTimer;
	
	public TcpClient (String serverIp, int serverPort, IGW809 gw809) {
		this.gw809 = gw809;
		
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		ChannelFactory cf = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		bootstrap = new ClientBootstrap(cf);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new TcpDataDecoder(), new TcpClientHandler(TcpClient.this));
			}
		});

		this.heartBeaTimer = null;
		this.reconnectTimer = null;
		this.sysParams = SysParams.getInstance();
	}
	
	private void connectServer() {
		if (!this.isStopped() && !isChannelConnected() ) {
			KKLog.info("main link connecting to superior platform:" + this.serverIp + ":" + this.serverPort);
			bootstrap.connect(new InetSocketAddress(this.serverIp, this.serverPort));
		}
	}
	
	public void start() {
		this.setStopped(false);
		connectServer();
	}
	
	@Override
	public void closeLink() {
		this.setStopped(true);
		this.stopReconnectTimer();
		this.stopHeartbeatTimer();
		this.closeChannel();
		this.setChannel(null);
	}
	
	private void startHeartbeatTimer() {
		if (heartBeaTimer == null) {
			this.setLastRecvDataTime(System.currentTimeMillis());
			heartBeaTimer = new KKSimpleTimer(new Runnable() {
				@Override
				public void run() {
					if (isChannelConnected()) {
						if (isConnectionOvertime()) {
							KKLog.info("timeout close main link");
							closeChannel();
						} else {
							SendHeartbeat();
						}
					}
				}
			}, Const.HEARTBEAT_INTERVAL_SECONDS, Const.HEARTBEAT_INTERVAL_SECONDS);
			heartBeaTimer.start();
		}
	}
	
	private void stopHeartbeatTimer() {
		if (heartBeaTimer != null) {
			heartBeaTimer.stop();
		}
		heartBeaTimer = null;
	}
	
	private void startReconnectTimer() {
		if (isStopped()) {
			return;
		}
		
		if (reconnectTimer == null) {
			reconnectTimer = new KKSimpleTimer(new Runnable() {
				@Override
				public void run() {
					connectServer();
				}
			}, Const.RECONNECT_INTERVAL_SECONDS, Const.RECONNECT_INTERVAL_SECONDS);
			reconnectTimer.start();
		}
	}
	
	private void stopReconnectTimer() {
		if (reconnectTimer != null) {
			reconnectTimer.stop();
		}
		reconnectTimer = null;
	}
	
	/**
	 * 主链路登录请求
	 */
	private void login() {
		CmdUpConnectReq cmdUpConnectReq = new CmdUpConnectReq();
		cmdUpConnectReq.setMsgFlagId(Const.UP_CONNECT_REQ);
		cmdUpConnectReq.setUserId(sysParams.getUserId());
		cmdUpConnectReq.setUserPass(KKTool.getFixedLenBytes(sysParams.getUserPass(), 8));
		cmdUpConnectReq.setDownLinkIp(KKTool.getFixedLenBytes(sysParams.getSubLinkIp(), 32));
		cmdUpConnectReq.setDownLinkPort(sysParams.getSubLinkPort());
		this.sendData(cmdUpConnectReq.getSendBuffer());
	}

	/**
	 * 注销请求
	 */
	public void logout() {
		CmdUpDisconnectReq cmdUpDisconnectReq = new CmdUpDisconnectReq();
		cmdUpDisconnectReq.setMsgFlagId(Const.UP_DISCONNECE_REQ);
		cmdUpDisconnectReq.setUserId(sysParams.getUserId());
		cmdUpDisconnectReq.setUserPass(KKTool.getFixedLenBytes(sysParams.getUserPass(), 8));
		this.sendData(cmdUpDisconnectReq.getSendBuffer());
	}

	/**
	 * 发送心跳
	 */
	private void SendHeartbeat() {
		CmdUpLinkTestReq cmdUpLinkTestReq = new CmdUpLinkTestReq();
		cmdUpLinkTestReq.setMsgFlagId(Const.UP_LINKETEST_REQ);
		this.sendData(cmdUpLinkTestReq.getSendBuffer());
	}
	
	@Override
	public void setConnectedChannel(Channel channel) {
		this.setChannel(channel);
		if (this.isChannelConnected()) {
			stopReconnectTimer();
			startHeartbeatTimer();
			this.setLastRecvDataTime(System.currentTimeMillis());
			KKLog.info("main link connected");
			login();
		}
		this.gw809.doOnLinkConnected(this);
	}

	@Override
	public void disconnectChannel(Channel channel) {
		KKLog.info("main link disconnected");
		stopHeartbeatTimer();
		startReconnectTimer();
		this.setChannel(null);
		this.gw809.doOnLinkDisconnected(this);
	}

	@Override
	public void disposeRecvData(ChannelBuffer buffer) {
		this.setLastRecvDataTime(System.currentTimeMillis());
		this.gw809.dispose809Data(KKTool.getUnescapedBuffer(buffer), this);
	}

	@Override
	public boolean isMainLink() {
		return true;
	}

}

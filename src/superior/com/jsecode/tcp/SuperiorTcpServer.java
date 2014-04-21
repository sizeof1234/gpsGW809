package superior.com.jsecode.tcp;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import superior.com.jsecode.biz.ThreadDisposeTcpChannelData;

import com.jsecode.ISuperiorToInferior;
import com.jsecode.cmd.CmdHead;
import com.jsecode.tcp.TcpDataDecoder;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKSimpleTimer;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;

/**
 * 模拟上级平台主链路
 * @author 	Jadic
 * @created 2014-4-2
 */
public class SuperiorTcpServer {
	
	private ServerBootstrap bootstrap;
	private int localPort;
	
	private final Map<Integer, TcpChannel> tcpChannels;
	private KKSimpleTimer checkTimeoutTimer;
	
	private ExecutorService threadPoolDisposeTcpData;
	
	private ISuperiorToInferior iSuperiorToInferior;

	/**
	 * iSuperiorToInferior为null时，则认为是独立启动的模拟上级平台服务，不进行转发
	 * @param iSuperiorToInferior
	 */
	public SuperiorTcpServer(ISuperiorToInferior iSuperiorToInferior) {
		this.localPort = SysParams.getInstance().getSuperiorPort();
		this.tcpChannels = new ConcurrentHashMap<Integer, TcpChannel>();
		this.threadPoolDisposeTcpData = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.iSuperiorToInferior = iSuperiorToInferior;
	}

	public void start() {
		try {
			ChannelFactory channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
			bootstrap = new ServerBootstrap(channelFactory);
			bootstrap.setPipelineFactory(new ChannelPipelineFactory(){
				@Override
				public ChannelPipeline getPipeline() throws Exception {
					return Channels.pipeline(new TcpDataDecoder(), new SuperiorTcpServerHandler(SuperiorTcpServer.this));
				}
			});
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			bootstrap.bindAsync(new InetSocketAddress(localPort)).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						KKLog.info("superior main link server started, listening on Port:" + localPort);		
					} else {
						KKLog.info("superior main link server bind port[" + localPort + "]failed");
					}
				}
			});
			
			startCheckTimeoutTimer();
		} catch (Exception e) {
			KKLog.info("fail to start superior main link server:" + KKTool.getExceptionTip(e));
		}
	}
	
	public void stop() {
		stopCheckTimeoutTimer();
		clearTcpChannels();
		this.threadPoolDisposeTcpData.shutdown();
		bootstrap.releaseExternalResources();
	}
	
	/**
	 * start timer to check all connected channels timeout 
	 */
	private void startCheckTimeoutTimer() {
		this.checkTimeoutTimer = new KKSimpleTimer(new Runnable() {
			final static long TIME_OUT = 1000 * 60 * 3;
			
			@Override
			public void run() {
				if (tcpChannels.size() > 0) {
					Iterator<Entry<Integer, TcpChannel>> iterator = tcpChannels.entrySet().iterator();
					
					Entry<Integer, TcpChannel> entry = null;
					TcpChannel tcpChannel = null; 
					while (iterator.hasNext()) {
						entry = iterator.next();
						tcpChannel = entry.getValue();
						if (System.currentTimeMillis() - tcpChannel.getLastRecvDataTime() >= TIME_OUT) {
							tcpChannel.close();
							tcpChannels.remove(entry.getKey());
						}
					}
				}
			}
		}, 60, 60);
		this.checkTimeoutTimer.start();
	}
	
	private void stopCheckTimeoutTimer() {
		if (this.checkTimeoutTimer != null) {
			this.checkTimeoutTimer.stop();
			this.checkTimeoutTimer = null;
		}
	}

	/**
	 * clear all connected tcp channels from map
	 */
	private void clearTcpChannels() {
		if (tcpChannels.size() > 0) {
			Iterator<Entry<Integer, TcpChannel>> iterator = tcpChannels.entrySet().iterator();
			TcpChannel tcpChannel = null; 
			while (iterator.hasNext()) {
				tcpChannel = iterator.next().getValue();
				tcpChannel.close();
			}
			tcpChannels.clear();
		}
	}

	/**
	 * add new connected tcp channel
	 * @param channel new connected channel
	 * @return new tcp channel
	 */
	protected TcpChannel addTcpChannel(Channel channel) {
		TcpChannel tcpChannel = new TcpChannel(channel, this);
		this.tcpChannels.put(channel.getId(), tcpChannel);
		return tcpChannel;
	}
	
	/**
	 * get tcp channel via channelId({@link org.jboss.netty.channel.Channel.getId})
	 * @param channelId
	 * @return
	 */
	public TcpChannel getTcpChannel(Integer channelId) {
		return tcpChannels.get(channelId);
	}
	
	protected void removeTcpChannel(Integer channelId) {
		TcpChannel tcpChannel = tcpChannels.remove(channelId);
		if (tcpChannel != null) {
			tcpChannel.close();
		}
	}
	
	/**
	 * submit dispose tcp data task to thread pool
	 * @param tcpChannel
	 */
	protected void executeDisposeTask(TcpChannel tcpChannel) {
		this.threadPoolDisposeTcpData.execute(new ThreadDisposeTcpChannelData(tcpChannel));
	}
	
	/**
	 * forward command to standard inferior server if current superior tcp server is not standalone
	 * @param cmd
	 */
	public void forwardData(CmdHead cmd) {	
		if (this.iSuperiorToInferior != null) {
			this.iSuperiorToInferior.forwardCmd(cmd);
		}
	}
	
	/**
	 * get current connected tcp channel count
	 * @return
	 */
	public int getClientsCount() {
		return this.tcpChannels.size();
	}
	
}

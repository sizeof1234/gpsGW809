package superior.com.jsecode.tcp;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

/**
 * @author 	Jadic
 * @created 2014-4-9
 */
public class SuperiorTcpServerHandler extends SimpleChannelHandler {
	
	private SuperiorTcpServer tcpServer;
	
	public SuperiorTcpServerHandler(SuperiorTcpServer tcpServer) {
		this.tcpServer = tcpServer;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
		TcpChannel tcpChannel = this.tcpServer.getTcpChannel(ctx.getChannel().getId());
		if (tcpChannel != null) {
			tcpChannel.addRecvDataAndDispose(KKTool.getUnescapedBuffer(buffer));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		TcpChannel tcpChannel = this.tcpServer.getTcpChannel(ctx.getChannel().getId());
		this.tcpServer.removeTcpChannel(ctx.getChannel().getId());
		KKLog.error(ctx.getChannel().getRemoteAddress() + " exception caught:" + KKTool.getExceptionTip(e.getCause()));
		KKLog.info(tcpChannel + " isClosed=" + tcpChannel.isClosed() + ", isDisposing=" + tcpChannel.isDisposing());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		TcpChannel tcpChannel = this.tcpServer.addTcpChannel(ctx.getChannel());
		KKLog.info("a tcp client[" + tcpChannel + "] connected to SuperiorTcpServer, total:" + tcpServer.getClientsCount());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		this.tcpServer.removeTcpChannel(ctx.getChannel().getId());
	}

}

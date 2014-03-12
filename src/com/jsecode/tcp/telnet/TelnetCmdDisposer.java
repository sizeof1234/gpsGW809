/**
 * @author 	Jadic
 * @created 2014-2-27
 */
package com.jsecode.tcp.telnet;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.jsecode.IGW809;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.Const;
import com.jsecode.utils.KKTool;

/**
 * telnet命令处理
 */
public final class TelnetCmdDisposer {
	
	private IGW809 gw809;
	private IMainSubLink mainLink;
	private IMainSubLink subLink;

	public TelnetCmdDisposer(IGW809 gw809) {
		this.gw809 = gw809;
		mainLink = this.gw809.getMainLink(false);
		subLink = this.gw809.getSubLink(false);
	}

	public void doOnTelnetConnection(Channel channel) {
		if (channel != null) {
			String response = Const.TELNET_CONNECT_WELCOME + "[809 server]$ ";
			channel.write(response);
		}
	}

	public void disposeTelnetData(Channel channel, String request) {
		if (channel != null) {
			if (!KKTool.isStrNullOrBlank(request)) {
				boolean isQuit = false;
				String response = Const.EMPTY_STR;
				
				if (request.equals(Const.TELNET_CMD_QUIT)) {
					response = Const.TELNET_DISCONNECT_TIP;
					isQuit = true;
				} else if (request.equals(Const.TELNET_CMD_HELP)) {
					response = KKTool.getTelnetHelpContent() + "\r\n";
				} else if (request.equals(Const.TELNET_CMD_START_MAINLINK)) {
					if (mainLink.isStopped()) {
						response = "starting main link\r\n";
						this.startMainLinkManually();
						response = response + "main link started\r\n\r\n";
					} else {
						response = "main link has started\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_END_MAINLINK)) {
					if (mainLink.isStopped()) {
						response = "main link has ended\r\n\r\n";
					} else {
						response = "ending main link\r\n";
						this.stopMainLinkManually();
						response = response + "main link ended\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_START_SUBLINK)) {
					if (subLink.isStopped()) {
						response = "starting sub lnik\r\n";
						this.startSubLinkManually();
						response = response + "main link ended\r\n\r\n";
					} else {
						response = "sub link has started\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_END_SUBLINK)) {
					if (subLink.isStopped()) {
						response = "sub link has ended\r\n\r\n";
					} else {
						response = "ending sublink\r\n";
						this.stopSubLinkManually();
						response = response + "sub link ended\r\n\r\n";
					}
				} else if (request.equals(Const.TELNET_CMD_LINK_STATUS)) {
					response = "main link status:" + (mainLink.isChannelConnected() ? "connected" : "not connected") + "\r\n"
							 + " sub link status:" + (subLink.isChannelConnected() ? "connected" : "not connected") + "\r\n\r\n";
				} else {
					response = "Your input command[" + request + "] is not in command list.\r\n" 
							 + "Input 'help' for all supported commands\r\n\r\n";
				}
				response = response + "[809 server]$ ";
				ChannelFuture future = channel.write(response);
				if (isQuit) {
					future.addListener(ChannelFutureListener.CLOSE);
				}
			} else {
				channel.write("Your input is empty, please input valid command mentioned above\r\n\r\n");
			}
		}
	}

	/**
	 * 手动关闭主链路
	 */
	private void stopMainLinkManually() {
		if (!mainLink.isStopped()) {
			mainLink.closeLink();
		}
	}
	
	/**
	 * 手动启动主链路
	 */
	private void startMainLinkManually() {
		if (mainLink.isStopped()) {
			mainLink.start();
		}
	}
	
	/**
	 * 手动关闭从链路
	 */
	private void stopSubLinkManually() {
		if (!subLink.isStopped()) {
			subLink.closeLink();
		}
	}
	
	/**
	 * 手动启动从链路
	 */
	private void startSubLinkManually() {
		if (subLink.isStopped()) {
			subLink.start();
		}
	}
}

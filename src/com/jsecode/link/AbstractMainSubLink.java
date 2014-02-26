/**
 * @author 	Jadic
 * @created 2014-2-24
 */
package com.jsecode.link;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import com.jsecode.utils.Const;
import com.jsecode.utils.KKTool;

public abstract class AbstractMainSubLink implements IMainSubLink {
	
	private Channel channel;
	private long lastRecvDataTime;
	private boolean isChannelConnected;
	private boolean isStopped;
	
	private int verifyCode;//校验码
	
	protected AbstractMainSubLink() {
		this.channel = null;
		this.isChannelConnected = false;
		this.isStopped = false;
		this.verifyCode = 0;
	}
	
	@Override
	public boolean isChannelConnected() {
		return isChannelConnected;
	}
	
	@Override
	public ChannelFuture sendData(ChannelBuffer buffer) {
		if (this.channel != null) {
			return this.channel.write(KKTool.getEscapedBuffer(buffer));
		}
		return null;
	}

	protected void closeChannel() {
		if (this.channel != null) {
			this.channel.close();
		}
	}

	public boolean isStopped() {
		return isStopped;
	}

	protected void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

	protected boolean isConnectionOvertime() {
		return System.currentTimeMillis() - lastRecvDataTime >= Const.OVER_TIME_MILLISECONDS;
	}

	public int getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(int verifyCode) {
		this.verifyCode = verifyCode;
	}

	public long getLastRecvDataTime() {
		return lastRecvDataTime;
	}

	protected void setLastRecvDataTime(long lastRecvDataTime) {
		this.lastRecvDataTime = lastRecvDataTime;
	}

	public Channel getChannel() {
		return channel;
	}
	
	protected void setChannel(Channel channel) {
		this.channel = channel;
		this.isChannelConnected = this.channel != null;
	}
}

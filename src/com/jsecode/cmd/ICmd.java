package com.jsecode.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.bean.GpsCmdBean;

public interface ICmd {
	
	byte[] ZERO_BYTES = new byte[0];
	GpsCmdBean EMPTY_GPS_BEAN = new GpsCmdBean();

	//命令长度（从头标识到尾标识完整的长度）
	public int getCmdSize();

	//解析收到的命令
	public boolean disposeData(ChannelBuffer channelBuffer);
	
	/**
	 * 获取给命令完整可发送的channel buffer<p>
	 * 切记：获取前先将每个具体命令的字段设置好
	 * @return 可直接发送的channel buffer
	 */
	public ChannelBuffer getSendBuffer();
	
}

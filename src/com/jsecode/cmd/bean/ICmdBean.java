/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.bean;

import org.jboss.netty.buffer.ChannelBuffer;

public interface ICmdBean {

	public int getBeanSize();

	//解析收到的命令
	public boolean disposeData(ChannelBuffer channelBuffer);
	
	//填充要发出的命令
	public boolean fillChannelBuffer(ChannelBuffer channelBuffer);
}

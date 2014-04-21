package com.jsecode.cmd;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import com.jsecode.cmd.bean.GpsCmdBean;

public interface ICmd {
	
	String EMPTY_STR = "";
	byte[] ZERO_BYTES = new byte[0];
	GpsCmdBean EMPTY_GPS_BEAN = new GpsCmdBean();
	List<GpsCmdBean> GPS_LIST_EMPTY = new ArrayList<GpsCmdBean>(0);

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
	
	/**
	 * 获取命令存储数据库时需拼装的内容<br>
	 * 由每个具体的命令类实现
	 * @return
	 */
	public String getDBSaveContent();
}

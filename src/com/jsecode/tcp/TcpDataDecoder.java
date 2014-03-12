package com.jsecode.tcp;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

public class TcpDataDecoder extends FrameDecoder {
	
	private final static int CMD_MIN_SIZE = 26;
	private final static byte CMD_HEAD_FLAG = 0x5B;
	private final static byte CMD_END_FLAG = 0x5D;
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		Object message = e.getMessage();
		if (message instanceof ChannelBuffer) {
			ChannelBuffer buffer = (ChannelBuffer)message;
			KKLog.recvData("[" + buffer.readableBytes() + "] " + KKTool.channelBufferToHexStr(buffer));
		}
		
		super.messageReceived(ctx, e);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		int readableBytes = buffer.readableBytes();
		if (readableBytes < CMD_MIN_SIZE) {
			return null;
		}
		
		int headIndex = buffer.indexOf(buffer.readerIndex(), buffer.readerIndex() + readableBytes, CMD_HEAD_FLAG);
		if (headIndex < 0) {//no valid data,skip all
			buffer.skipBytes(readableBytes);
			return null;
		}
		
		int endIndex = buffer.indexOf(headIndex + CMD_MIN_SIZE - 1, buffer.readerIndex() + readableBytes, CMD_END_FLAG);
		if (endIndex < 0) {//no end flag found
			if (readableBytes < 2048) {//wait for the end flag 
				return null;
			} else {//if the readable bytes is greater than 2K, skip the wrong data(it should not happen)
				buffer.skipBytes(readableBytes);
				return null;
			}
		}
		
		int wholePackSize = endIndex - headIndex + 1;		
		
		if (wholePackSize >= CMD_MIN_SIZE) {//correct data
			buffer.readerIndex(headIndex);//set the headIndex to next readerIndex;
			return buffer.readBytes(wholePackSize);
		} else {//skip the invalid data
			if (endIndex < buffer.capacity() - 1) {
				buffer.readerIndex(endIndex + 1);
			} else {
				buffer.readerIndex(endIndex);
			}
			return null;
		}
	}

}

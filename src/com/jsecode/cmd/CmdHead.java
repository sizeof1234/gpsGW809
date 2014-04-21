package com.jsecode.cmd;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import com.jsecode.utils.Const;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;

public abstract class CmdHead implements ICmd {
	
	private byte headFlag;//头标识
	private int msgLen;//数据长度(包括头标识、数据头、数据体和尾标识)
	private int msgSNo;//报文序列号
	private short msgFlagId;//业务数据类型	
	private int msgGnssCenterId;//下级平台接入码，上级平台给下级平台分配唯一标识码
	private byte[] verFlag;//协议版本好标识，上下级平台之间采用的标准协议版编号
	private byte encryptFlag;//报文加密标识位b: 0表示报文不加密，1表示报文加密
	private int encryptKey;//数据加密的密匙，长度为4个字节
	
	private byte endFlag;//尾标识
	
	private int cmdHeadSize;
	private int cmdEndSize;
	
	private static int cmdSNo = 0;
	
	final static byte ENCRYPT_FLAG_UNENCRYPTED = 0;//报文未加密
	final static byte ENCRYPT_FLAG_ENCRYPTED 	 = 1;//报文加密
	
	public CmdHead() {
		this.verFlag = new byte[3];
		this.cmdHeadSize = 1 + 4 + 4 + 2 + 4 + this.verFlag.length + 1 + 4;
		this.cmdEndSize = 3;
	}
	
	/**
	 * 获取每个具体命令体的长度
	 * @return
	 */
	protected abstract int getCmdBodySize();
	
	/**
	 * 解析每个具体命令消息体的内容
	 * @param channelBuffer
	 */
	protected abstract void disposeCmdBody(ChannelBuffer channelBuffer);
	
	/**
	 * 填充每个具体命令消息体的内容
	 * @param channelBuffer
	 */
	protected abstract void fillCmdBody(ChannelBuffer channelBuffer);

	@Override
	public int getCmdSize() {
		return getCmdHeadEndSize() + getCmdBodySize();
	}

	@Override
	public boolean disposeData(ChannelBuffer channelBuffer) {
		if (channelBuffer != null && channelBuffer.readableBytes() >= this.getCmdSize()) {
			this.headFlag = channelBuffer.readByte();
			this.msgLen = channelBuffer.readInt();
			this.msgSNo = channelBuffer.readInt();
			this.msgFlagId = channelBuffer.readShort();
			this.msgGnssCenterId = channelBuffer.readInt();
			channelBuffer.readBytes(this.verFlag);
			this.encryptFlag = channelBuffer.readByte();
			this.encryptKey = channelBuffer.readInt();
			
			//解析的数据可先不检测CRC
			
			int sIndex = channelBuffer.readerIndex();
			int eIndex = sIndex + getCmdBodySize();
			if (this.encryptFlag == ENCRYPT_FLAG_ENCRYPTED) {
				this.encryptData(channelBuffer, sIndex, eIndex, encryptKey);
			}
			
			disposeCmdBody(channelBuffer);
			return true;
		}
		return false;
	}

	@Override
	public ChannelBuffer getSendBuffer() {
		setCmdCommonField();
		ChannelBuffer channelBuffer = ChannelBuffers.buffer(this.getCmdSize());
		fillChannelBuffer(channelBuffer);
		return channelBuffer;
	}

	/**
	 * 设置命令中的公共字段
	 */
	private void setCmdCommonField() {
		this.headFlag = Const.CMD_HEAD;
		this.msgLen = getCmdSize();
		this.msgSNo = getCmdSNo();
		this.msgGnssCenterId = SysParams.getInstance().getGnssCenterId();
		this.setVerFlag(KKTool.strToHexBytes(SysParams.getInstance().getVerFlag(), 3, '0'));
		if (SysParams.getInstance().isEncrypted()) {
			this.encryptFlag = ENCRYPT_FLAG_ENCRYPTED;
			this.encryptKey = SysParams.getInstance().getEncryptionKey(); 
		} else {
			this.encryptFlag = ENCRYPT_FLAG_UNENCRYPTED;
		}
		
		this.endFlag = Const.CMD_END;
	}
	
	/**
	 * 根据命令每个字段封装完整的可直接发送的Channel buffer
	 * @param channelBuffer
	 * @return
	 */
	private boolean fillChannelBuffer(ChannelBuffer channelBuffer) {
		if (channelBuffer.writableBytes() >= this.getCmdSize()) {
			channelBuffer.writeByte(this.headFlag);
			channelBuffer.writeInt(this.msgLen);
			channelBuffer.writeInt(this.msgSNo);
			channelBuffer.writeShort(this.msgFlagId);
			channelBuffer.writeInt(this.msgGnssCenterId);
			channelBuffer.writeBytes(this.verFlag);
			channelBuffer.writeByte(encryptFlag);
			channelBuffer.writeInt(encryptKey);

			int sIndex = channelBuffer.writerIndex();
			fillCmdBody(channelBuffer);
			int eIndex = channelBuffer.writerIndex();
			if (this.encryptFlag == ENCRYPT_FLAG_ENCRYPTED) {
				this.encryptData(channelBuffer, sIndex, eIndex, encryptKey);
			}
			short crc = (short)KKTool.getCRC16(channelBuffer, 1, channelBuffer.writerIndex());
			channelBuffer.writeShort(crc);
			channelBuffer.writeByte(this.endFlag);
			return true;
		}
		return false;
	}
	
	/**
	 * 数据加解密算法
	 * @param buffer
	 * @param sIndex（包含） 
	 * @param eIndex（不含）
	 * @param key
	 */
	private void encryptData(ChannelBuffer buffer, int sIndex, int eIndex, long key) {
		if (sIndex < 0 || eIndex > buffer.capacity()) {
			return;
		}
		if (key == 0) {
			key = 1;
		}
		long m1 = SysParams.getInstance().getM1();
		if (m1 == 0) {
			m1 = 1;
		}
		long ia1 = SysParams.getInstance().getIa1();
		long ic1 = SysParams.getInstance().getIc1();
		byte b = 0;
		for (int i = sIndex; i < eIndex; i ++) {
			key = ia1 * (key % m1) + ic1;
			key &= 0xFFFFFFFFL;
			b = buffer.getByte(i);
			b ^= (byte)((key >>> 20) & 0xFF);
			buffer.setByte(i, b);
		}
	}
	
	/**
	 * 获取命令头尾固定长度
	 * @return
	 */
	protected int getCmdHeadEndSize() {
		return this.cmdHeadSize + this.cmdEndSize;
	}
	
	/**
	 * 判断字节数组长度是否一致
	 * @param buf1
	 * @param buf2
	 * @return true if both buf1 and buf2 are not null, and length of buf1 equals length of buf2
	 */
	public boolean isByteArraySameSize(byte[] buf1, byte[] buf2) {
		return buf1 != null && buf2 != null && buf1.length == buf2.length;
	}
	
	/**
	 * 命令序列号
	 * @return
	 */
	private static int getCmdSNo() {
		if (cmdSNo <= Integer.MAX_VALUE) {
			return cmdSNo ++;
		} else {
			cmdSNo = 0;
			return 0;
		}
	}

	public byte getHeadFlag() {
		return headFlag;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public int getMsgSNo() {
		return msgSNo;
	}

	public short getMsgFlagId() {
		return msgFlagId;
	}
	
	public void setMsgFlagId(short msgFlagId) {
		this.msgFlagId = msgFlagId;
	}

	public int getMsgGnssCenterId() {
		return msgGnssCenterId;
	}

	public byte[] getVerFlag() {
		return verFlag;
	}

	private void setVerFlag(byte[] verFlag) {
		if (isByteArraySameSize(this.verFlag, verFlag)) {
			this.verFlag = verFlag;
		}
	}

	public byte getEncryptFlag() {
		return encryptFlag;
	}

	public int getEncryptKey() {
		return encryptKey;
	}

	public byte getEndFlag() {
		return endFlag;
	}

}


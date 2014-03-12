/**
 * @author 	Jadic
 * @created 2014-2-14
 */
package com.jsecode.cmd.bean;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * 809命令中所有GPS数据字段对应类
 */
public class GpsCmdBean extends AbstractCmdBean {
	
	private byte encrypt;
	private byte[] date;
	private byte[] time;
	private int lon;
	private int lat;
	private short speed;
	private short speedRec;
	private int miles;
	private short direction;
	private short altitude;
	private int state;
	private int alarm;	

	public GpsCmdBean() {
		this.encrypt = 0;
		this.date = new byte[4];
		this.time = new byte[3];
	}

	@Override
	public int getBeanSize() {
		return 1 
			 + this.date.length 
			 + this.time.length 
			 + 4 
			 + 4 
			 + 2 
			 + 2 
			 + 4 
			 + 2 
			 + 2 
			 + 4 
			 + 4;
	}
	
	public static int getCmdBeanSize() {
		return  1 
			   + 4
			   + 3 
			   + 4 
			   + 4 
			   + 2 
			   + 2 
			   + 4 
			   + 2 
  			   + 2 
			   + 4 
			   + 4;
	}

	@Override
	public boolean disposeData(ChannelBuffer channelBuffer) {
		if (channelBuffer != null && channelBuffer.readableBytes() >= this.getBeanSize()) {
			this.encrypt = channelBuffer.readByte();
			channelBuffer.readBytes(this.date);
			channelBuffer.readBytes(this.time);
			this.lon = channelBuffer.readInt();
			this.lat = channelBuffer.readInt();
			this.speed = channelBuffer.readShort();
			this.speedRec = channelBuffer.readShort();
			this.miles = channelBuffer.readInt();
			this.direction = channelBuffer.readShort();
			this.altitude = channelBuffer.readShort();
			this.state = channelBuffer.readInt();
			this.alarm = channelBuffer.readInt();
			return true;
		}
		return false;
	}

	@Override
	public boolean fillChannelBuffer(ChannelBuffer channelBuffer) {
		if (channelBuffer != null && channelBuffer.writableBytes() >= this.getBeanSize()) {
			channelBuffer.writeByte(this.encrypt);
			channelBuffer.writeBytes(this.date);
			channelBuffer.writeBytes(this.time);
			channelBuffer.writeInt(this.lon);
			channelBuffer.writeInt(this.lat);
			channelBuffer.writeShort(this.speed);
			channelBuffer.writeShort(this.speedRec);
			channelBuffer.writeInt(this.miles);
			channelBuffer.writeShort(this.direction);
			channelBuffer.writeShort(this.altitude);
			channelBuffer.writeInt(this.state);
			channelBuffer.writeInt(this.alarm);
			return true;
		}
		return false;
	}

	public byte getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(byte encrypt) {
		this.encrypt = encrypt;
	}

	public byte[] getDate() {
		return date;
	}

	public void setDate(byte[] date) {
		if (isByteArraySameSize(this.date, date)) {
			this.date = date;
		}
	}

	public byte[] getTime() {
		return time;
	}

	public void setTime(byte[] time) {
		if (isByteArraySameSize(this.time, time)) {
			this.time = time;
		}
	}

	public int getLon() {
		return lon;
	}

	public void setLon(int lon) {
		this.lon = lon;
	}

	public int getLat() {
		return lat;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}

	public short getSpeed() {
		return speed;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public short getSpeedRec() {
		return speedRec;
	}

	public void setSpeedRec(short speedRec) {
		this.speedRec = speedRec;
	}

	public int getMiles() {
		return miles;
	}

	public void setMiles(int miles) {
		this.miles = miles;
	}

	public short getDirection() {
		return direction;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}

	public short getAltitude() {
		return altitude;
	}

	public void setAltitude(short altitude) {
		this.altitude = altitude;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

}

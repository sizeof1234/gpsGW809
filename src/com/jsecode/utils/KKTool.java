/**
 * @author Jadic
 * @created 2012-4-27 
 */
package com.jsecode.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class KKTool {
	
	final static char BLANK_CHAR = 0x00;
	
	final static String EMPTY_STR = "";
	final static byte[] ZERO_BYTES = new byte[0];
	final static ChannelBuffer EMPTY_BUFFER = ChannelBuffers.buffer(0);
	
	final static byte BYTE_01 = 0x01;
	final static byte BYTE_02 = 0x02;
	final static byte BYTE_5A = 0x5A;
	final static byte BYTE_5B = 0x5B;
	final static byte BYTE_5D = 0x5D;
	final static byte BYTE_5E = 0x5E;
	final static short SHORT_5A01 = 0x5A01;
	final static short SHORT_5A02 = 0x5A02;
	final static short SHORT_5E01 = 0x5E01;
	final static short SHORT_5E02 = 0x5E02;
	
    /*数据的大小端标识*/
    public static final byte BIT_BIGENDIAN = 0;
    public static final byte BIT_LITTLEENDIAN = 1;
    
    /*时间*/
    public static final int MILLISECOND = 1;
    public static final int SECOND_MILLISECONDS = 1000 * MILLISECOND;
    public static final int MINUTE_MILLISECONDS = 60 * SECOND_MILLISECONDS;
    public static final int HOUR_MILLISECONDS = 60 * MINUTE_MILLISECONDS;
    public static final int DAY_MILLISECONDS = 24 * HOUR_MILLISECONDS;
    
    /**
     * 字节转整型
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        return b >= 0 ? b : 256 + b;
    }

    /**
     * 字节转BCD字节
     * @param b
     * @return
     */
    public static byte byteToBCD(byte b) {
        return (byte)((byte)((b/10)<<4) + (byte)(b%10));
    }
    
    /**
     * BCD字节转换
     * 0x12(18)->0x0c(12)
     * @param bcdByte
     * @return
     */
    public static byte bcd2Byte(byte bcdByte) {
    	return (byte)(((byte)((byte)(bcdByte >>> 4 & (byte)0x0F) )) * 10 + (byte)(bcdByte & 0x0F));
    }
    
    /**
     * 转为BCD字节
     * 0x0C(12)->0x12(18)
     * @param b
     * @return
     */
    public static byte byte2Bcd(byte b) {
    	if (b > 99 || b < 0) {
    		return 0;
    	}
    	byte b1 = (byte)(b / 10);
    	byte b2 = (byte)(b % 10);
    	return (byte)(b1 << 4 + b2);
    }
    
    /**
     * bcd字节数转为整数
     * 如0x123478->123478
     * 转换后的数超过Integer.MaxValue(2^31-1)值会被强转
     * @param bcdBytes
     * @return
     * 
     */
    public static int bcdBytes2Int(byte[] bcdBytes) {
    	return (int)bcdBytes2Long(bcdBytes);
    }
    
    /**
     * bcd字节数转为整数
     * 如0x123478->123478
     * 转换后的数超过Long.MaxValue(2^63-1)值会被强转
     * @param bcdBytes
     * @return
     */
    public static long bcdBytes2Long(byte[] bcdBytes) {
    	long ret = 0;
    	if (bcdBytes != null) {
	    	for (int i = 0; i < bcdBytes.length; i ++) {
	    		ret += bcd2Byte(bcdBytes[i]) * Math.round(Math.pow(100, bcdBytes.length - i - 1));
	    	}
    	}
    	return ret;
    }   
    
    /**
     * 截取字节数组转成字符串
     * 示例:0x10 0x12 0x01  返回  161801
     *      0xA0 0xB1 0x12  返回  16017718
     * @param data        字节数组
     * @param startIndex  截取开始的位置
     * @param len         截取的长度
     * @return
     */
    public static String bytesToString(byte[] data, int startIndex, int len) {
        StringBuilder ret = new StringBuilder();
        byte b = 0;
        int j = 0;
        for (int i = startIndex; (i < data.length) && (i < len); i ++) {
            b = (byte)data[i];
            j = (b >= 0 ? b : b + 256);
            ret.append(j >= 10 ? "" + j : "0" + j);
        }
        return ret.toString();
    }

    /**
     * 从字节数组中截取4个字节转成整型，不足4个字节不进行转换返回0
     * @param data   a2 00 00 00 04
     * @param startIndex  截取开始的下标
     * @param bigOrLittleEndian 大小端 0:大端 1:小端
     * @return
     */
    public static int getInt(byte[] data, int startIndex, byte bigOrLittleEndian) {
        int ret = 0;
        int tmp = 0;
        if ((startIndex >= 0) && (startIndex + 3 < data.length)) {
            if (bigOrLittleEndian == 0) {
                for (int i = startIndex, j= 0; i < startIndex + 4; i ++, j++) {
                    tmp = data[i] >= 0 ? data[i] : 256 + data[i];
                    ret += tmp << (24 - 8 * j);
                }
            } else {
                for (int i = startIndex, j= 0; i < startIndex + 4; i ++, j++) {
                    tmp = data[i] >= 0 ? data[i] : 256 + data[i];
                    ret += tmp << (8 * j);
                }
            }
        }
        return ret;
    }
    
    /**
     * 字节数组转整型，大端模式
     * @param data 数组内容
     * @param startIndex  起始字节
     * @return
     */
    public static int getIntBigEndian(byte[] data, int startIndex) {
    	return getInt(data, startIndex, BIT_BIGENDIAN);
    }

    /**
     * 从字节数组中截取2个字节转成short，不足2个字节不进行转换返回0
     * @param data   a2 00 00 00 04
     * @param startIndex  截取开始的下标
     * @param bigOrLittleEndian 大小端 0:大端 1:小端
     * @return
     */
    public static short getShort(byte[] data, int startIndex, byte bigOrLittleEndian) {
        short ret = 0;
        int tmp = 0;
        if ((startIndex >= 0) && (startIndex + 1 < data.length)) {
            if (bigOrLittleEndian == 0) {
                for (int i = startIndex, j= 0; i < startIndex + 2; i ++, j++) {
                    tmp = data[i] >= 0 ? data[i] : 256 + data[i];
                    ret += (short)tmp << (8 - 8 * j);
                }
            } else {
                for (int i = startIndex, j= 0; i < startIndex + 2; i ++, j++) {
                    tmp = data[i] >= 0 ? data[i] : 256 + data[i];
                    ret += (short)tmp << (8 * j);
                }
            }
        }
        return ret;
    }

    public static short getShort(byte highByte, byte lowByte, byte bigOrLittle) {
        short ret = 0;
        int tmp = 0;
        if (bigOrLittle == 0) {
            tmp = highByte >= 0 ? highByte : 256 + highByte;
            ret += (short)tmp << 8;
            tmp = lowByte >= 0 ? lowByte : 256 + lowByte;
            ret += (short)tmp;
        } else {
            tmp = lowByte >= 0 ? lowByte : 256 + lowByte;
            ret += (short)tmp << 8;
            tmp = highByte >= 0 ? highByte : 256 + highByte;
            ret += (short)tmp;
        }
        return ret;
    }
    
    public static short getShortBigEndian(byte highByte, byte lowByte) {
    	return getShort(highByte, lowByte, (byte)0);
    }

    /**
     * 截取指定开始位置指定长度的字节数组转成字符串
     * @param data          字节数组
     * @param startIndex    截取开始位置
     * @param len           截取长度
     * @return
     */
    public static String getString(byte[] data, int startIndex, int len) {
        if (startIndex < 0 || startIndex >= data.length)
            return "";
        int retStrLen = (startIndex + len <= data.length ? len : data.length - startIndex);
        byte[] tmp = new byte[retStrLen];
        System.arraycopy(data, startIndex, tmp, 0, retStrLen);
        return new String(tmp);
    }

    /**
     * 获取字节数组中从指定位置开始的指定长度的字节数组
     * @param data
     * @param startIndex
     * @param len
     * @return
     */
    public static byte[] getByteAry(byte[] data, int startIndex, int len) {
        byte[] buf = null;
        if (startIndex < 0 || startIndex >= data.length)
            return null;
        int realLen = (startIndex + len <= data.length ? len : data.length - startIndex);
        buf = new byte[realLen];
        System.arraycopy(data, startIndex, buf, 0, realLen);
        return buf;
    }

    /**
     * 获取日期时间 数组存放格式 yymmddhhnnss
     * 不正常情况返回当前时间
     * @param data
     * @param startIndex
     * @return
     */
    public static Date getDateTime(byte[] data, int startIndex) {
        if (startIndex < 0 || startIndex + 6 > data.length)
            return new Date();
        int year = KKTool.byteToInt(data[startIndex ++]) + 2000;
        int month = KKTool.byteToInt(data[startIndex ++]);
        int day = KKTool.byteToInt(data[startIndex ++]);
        int hour = KKTool.byteToInt(data[startIndex ++]);
        int minute = KKTool.byteToInt(data[startIndex ++]);
        int second = KKTool.byteToInt(data[startIndex ++]);
        Calendar c = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        return c.getTime();
    }
    
    /**
     * 返回格式: yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getFormatDateTime(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static String getFormatDateTime(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static String getFormatDate(Date date) {
        if (date == null)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);	
    }
    
    /**
     * 字节转对应的十六进制字符串
     * @param data
     * @return
     */
    public static String byteToHexStr(byte data) {
    	String s = Integer.toHexString(data & 0xFF); 
    	return (s.length() == 2 ? s : "0" + s).toUpperCase();
//        int intValue = byteToInt(data);
//        return (intValue > 15? Integer.toHexString(intValue):"0" + Integer.toHexString(intValue));
    }

    /**
     * 字节数组转对应的十六进制字符串
     * @param data
     * @return 十六进制字符串，数组长度为空时返回空字符串""
     */
    public static String byteArrayToHexStr(byte[] data) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < data.length; i ++) {
            result.append(byteToHexStr(data[i]));
        }
        return result.toString();
    }

    /**
     * 字节数组按指定开始位置、指定长度转成十六进制字符串
     * @param data  字节数组
     * @param startIndex 开始位置
     * @param len 需转换的长度
     * @return 十六进制字符串，开始位置、长度指定不合法返回空字符串""
     */
    public static String byteArrayToHexStr(byte[] data, int startIndex, int len) {
        if(startIndex < 0 || startIndex >= data.length)
            return "";
        if (len < 0)
            return "";
        len = (startIndex + len <= data.length ? len : data.length - startIndex);
        byte[] buf = new byte[len];
        System.arraycopy(data, startIndex, buf, 0, len);
        return byteArrayToHexStr(buf);
    }
    
    public static String channelBufferToHexStr(ChannelBuffer channelBuffer) {
    	return byteArrayToHexStr(channelBuffer.array());
    }
    
    public static String channelBufferReadableBytesToHexStr(ChannelBuffer channelBuffer) {
    	StringBuilder result = new StringBuilder("");
        for (int i = channelBuffer.readerIndex(); i < channelBuffer.writerIndex(); i ++) {
            result.append(byteToHexStr(channelBuffer.getByte(i)));
        }
        return result.toString();
    }

    public static byte[] short2Bytes(short sValue, byte bigOrLittle) {
        byte[] b = new byte[2];
        if (bigOrLittle == 0) {
            b[0] = (byte)(sValue >> 8);
            b[1] = (byte)sValue;
        } else {
            b[0] = (byte)sValue;
            b[1] = (byte)(sValue >> 8);
        }
        return b;
    }
    
    public static byte[] short2BytesBigEndian(short sValue) {
    	return short2Bytes(sValue, BIT_BIGENDIAN);
    }
    
    /**
     * 将short值填入到字节数组中
     * @param sValue  short值
     * @param bigOrLittle  0:大端 1:小端
     * @param data  字节数组
     * @param sIndex 数组中开始填充位置
     * @return 是否成功
     */
    public static boolean short2Bytes(short sValue, byte bigOrLittle, byte[] data, int sIndex) {
    	if (data == null || sIndex < 0 || sIndex + 2 > data.length) 
    		return false;
    	if (bigOrLittle == BIT_BIGENDIAN) {
    		data[sIndex] = (byte)(sValue >> 8);
    		data[sIndex + 1] = (byte)sValue;
        } else {
        	data[sIndex] = (byte)sValue;
        	data[sIndex + 1] = (byte)(sValue >> 8);
        }    		
    	
    	return true;
    }
    
    /**
     * 将short值填入字节数组中 按大端模式填充
     * @param sValue
     * @param data
     * @param sIndex 数组中开始填充位置
     * @return 是否成功
     */
    public static boolean short2BytesBigEndian(short sValue, byte[] data, int sIndex) {
    	return short2Bytes(sValue, BIT_BIGENDIAN, data, sIndex);
    }
    
    public static int getUnsignedShort(short s) {
    	return s > 0 ? s : ((Short.MAX_VALUE + 1) * 2 + s);
    }

    public static byte[] int2Bytes(int iValue, byte bigOrLittle) {
        byte[] b = new byte[4];
        if (bigOrLittle == 0) {
            b[0] = (byte)(iValue >> 24);
            b[1] = (byte)(iValue >> 16);
            b[2] = (byte)(iValue >> 8);
            b[3] = (byte)(iValue);
        } else {
            b[0] = (byte)(iValue);
            b[1] = (byte)(iValue >> 8);
            b[2] = (byte)(iValue >> 16);
            b[3] = (byte)(iValue >> 24);
        }
        return b;
    }
    
    /**
     * 将int值填入到字节数组中
     * @param iValue  int值
     * @param bigOrLittle  0:大端 1:小端
     * @param data  字节数组
     * @param sIndex 数组中开始填充位置
     * @return 是否成功
     */
    public static boolean int2Bytes(int iValue, byte bigOrLittle, byte[] data, int sIndex) {
    	if (data == null || sIndex < 0 || sIndex + 4 > data.length) 
    		return false;
    	if (bigOrLittle == BIT_BIGENDIAN) {
            data[sIndex] = (byte)(iValue >> 24);
            data[sIndex + 1] = (byte)(iValue >> 16);
            data[sIndex + 2] = (byte)(iValue >> 8);
            data[sIndex + 3] = (byte)(iValue);
        } else {
        	data[sIndex + 0] = (byte)(iValue);
        	data[sIndex + 1] = (byte)(iValue >> 8);
        	data[sIndex + 2] = (byte)(iValue >> 16);
        	data[sIndex + 3] = (byte)(iValue >> 24);
        }    		
    	
    	return true;
    }
    
    /**
     * 将int值填入字节数组中 按大端模式填充
     * @param iValue
     * @param data
     * @param sIndex 数组中开始填充位置
     * @return 是否成功
     */
    public static boolean int2BytesBigEndian(int iValue, byte[] data, int sIndex) {
    	return int2Bytes(iValue, BIT_BIGENDIAN, data, sIndex);
    }    

    /**
     * 线程睡眠指定时间
     * @param milliseconds 毫秒
     */
    public static void sleepTime(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            
        }
    }
    
    public static void sleppTimeWithException(long milliseconds) throws InterruptedException {
    	Thread.sleep(milliseconds);
    }
    
    public static void sleepOnMinuteUnit(int minutes) {
    	sleepOnSecondsUnit(60 * minutes);
    }
    
    public static void sleepOnSecondsUnit(int seconds) {
    	sleepOnMilliseconds(seconds * 1000);
    }
    
    public static void sleepOnMilliseconds(long milliseconds) {
    	sleepTime(milliseconds);
    }
    
    public static void sleepOneMinute() {
    	sleepOnMinuteUnit(1);
    }

    public static long getCurrTimeMilliseconds() {
        return System.currentTimeMillis();
    }
    
    public static String getCurrFormatDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdf.format(now);
    }

    public static String getCurrFormatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now);
    }
    
    public static String getCurrFormatDate(String format) {
		if (isStrNullOrBlank(format))
			return "00000000";//yyyyMMdd
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date now = new Date();
			return sdf.format(now);
		} catch (Exception e) {
			return "00000000";
		}
	}
    
    /**
     * 解析字节数组中的GPS时间
     * @param data
     * @param startIndex 开始解析的位置
     * @param isGMT 是否是格林尼治时间
     * @param dataType GPS时间数据的格式  0：正常的16进制 1：BCD码
     * @return 返回北京时间 数据有错返回当前时间
     */
    public static Date getGpsTime(byte[] data, int startIndex, boolean isGMT, byte dataType) {
        if (data == null || startIndex < 0 || data.length - startIndex < 6)
            return new Date();
        int offset = startIndex;
        int year, month, day, hour, minute, second;
        try {
            if (dataType == 0) {//16进制
                year = 2000 + data[offset++];
                month = data[offset++];
                day = data[offset++];
                hour = data[offset++];
                minute = data[offset++];
                second = data[offset++];
            } else {//BCD码  10 0x10
                year = 2000 + Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                month = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                day = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                hour = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                minute = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                second = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
            }
            Calendar c = new GregorianCalendar(year, month - 1, day, hour, minute, second);
            if (isGMT) {//是格林尼治时间加上8小时
                c.add(Calendar.HOUR_OF_DAY, 8);
            }
            return c.getTime();
        } catch (Exception e) {
            return new Date();
        }
    }

    public static byte[] getBCDGMTGpsTime(byte[] data, int startIndex, byte dataType, boolean isGMT) {
        byte[] bcdGpsTime = new byte[6];
        if (data == null || startIndex < 0 || data.length - startIndex < 6)
            return bcdGpsTime;
        int offset = startIndex;
        int year, month, day, hour, minute, second;
        try {
            if (dataType == 0) {//16进制
                year = 2000 + data[offset++];
                month = data[offset++];
                day = data[offset++];
                hour = data[offset++];
                minute = data[offset++];
                second = data[offset++];
            } else {//BCD码  10 0x10
                year = 2000 + Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                month = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                day = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                hour = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                minute = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
                second = Integer.parseInt(KKTool.byteToHexStr(data[offset++]));
            }
            Calendar c = new GregorianCalendar(year, month - 1, day, hour, minute, second);
            if (!isGMT) {
                c.add(Calendar.HOUR_OF_DAY, -8);
            }
            bcdGpsTime[0] = byteToBCD((byte)(c.get(Calendar.YEAR)- 2000));
            bcdGpsTime[1] = byteToBCD((byte)(c.get(Calendar.MONTH) + 1));
            bcdGpsTime[2] = byteToBCD((byte)c.get(Calendar.DAY_OF_MONTH));
            bcdGpsTime[3] = byteToBCD((byte)c.get(Calendar.HOUR_OF_DAY));
            bcdGpsTime[4] = byteToBCD((byte)c.get(Calendar.MINUTE));
            bcdGpsTime[5] = byteToBCD((byte)c.get(Calendar.SECOND));
        } catch (Exception e) {
        }
        return bcdGpsTime;
    }

    /**
     * 解析字节数组中的GPS时间,返回格式yyyy-MM-dd hh:mm:ss
     * @param data
     * @param startIndex 开始解析的位置
     * @param isGMT 是否是格林尼治时间
     * @param dataType GPS时间数据的格式  0：正常的16进制 1：BCD码
     * @return 返回北京时间 数据有错返回当前时间
     */
    public static String getGpsTimeStr(byte[] data, int startIndex, boolean isGMT, byte dataType) {
        Date gpsTime = getGpsTime(data, startIndex, isGMT, dataType);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(gpsTime);
    }

    /**
     * 填充成指定长度的字符串
     * @param str 源字符串
     * @param len 填充后的长度
     * @param fillType 填充方式 0:左补 1:右补
     * @return
     */
    public static String formatString(String str, int len, byte fillType) {
        if (str == null)
            return null;
        int strLen = str.getBytes(Charset.forName("UTF-8")).length;
        if (strLen == len) {
            return str;
        } else if (strLen < len) {
            int blankCount = len - strLen;
            String blank = "";
            for (int i = 0; i < blankCount - 1; i ++) {
                blank = blank + " ";
            }
            if (fillType == 0) {
                return blank + str;
            } else {
                return str + blank;
            }
        } else {
            return str.substring(0, len);
        }
    }

    public static String getExceptionTip(Throwable e) {
    	String ret = null;
        if (e != null) {
        	ret = "msg:" + e.getMessage() + " stacktrace:";
			StackTraceElement[] stes =  e.getStackTrace();
			for(StackTraceElement ste : stes) {
			    ret = ret + ste.toString() + " ";
			}
        }
        return ret;
    }
    
    public static boolean isOvertime(long sTime, long maxTime) {
    	return System.currentTimeMillis() - sTime >= maxTime;
    }

    /**
     * 计算CRC值，此算法是公交IC卡中采用，
     * 算法中要返回2字节无符号型整数，此处将其返回4字节的int型，获取后比较时需进行相关转换
     * @param len
     * @param data
     * @param sIndex
     * @return
     */
    public static int getCRCBusIC(int len, byte[] data, int sIndex) {
    	int crc = 0x31E3;
    	for(int i = 0; i < len; i ++) {
    		crc = crc ^ (data[i + sIndex]<<8);
    		for (int j = 0; j < 8; j ++) {
    			if ((crc & 0x8000) != 0) {
    				crc = (crc << 1) ^ 0x1021;
    			} else {
    				crc = crc << 1;
    			}
    		}
    	}
    	return crc;
    }
    
    /**
     * 809平台CRC校验算法(取)
     * @param buffer
     * @param sIndex (包含)
     * @param eIndex (不含)
     * @return
     */
    public static int getCRC16(ChannelBuffer buffer, int sIndex, int eIndex) {
    	int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
		if (buffer != null && sIndex >= 0 && eIndex <= buffer.capacity()) {
			for (int j = sIndex; j < eIndex; j++) {
				byte b = buffer.getByte(j);
				for (int i = 0; i < 8; i++) {
					boolean bit = ((b >> (7 - i) & 1) == 1);
					boolean c15 = ((crc >> 15 & 1) == 1);
					crc <<= 1;
					if (c15 ^ bit)
						crc ^= polynomial;
				}
			}
		}
		crc &= 0xffff;
		return crc;
    }
    
    public static boolean checkLRC(byte[] data) {
    	return checkLRC1(data) && checkLRC2(data);
    }
    
    public static boolean checkLRC(ChannelBuffer buffer, int cmdSize) {
    	return checkLRC1(buffer, cmdSize) && checkLRC2(buffer, cmdSize);
    }
    
    /**
     * 校验位1是否正确   LRC1：从长度开始到数据结束的每一个字节的异或值再异或0x33
     * @param data  完整的数据包   从包类型到校验位2
     * @return
     */
    public static boolean checkLRC1(byte[] data) {
    	if (data == null || data.length <= 14)
    		return false;
    	
    	byte lrc1 = data[data.length - 2];
    	
    	byte lrc = 0;
    	
    	for(int i = 2; i < data.length - 2; i ++) {
    		lrc ^= data[i];
    	}
    	
    	return lrc1 == (lrc ^ (byte)0x33);
    }
    
    public static boolean checkLRC1(ChannelBuffer buffer, int cmdSize) {
    	if (buffer == null || buffer.readableBytes() <= 14) {
    		return false;
    	}
    	
    	int readerIndex = buffer.readerIndex();
    	byte lrc1 = buffer.getByte(readerIndex + cmdSize - 2);
    	byte lrc = 0;
    	for (int i = 4; i < cmdSize - 2; i ++) {
    		lrc ^= buffer.getByte(readerIndex + i);
    	}
    	return lrc1 == (byte)(lrc ^ (byte)0x33);
    }
    
    /**
     * 校验位2是否正确   LRC2：从长度开始到数据结束的每一个字节的累加和再加0x33
     * @param data  完整的数据包   从包类型到校验位2
     * @return
     */
    public static boolean checkLRC2(byte[] data) {
    	if (data == null || data.length <= 14)
    		return false;
    	
    	byte lrc2 = data[data.length - 1];
    	
    	byte lrc = 0;
    	
    	for(int i = 2; i < data.length - 2; i ++) {
    		lrc += data[i];
    	}
    	
    	return lrc2 == (byte)(lrc + 0x33);
    }
    
    public static boolean checkLRC2(ChannelBuffer buffer, int cmdSize) {
    	if (buffer == null || buffer.readableBytes() <= 14) {
    		return false;
    	}
    	
    	int readerIndex = buffer.readerIndex();
    	byte lrc2 = buffer.getByte(readerIndex + cmdSize - 1);
    	byte lrc = 0;
    	for (int i = 4; i < cmdSize - 2; i ++) {
    		lrc += buffer.getByte(readerIndex + i);
    	}
    	return lrc2 == (byte)(lrc + (byte)0x33);
    }
    
    public static byte getLRC1(byte[] data) {
    	byte lrc1 = 0;
    	
    	for(int i = 0; i < data.length; i ++) {
    		lrc1 ^= data[i];
    	}
    	return (byte)(lrc1 ^ 0x33);
    }
    
    public static byte getLRC2(byte[] data) {
    	byte lrc2 = 0;
    	
    	for(int i = 0; i < data.length; i ++) {
    		lrc2 += data[i];
    	}
    	return (byte)(lrc2 + 0x33);
    }
    
    /**
     * 根据卡号返回bcd字节数组
     * 如：卡号为8845879911223344，则返回0x880x450x870x990x110x220x330x44,
     * @param cardNo
     * @return
     */
    public static byte[] cardNoToBcdBytes(char[] cardNo) {
    	if (cardNo == null || cardNo.length != 16)
    		return null;
    	
    	byte[] bcdBytes = new byte[8];
    	try {
    		for (int i = 0; i < 8; i ++) {
    			bcdBytes[i] = (byte)Short.parseShort(String.copyValueOf(cardNo, 2 * i, 2), 16);
    		}
		} catch (Exception e) {
			return null;
		}
    	return bcdBytes;
    }
    
    /**
     * 根据卡号转换为16进制
     * @param cardNo    原始卡号
     * @param bytesLen  转换后的字节数组长度
     * @param fillChar  长度不足时补充的字符
     * @return
     */
    public static byte[] cardNoToBcdBytes(char[] cardNo, int bytesLen, char fillChar) {
    	byte[] bcdBytes = null;
    	if (cardNo != null) {
    		char[] tmp = cardNo;
    		if (tmp.length != bytesLen * 2) {
	    		tmp = new char[bytesLen * 2];
	    		if (cardNo.length < bytesLen * 2) {
		    		for (int i = 0; i < tmp.length; i ++) {
		    			tmp[i] = fillChar;
		    		}
	    		}
	    		System.arraycopy(cardNo, 0, tmp, 0, cardNo.length < tmp.length ? cardNo.length : tmp.length);
    		}
    		try {
    			bcdBytes = new byte[bytesLen];
        		for (int i = 0; i < bytesLen; i ++) {
        			bcdBytes[i] = (byte)Short.parseShort(String.copyValueOf(tmp, 2 * i, 2), 16);
        		}
    		} catch (Exception e) {
    			return null;
    		}
    	}
    	return bcdBytes;
    }
    
    /**
     * 字符串转换为固定长度的对应的字节数组
     * @param srcStr    12345678
     * @param bytesLen  5
     * @param fillChar  'F'
     * @return byte[]{0x12,0x34,0x56,0x78, 0xFF} 
     */
    public static byte[] strToHexBytes(String srcStr, int bytesLen, char fillChar) {
    	byte[] bcdBytes = null;
    	if (bytesLen > 0) {
    		String fixedLenStr = KKTool.getFixedLenString(srcStr, bytesLen * 2, fillChar, false);
    		if (fixedLenStr.length() == bytesLen * 2) {
    			bcdBytes = new byte[bytesLen];
    			try {
	    			for (int i = 0; i < bytesLen; i++) {
						bcdBytes[i] = (byte)Short.parseShort(fixedLenStr.substring(2 * i, 2 * i + 2), 16); 
					}
    			} catch (Exception e) {
    				bcdBytes = null;
				}
    		}
    	}
    	return bcdBytes;
    }
    
    public static void fillBytes(byte[] data, int startIndex, byte fillByte) {
    	if (data == null || data.length <= 0)
    		return;
    	
    	for (int i = startIndex; i < data.length; i ++) {
    		data[i] = fillByte; 
    	}
    }
    
    public static boolean isDateBeforeToday(String date) {
    	String today = getFormatDateTime(new Date(), "yyyyMMdd");
    	return date.compareTo(today) <= 0;
    }
    
    public static boolean deleteFile(String filePath) {
    	if (filePath == null || filePath.equals(""))
    		return false;
    	
    	File file = new File(filePath);
    	if (file.exists())
    		return file.delete();
    	return false;
    }
    
    public static boolean renameFile(String oldFileName, String newFileName) {
    	if (oldFileName == null || oldFileName.equals("") || newFileName == null || newFileName.equals(""))
    		return false;
    	
    	File oFile = new File(oldFileName);
    	File nFile = new File(newFileName);
    	return oFile.renameTo(nFile);
    }
    
    /**
     * 返回昨天的标准格式字符串yyyyMMdd
     * @return
     */
    public static String getYesterdayFormatDate() {
    	Calendar c = Calendar.getInstance();
    	c.add(Calendar.DAY_OF_YEAR, -1);
    	return getFormatDate(c.getTime());
    }
    
    /**
     * 返回基于当天时间偏差的标准日期格式<p>
     * 示例：daysOffset = -1, 则返回昨天的标准格式的字符串
     * @param daysOffset
     * @param format
     * @return
     */
    public static String getFormatDate(int daysOffset, String format) {
    	Calendar c = Calendar.getInstance();
    	if (daysOffset != 0) {
    		c.add(Calendar.DAY_OF_YEAR, daysOffset);
    	}
    	return getFormatDateTime(c.getTime(), format);
    }
    
    public static boolean isFileExisted(String fileName) {
		if (isStrNullOrBlank(fileName))
			return false;
		
		return new File(fileName).exists();
    }
    
    public static boolean isFileExisted(File file) {
    	return file != null && file.exists();
    }
    
    public static boolean fillIntIntoBytes(int iValue, byte[] data, int offset, byte bigOrLittle) {
    	if (data == null || data.length < 4 || offset < 0 || offset + 3 < data.length)
    		return false;
    	
        if (bigOrLittle == 0) {
        	data[offset] = (byte)(iValue >> 24);
        	data[offset + 1] = (byte)(iValue >> 16);
        	data[offset + 2] = (byte)(iValue >> 8);
        	data[offset + 3] = (byte)(iValue);
        } else {
        	data[offset] = (byte)(iValue);
        	data[offset + 1] = (byte)(iValue >> 8);
        	data[offset + 2] = (byte)(iValue >> 16);
        	data[offset + 3] = (byte)(iValue >> 24);
        }
        return true;
    }
    
    public static boolean fillIntIntoBytesBigEndian(int iValue, byte[] data, int offset) {
    	return fillIntIntoBytes(iValue, data, offset, BIT_BIGENDIAN);
    }
    
    public static boolean isStrNullOrBlank(String s) {
		return s == null || s.equals("");
    }
    
    public static boolean isBytesNullOrBlank(byte[] bytes) {
    	return bytes == null || bytes.length == 0;
    }
    
    public static void initBytes(byte[] data, byte b) {
    	if (data == null || data.length == 0)
    		return;
    	for (int i = 0; i < data.length; i++) {
			data[i] = b; 
		}
    }
    
    /**
     * 将字符串转换为bcd字节数组
     * 以下几种情况返回值全为0的数组
     * 1.字符串为空
     * 2.字符串的长度不为字节数组长度的2倍
     * 3.字符串中有不合法的内容
     * @param s 字符串
     * @param bytesLen 转换后的数组长度
     * @return
     */
    public static byte[] strToBcdBytes(String s, int bytesLen) {
    	byte[] bcdBytes = new byte[bytesLen];
    	if (!KKTool.isStrNullOrBlank(s)) {
    		if (s.length() == 2 * bytesLen) {
    			try {
    				for(int i = 0; i < bytesLen; i ++) {
    					bcdBytes[i] = (byte)(Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16)); 
    				}
				} catch (Exception e) {
					initBytes(bcdBytes, (byte)0);
				}
    		}
    	}
    	return bcdBytes;
    }
    
    public static void printLog(Object object) {
    	System.out.println(object);
    }
    
    /**
     * 字节数组拷贝
     * @param dst 目标数组
     * @param src 源数组
     * @param dstIndex 目标数组的开始拷贝位置
     * @return 拷贝是否成功 
     */
    public static boolean copyBytes(byte[] dst, byte[] src, int dstIndex) {
    	if (dst == null || src == null || dstIndex < 0 || dstIndex + src.length > dst.length)
    		return false;
    	try {
    		System.arraycopy(src, 0, dst, dstIndex, src.length);
    	} catch (Exception e) {
			return false;
		}
    	return true;
    }
    
    /**
     * 获取定长格式化字符串，不足补指定字符
     * 超过长度则截取
     * @param srcStr
     * @param len
     * @param fillChar
     * @param isFillLeft
     * @return
     */
    public static String getFormatStr(String srcStr, int len, String fillChar, boolean isFillLeft) {
    	if (srcStr.length() == len)
    		return srcStr;
    	
    	if (srcStr.length() > len) {//超过长度则截取
    		if(isFillLeft)
    			return srcStr.substring(srcStr.length() - len);
    		else
    			return srcStr.substring(0, len);
    	}
    	
    	StringBuilder sBuilder = null;
    	if (isFillLeft) {//左补
    		sBuilder = new StringBuilder();
			for (int i = 0; i < len - srcStr.length(); i ++) {
				sBuilder.append(fillChar);
			}    	
			sBuilder.append(srcStr);
    	} else {//右补
	    	sBuilder = new StringBuilder(srcStr);
			for (int i = 0; i < len - srcStr.length(); i ++) {
				sBuilder.append(fillChar);
			}
    	}
    	return sBuilder.toString();
    }
    
    public static char[] getFormatCharAry(String srcStr, int len) {
    	return getFormatStr(srcStr, len, "0", true).toCharArray();
    }
    
    public static byte[] getBytesFromStr(String srcStr, int len) {
    	byte[] bytes = null;
    	if (!isStrNullOrBlank(srcStr)) {
    		bytes = new byte[len];
    		try {
    			srcStr = getFormatStr(srcStr, len, "0", true);
    			for(int i = 0; i < len; i ++) {
    				bytes[i] = (byte)(Integer.parseInt((srcStr.substring(i, i + 1))));
    			}
			} catch (Exception e) {
				initBytes(bytes, (byte)0);
			}
    		
    	}
    	return bytes;
    }
    
    public static long bytesToLong(byte[] bytes) {
    	long l = 0;
    	if (!isBytesNullOrBlank(bytes) && bytes.length <= 8) {
    		for (int i = bytes.length - 1; i >= 0; i --) {
    			l |= ((long)(bytes[i] & 0x00ff)) << ((bytes.length - 1 - i) * 8);
    		}
    	}
    	return l;
    }
    
    public static byte[] getCurrBcdDateTime() {
    	byte[] dt = new byte[7];
    	Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
    	int month = c.get(Calendar.MONTH) + 1;
    	int day = c.get(Calendar.DAY_OF_MONTH);
    	int hour = c.get(Calendar.HOUR_OF_DAY);
    	int minute = c.get(Calendar.MINUTE);
    	int second = c.get(Calendar.SECOND);
    	dt[0] = byteToBCD((byte)(year/100));
    	dt[1] = byteToBCD((byte)(year % 100));
    	dt[2] = byteToBCD((byte)month);
    	dt[3] = byteToBCD((byte)day);
    	dt[4] = byteToBCD((byte)hour);
    	dt[5] = byteToBCD((byte)minute);
    	dt[6] = byteToBCD((byte)second);
    	return dt;
    }
    
    public static byte[] getBcdBytesFromStr(String srcStr, int len) {
    	byte[] b = new byte[len];
    	try {
			for(int i = 0; i < len; i ++) {
				b[i] = (byte)Integer.parseInt(srcStr.substring(2 * i, 2 * i + 2), 16);
			}	
    	} catch (Exception e) {
			initBytes(b, (byte)0);
			KKLog.error("getBcdBytesFromStr err:" + getExceptionTip(e));
		}
		return b;
    }
    
    public static boolean createFileDir(String dir) {
    	if (!isStrNullOrBlank(dir)) {
    		File file = new File(dir);
    		if (!file.exists())
    			return file.mkdirs();
    		return true;
    	}
    	return false;
    }
    
    public static boolean isPosDealDataValid(String carNo, String lineNo, String dealTime) {
		if (carNo == null || lineNo == null || dealTime == null)
			return false;
		if (!carNo.matches("\\w{6}"))
			return false;
		if (!lineNo.matches("\\d{7}"))
			return false;
		if (!dealTime.matches("20((([1-9][13579])|([13579][048])|([2468][26]))(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[12][0-9]|2[0-8])))|(([2468][048])|([13579][26]))((((0[13578])|(1[02]))(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[12][0-9]|2[0-9]))))(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])"))
			//if (!dealTime.matches("20[1-9][1-9]((0[1-9])|(1[0-2]))()(([0-1][0-9])|(2[0-3]))([0-5][0-9])([0-5][0-9])"))//20120202
			return false;
		return true;
	}
    
    /**
     * windows下获取正常,linux下获取虚拟机下会为127.0.0.1,真机未试过
     * @return
     */
    public static String getLocalIp() {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			return inet.getHostAddress();
		} catch (UnknownHostException e1) {
			return "";
		}
    }
    
    public static String elipseTime(long sTime, long eTime) {
    	long elipseTime = Math.abs(eTime - sTime);
    	int day = (int)elipseTime/DAY_MILLISECONDS;
    	elipseTime -= day * DAY_MILLISECONDS;
    	int hour = (int)elipseTime/HOUR_MILLISECONDS;
    	elipseTime -= hour * HOUR_MILLISECONDS;
    	int min = (int)elipseTime/MINUTE_MILLISECONDS;
    	elipseTime -= min * MINUTE_MILLISECONDS;
    	int second = (int)elipseTime / SECOND_MILLISECONDS;
    	elipseTime -= second * SECOND_MILLISECONDS;
    	int millisecond = (int)elipseTime;
    	StringBuilder sb = new StringBuilder();
    	if (day > 0)
    		sb.append(day + "day");
    	if (hour > 0)
    		sb.append(hour + "hour");
    	if (min > 0)
    		sb.append(min + "minute");
    	if (second > 0)
    		sb.append(second + "second");
    	if (millisecond > 0)
    		sb.append(millisecond + "millisecond");
    	return sb.toString();
    }
    
    public static boolean isIpInvalid(String ip) {
    	if (isStrNullOrBlank(ip))
			return false;
		String[] s = ip.split("\\x2e");
		if (s.length == 4) {
			return isInputNumberInRange(s[0], 1, 223, 3) && isInputNumberInRange(s[1], 0, 255, 3) 
				&& isInputNumberInRange(s[2], 0, 255, 3) && isInputNumberInRange(s[3], 0, 255, 3);
		}
		if (s.length == 6) {
			return isInputNumberInRange(s[0], 1, 223, 3) && isInputNumberInRange(s[1], 0, 255, 3) 
					&& isInputNumberInRange(s[2], 0, 255, 3) && isInputNumberInRange(s[3], 0, 255, 3)
					&& isInputNumberInRange(s[4], 0, 255, 3) && isInputNumberInRange(s[5], 0, 255, 3);
		}
		return false;
    }
    
    public static boolean isInputNumberInRange(String input, int minValue, int maxValue, int maxLength) {
		if (isInputNumber(input) && input.length() <= maxLength) {
			int i = Integer.valueOf(input);
			return minValue <= i && i <= maxValue;
		}
		return false;
    }
    
    public static boolean isInputNumber(String input) {
		if (isStrNullOrBlank(input))
			return false;
		
		return input.matches("\\d+");
	}
    
    public static boolean isPortInvalid(String port) {
		if (isInputNumber(port) && port.length() <= 5) {
			int iPort = Integer.valueOf(port);
			return iPort <= 65535;
		}
		
		return false;
	}
    
    /**
     * 根据毫秒值返回固定格式的时间字符串
     * 格式：yyyyMMdd HHmmss
     * @param d
     * @return
     */
    public static String getFormatDatetime(long d) {
    	Date date = new Date(d);
    	return getFormatDateTime(date, "yyyyMMdd HHmmss");
    }
    
    /**
     * 创建文件所在目录
     * @param fileName
     * @return
     */
    public static boolean createFileParentDir(String fileName) {
    	if (isStrNullOrBlank(fileName)) {
    		return false;
    	}
    	File file = new File(fileName);
    	if (!file.isDirectory()) {
    		String parentPath = file.getParent();
    		if (!isStrNullOrBlank(parentPath)) {
    			File parentFile = new File(parentPath);
    			parentFile.mkdirs();
    		}
    	}
    	return true;
    }
    
    /**
     * check two array content is same
     * @param b1
     * @param b2
     * @return
     */
    public static boolean isArrayDataSame(byte[] b1, byte[] b2) {
    	if (b1 == null || b2 == null) {
    		return false;
    	}
    	
    	if (b1 == b2) {
    		return true;
    	}
    	if (b1.length != b2.length) {
    		return false;
    	}
    	
    	for (int i = 0; i < b1.length; i ++) {
    		if (b1[i] != b2[i]) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * 关闭connection，抛异常时不提示
     * @param connection
     */
    public static void closeConnectionInSilence(Connection connection) {
    	if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    /**
     * 关闭statement，抛异常时不提示
     * @param statement
     */
    public static void closeStatementInSilence(Statement statement) {
    	if (statement != null) {
    		try {
				statement.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    /**
     * 关闭result set，抛异常时不提示
     * @param resultSet
     */
    public static void closeResultSetInSilence(ResultSet resultSet) {
    	if (resultSet != null) {
    		try {
				resultSet.close();
			} catch (SQLException e) {
			}
    	}
    }
    
    /**
     * 关闭result set, statement, connection<p>
     * 数据库查询操作，经常需要作这三个关闭操作
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void closeRS_Statement_ConnInSilence(ResultSet resultSet, Statement statement, Connection connection) {
    	closeResultSetInSilence(resultSet);
    	closeStatementInSilence(statement);
    	closeConnectionInSilence(connection);
    }
    
    /**
     * 关闭statement,connection<p>
     * 数据库存储操作，经常需要作这两个关闭操作
     * @param statement
     * @param connection
     */
    public static void closeStatementAndConnectionInSilence(Statement statement, Connection connection) {
    	closeStatementInSilence(statement);
    	closeConnectionInSilence(connection);
    }
    
    /**
     * 关闭Writer不提示异常
     * @param writer
     */
    public static void closeWriterInSilence(Writer writer) {
    	if (writer != null) {
    		try {
				writer.close();
			} catch (IOException e) {
			}
    	}
    }
    
    /**
     * 关闭reader不提示异常
     * @param reader
     */
    public static void closeReaderInSilence(Reader reader) {
    	if (reader != null) {
    		try {
				reader.close();
			} catch (IOException e) {
			}
    	}
    }
    
    /**
     * 关闭RandomAccessFile不提示异常
     * @param raf
     */
    public static void closeRandomAccessFileInSilence(RandomAccessFile raf) {
    	if (raf != null) {
    		try {
				raf.close();
			} catch (IOException e) {
			}
    	}
    }

    /**
     * 根据提供的字符串及填充字符串生成固定长度的字符串
     * @param srcStr	原字符串
     * @param fixedLen  固定长度
     * @param filledChar填充字符
     * @param isFillLeft左填充或右填充  true:左  false:右 
     * @return
     */
    public static String getFixedLenString(String srcStr, int fixedLen, char filledChar, boolean isFillLeft) {
    	if (fixedLen <= 0) {
    		return "";
    	}
    	
    	StringBuilder sBuilder = null;
    	if (srcStr != null) {
    		int strLen = srcStr.length();
    		if (strLen == fixedLen) {
    			return srcStr;
    		} else if (strLen > fixedLen) {
    			return srcStr.substring(0, fixedLen);
    		} else {
    			sBuilder = new StringBuilder(srcStr);
    		}
    	} else {
    		sBuilder = new StringBuilder();
    	}
    	if (isFillLeft) {
    		while (sBuilder.length() < fixedLen) {
    			sBuilder.insert(0, filledChar);
    		}
    	} else {
	    	while (sBuilder.length() < fixedLen) {
	    		sBuilder.append(filledChar);
	    	}
    	}
    	return sBuilder.toString();
    }
    
    /**
     * 根据提供的字符串及填充字符串生成固定长度的字符串
     * @param srcStr	原字符串
     * @param fixedLen  固定长度
     * @param filledChar填充字符
     * @param isFillLeft左填充或右填充  true:左  false:右 
     * @param isTruncatedFromLeft	源字符串超过固定长度从左截取还是从右截取  true:左  false:右
     * @return
     */
    public static String getFixedLenString(String srcStr, int fixedLen, char filledChar, boolean isFillLeft, boolean isTruncatedFromLeft) {
    	if (fixedLen <= 0) {
    		return "";
    	}
    	
    	StringBuilder sBuilder = null;
    	if (srcStr != null) {
    		int strLen = srcStr.length();
    		if (strLen == fixedLen) {
    			return srcStr;
    		} else if (strLen > fixedLen) {
    			if (isTruncatedFromLeft) {
    				return srcStr.substring(0, fixedLen);
    			} else {
    				return srcStr.substring(srcStr.length() - fixedLen);
    			}
    		} else {
    			sBuilder = new StringBuilder(srcStr);
    		}
    	} else {
    		sBuilder = new StringBuilder();
    	}
    	if (isFillLeft) {
    		while (sBuilder.length() < fixedLen) {
    			sBuilder.insert(0, filledChar);
    		}
    	} else {
    		while (sBuilder.length() < fixedLen) {
    			sBuilder.append(filledChar);
    		}
    	}
    	return sBuilder.toString();
    }

    /**
     * 根据提供的字符串及填充字符串生成固定长度的字符串<br>
     * 长度不足时默认右补\0
     * @param srcStr
     * @param fixedLen
     * @return
     */
    public static String getFixedLenString(String srcStr, int fixedLen) {
    	return getFixedLenString(srcStr, fixedLen, BLANK_CHAR, false);
    }

    /**
     * 根据提供的字符串及填充字符串生成固定长度的字符串,并转成相应字节数组
     * @param srcStr	原字符串
     * @param fixedLen  固定长度
     * @param filledChar填充字符
     * @param isFillLeft左填充或右填充  true:左  false:右 
     * @return
     */
    public static byte[] getFixedLenBytes(String srcStr, int fixedLen, char filledChar, boolean isFillLeft) {
    	return getFixedLenString(srcStr, fixedLen, filledChar, isFillLeft).getBytes();
    }

    /**
     * 根据提供的字符串及填充字符串生成固定长度的字符串,并转成相应字节数组
     * @param srcStr	原字符串
     * @param fixedLen  固定长度
     * @param filledChar填充字符
     * @param isFillLeft左填充或右填充  true:左  false:右 
     * @param isTruncatedFromLeft	源字符串超过固定长度从左截取还是从右截取  true:左  false:右
     * @return
     */
    public static byte[] getFixedLenBytes(String srcStr, int fixedLen, char filledChar, boolean isFillLeft, boolean isTruncatedFromLeft) {
    	return getFixedLenString(srcStr, fixedLen, filledChar, isFillLeft, isTruncatedFromLeft).getBytes();
    }

    /**
     * 根据提供的字符串生成固定长度的字符串,并转成相应字节数组<br>
     * 长度不足时默认右补\0
     * @param srcStr
     * @param fixedLen
     * @return
     */
    public static byte[] getFixedLenBytes(String srcStr, int fixedLen) {
    	return getFixedLenString(srcStr, fixedLen, BLANK_CHAR, false).getBytes();
    }
    
    /**
     * 获取固定长度的字符串，该字符串编码格式为GBK
     * @param srcStr
     * @param fixedLen
     * @param filledChar
     * @param isFillLeft
     * @return
     */
    public static String getFixedLenGBKStr(String srcStr, int fixedLen, char filledChar, boolean isFillLeft) {
    	if (fixedLen <= 0) {
    		return "";
    	}
    	StringBuilder sBuilder = null;
    	int strLen = 0;
    	if (srcStr != null) {
    		strLen = srcStr.getBytes(Charset.forName("GBK")).length;
    		if (strLen == fixedLen) {
    			return srcStr;
    		} else if (strLen > fixedLen) {
    			return srcStr.substring(0, fixedLen);
    		} else {
    			sBuilder = new StringBuilder(srcStr);
    		}
    	} else {
    		sBuilder = new StringBuilder();
    	}
    	if (isFillLeft) {
    		while (strLen ++ < fixedLen) {
    			sBuilder.insert(0, filledChar);
    		}
    	} else {
	    	while (strLen ++ < fixedLen) {
	    		sBuilder.append(filledChar);
	    	}
    	}
    	return sBuilder.toString();
    }
    
    /**
     * GBK编码的字节转成字符串
     * @param gbkBytes
     * @return
     */
    public static String toGBKStr(byte[] gbkBytes) {
    	if (gbkBytes != null) {
    		return new String(gbkBytes, Charset.forName("GBK"));
    	}
    	return EMPTY_STR;
    }
    
    /**
     * GBK编码的字符串转成字节数组
     * @param gbkStr
     * @return
     */
    public static byte[] toGBKBytes(String gbkStr) {
    	if (gbkStr != null) {
    		return gbkStr.getBytes(Charset.forName("GBK"));
    	}
    	return ZERO_BYTES;  
    }
    
    /**
     * 将字符串转为指定长度的GBK编码的字节数组（超过长度则右截）
     * @param srcStr		待转换的源字符串
     * @param fixedLen		指定长度	
     * @param filledChar 	不足长度时填充的长度
     * @param isFillLeft 	true:左填充
     * @return
     */
    public static byte[] toFixedLenGBKBytes(String srcStr, int fixedLen, char filledChar, boolean isFillLeft) {
    	return toGBKBytes(getFixedLenGBKStr(srcStr, fixedLen, filledChar, isFillLeft));
    }
    
    /**
     * 将字符串转为指定长度的GBK编码的字节数组(长度不足时后补0x00,超过长度则右截)
     * @param srcStr		待转换的源字符串
     * @param fixedLen		指定长度	 
     * @return
     */
    public static byte[] toFixedLenGBKBytes(String srcStr, int fixedLen) {
    	return toGBKBytes(getFixedLenGBKStr(srcStr, fixedLen, BLANK_CHAR, false));
    }
    
    /**
     * 根据字节数组、格式化字符串，返回格式化的时间字符串
     * 示例：
     * 参数：new byte[]{0x20, 0x13, 0x01, 0x25, 0x10, 0x08, 0x09}, "%s%s-%s-%s %s:%s:%s"
     * 输出： 2013-01-25 10:08:09     
     * @param dt
     * @param formatDtStr
     * @return
     */
    public static String getFormatDTStrFromByteArray(byte[] dt, String formatDtStr) {
    	if (dt == null || dt.length == 0) {
    		return "";
    	}
    	try {
    		Object[] objs = new Object[dt.length];
    		for (int i = 0; i < dt.length; i ++) {
    			objs[i] = byteToHexStr(dt[i]);
    		}
    		return String.format(formatDtStr, objs);
		} catch (Exception e) {
			return byteArrayToHexStr(dt);
		}
    }
    
    /**
     * 文件拷贝
     * 0：失败
     * 1：成功
     * 2：源文件不存在
     * @param srcFileName
     * @param dstFileName
     * @return
     */
    public static byte copyFile(String srcFileName, String dstFileName) {
    	File srcFile = new File(srcFileName);
    	if (srcFile.exists()) {
    		return 2;
    	}
    	
    	createFileParentDir(dstFileName);
    	File dstFile = new File(dstFileName);
    	FileInputStream fis = null;
    	FileOutputStream fos = null;
    	try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(dstFile);
			byte[] bt = new byte[1024];  
            int count;  
            while ((count = fis.read(bt)) > 0) {  
                fos.write(bt, 0, count);  
            }  
            return 1;
		} catch (Exception e) {
			
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
    	return 0;
    }
    
    /**
     * 获取转义后的buffer
     * 数据内容进行转义判断，转义规则如下：
     * a)若数据内容中有出现字符0x5b 的，需替换为字符0x5a 紧跟字符0x01；
     * b)若数据内容中有出现字符0x5a 的，需替换为字符0x5a 紧跟字符0x02；
     * c)若数据内容中有出现字符0x5d 的，需替换为字符0x5e 紧跟字符0x01；
     * d)若数据内容中有出现字符0x5e 的，需替换为字符0x5e 紧跟字符0x02。
     * @param channelBuffer
     * @return
     */
    public static ChannelBuffer getEscapedBuffer(ChannelBuffer channelBuffer) {
    	if (channelBuffer != null) {
    		ChannelBuffer retBuffer = ChannelBuffers.buffer(channelBuffer.readableBytes() * 2);
    		retBuffer.writeByte(BYTE_5B);
    		byte b = 0;
    		for (int i = channelBuffer.readerIndex() + 1; i < channelBuffer.writerIndex() -1; i ++) {
    			b = channelBuffer.getByte(i);
    			switch (b) {
    			case BYTE_5B:
    				retBuffer.writeShort(SHORT_5A01);
					break;
    			case BYTE_5A:
    				retBuffer.writeShort(SHORT_5A02);
					break;
				case BYTE_5D:
					retBuffer.writeShort(SHORT_5E01);
					break;
				case BYTE_5E:
					retBuffer.writeShort(SHORT_5E02);
					break;
				default:
					retBuffer.writeByte(b);
					break;
				}
    		}
    		retBuffer.writeByte(BYTE_5D);
    		
    		return retBuffer;
    	} 
    	return EMPTY_BUFFER;
    }
    
    /**
     * 获取转义前的buffer
     * 数据内容进行转义判断，转义规则如下：
     * a)若数据内容中有出现字符0x5b 的，需替换为字符0x5a 紧跟字符0x01；
     * b)若数据内容中有出现字符0x5a 的，需替换为字符0x5a 紧跟字符0x02；
     * c)若数据内容中有出现字符0x5d 的，需替换为字符0x5e 紧跟字符0x01；
     * d)若数据内容中有出现字符0x5e 的，需替换为字符0x5e 紧跟字符0x02。
     * @param channelBuffer
     * @return
     */
    public static ChannelBuffer getUnescapedBuffer(ChannelBuffer channelBuffer) {
    	if (channelBuffer != null) {
    		ChannelBuffer retBuffer = ChannelBuffers.buffer(channelBuffer.readableBytes());
    		
    		byte b1 = 0;
    		byte b2 = 0;

    		retBuffer.writeByte(BYTE_5B);
    		//去掉头尾标识
    		for (int i = channelBuffer.readerIndex() + 1; i < channelBuffer.writerIndex() -1; i ++) {
    			b1 = channelBuffer.getByte(i);
    			if (b1 == BYTE_5A) {
    				b2 = channelBuffer.getByte(i + 1);
    				if (b2 == BYTE_01) {
    					retBuffer.writeByte(BYTE_5B);
    					i ++;
    				} else if (b2 == BYTE_02) {
    					retBuffer.writeByte(BYTE_5A);
    					i ++;
    				} else {
    					retBuffer.writeByte(b1);
    				}
    			} else if (b1 == BYTE_5E) {
    				b2 = channelBuffer.getByte(i + 1);
    				if (b2 == BYTE_01) {
    					retBuffer.writeByte(BYTE_5D);
    					i ++;
    				} else if (b2 == BYTE_02) {
    					retBuffer.writeByte(BYTE_5E);
    					i ++;
    				} else {
    					retBuffer.writeByte(b1);
    				}
    			} else {
    				retBuffer.writeByte(b1);
    			}
    		}
    		retBuffer.writeByte(BYTE_5D);
    		
    		return retBuffer;
    	} 
    	return EMPTY_BUFFER;
    }
    
    /**
     * 获取telnet服务帮助内容
     * @return
     */
    public static String getTelnetHelpContent() {
    	StringBuilder sBuilder = new StringBuilder("\r\n");
    	sBuilder.append(KKTool.getFixedLenString("command", 15, ' ', false) + "| description\r\n");
    	sBuilder.append(KKTool.getFixedLenString("", 15, '-', false) + "| " + KKTool.getFixedLenString("", 30, '-', false) + "\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_HELP, 15, ' ', false) + "| get help content\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_QUIT, 15, ' ', false) + "| close the connection\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_START_MAINLINK, 15, ' ', false) + "| notice the 809 server to start main link\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_END_MAINLINK, 15, ' ', false) + "| notice the 809 server to end main link\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_START_SUBLINK, 15, ' ', false) + "| notice the 809 server to start sub link\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_END_SUBLINK, 15, ' ', false) + "| notice the 809 server to end sub link\r\n");
    	sBuilder.append(KKTool.getFixedLenString(Const.TELNET_CMD_LINK_STATUS, 15, ' ', false) + "| show main and sub link status\r\n");
    	return sBuilder.toString();
    }
    
    /**
     * 登录应答结果
     * @param ret
     * @return
     */
    public static String getLoginRespRet(byte ret) {
    	String sRet = Const.STR_UNKNOWN;
    	switch (ret) {
		case Const.RET_OK:
			sRet = Const.SRET_OK;
			break;
		case Const.RET_IP_ERR:
			sRet = Const.SRET_IP_ERR;
			break;
		case Const.RET_CENTERID_ERR:
			sRet = Const.SRET_CENTERID_ERR;
			break;
		case Const.RET_USER_UNREGISTERED:
			sRet = Const.SRET_USER_UNREGISTERED;
			break;
		case Const.RET_USER_PASS_ERR:
			sRet = Const.SRET_USER_PASS_ERR;
			break;
		case Const.RET_RES_LIMIT:
			sRet = Const.SRET_RES_LIMIT;
			break;
		case Const.RET_OTHER:
			sRet = Const.SRET_OTHER;
			break;
		default:
			sRet = sRet + "[" + byteToHexStr(ret) + "]";
			break;
		}
    	return sRet;
    }
    

    /**
     * 根据UTC时间获取日期
     * @param utc
     * @return
     */
    public static Date getDateFromUTC(long utc) {
    	return getCalendarFromUTC(utc).getTime();
    }
    
    
    /**
     * 根据UTC时间获取日历
     * @param utc
     * @return
     */
    public static Calendar getCalendarFromUTC(long utc) {
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(utc * 1000);
    	return c;
    }
    
    
    /**
     * 由Date格式时间获取UTC格式时间
     * @param date	
     * @return
     */
    public static long getUTC(Date date) {
		return date.getTime()/1000;
	}
    
    
    /**
     * 获取当前时间的UTC格式时间
     * @return
     */
    public static long getCurrUTC() {
    	return new Date().getTime()/1000;
    }
    
    /**
     * 字符串转反向数组<br>
     * 长度为奇数时，前补0
     * 示例:<br>
     * "123456", 返回 byte[]{56, 34, 12}<br>
     * "12345",	 返回 byte[]{45, 23, 01}<br>
     * 
     * 参数不合法返回长度为0的字节数据
     * @param src	传入参数必须为数字
     * @return	
     */
    public static byte[] strToRevertAry(String src) {
    	if (src == null || src.length() == 0) {
    		return new byte[0];
    	}
    	
    	if (src.length() % 2 == 1) {
    		src = "0" + src;
    	}
    	byte[] buf = new byte[src.length()/2];
    	

		try {
			for(int i = 0; i < buf.length; i ++) {
				buf[buf.length - 1 - i] = (byte)(Integer.parseInt(src.substring(2 * i, 2 * i + 2), 10)); 
			}
		} catch (Exception e) {
			return new byte[0];
		}
    	return buf;
    }

    public static void main(String[] args) {
    }
    
}
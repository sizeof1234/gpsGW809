/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jsecode.IGW809;
import com.jsecode.link.IMainSubLink;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.MyExceptionHandler;

/**
 * 所有独立扫描数据发送给上级平台的线程
 */
public abstract class AbstractThreadSendData<T> extends Thread implements ISendData<T> {
	
	private IGW809 gw809;
	private Object objNewData;
	private Object objLinkConnected;
	protected final Queue<T> queue;
	
	final static long DEFAULT_WAIT_NEW_DATA_TIME = 5 * 60 * 1000;
	final static long DEFAULT_WAIT_LINK_CONNECTED_TIME = 5 * 1000;
	
	public AbstractThreadSendData (IGW809 gw809, String threadName) {
		this.gw809 = gw809;
		objNewData = new Object();
		objLinkConnected = new Object();
		queue = new ConcurrentLinkedQueue<T>();
		if (!KKTool.isStrNullOrBlank(threadName)) {
			this.setName(threadName);
		}
		this.setUncaughtExceptionHandler(new MyExceptionHandler());
	}
	
	abstract public void run();
		
	/**
	 * 等待指定时间，或被提前唤醒
	 * @param time	等待时间
	 */
	protected void waitNewData(long time) {
		synchronized (objNewData) {
			try {
				objNewData.wait(time);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/**
	 * 默认等待1分钟，或被提前唤醒
	 */
	protected void waitNewData() {
		waitNewData(DEFAULT_WAIT_NEW_DATA_TIME);
	}

	protected void noticeNewData() {
		synchronized (objNewData) {
			objNewData.notify();
		}
	}
	
	public void noticeLinkConnected() {
		synchronized (objLinkConnected) {
			objLinkConnected.notify();
		}
	}
	
	protected void waitLinkConnected() {
		synchronized (objLinkConnected) {
			try {
				objLinkConnected.wait(3 * 1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	protected IMainSubLink getMainLink(boolean isSwitchedIfNotConnected) {
		return gw809.getMainLink(isSwitchedIfNotConnected);
	}
	
	protected IMainSubLink getSubLink(boolean isSwitchedIfNotConnected) {
		return gw809.getSubLink(isSwitchedIfNotConnected);
	}
	
	@Override
	public boolean addListToQueue(List<T> list) {
		if (list == null || list.indexOf(null) >= 0) {
			KKLog.warn(this.getName() + " addListToQueue: list is null or list contains null object, check your damn code.");
			return false;
		}
		
		if (!this.queue.addAll(list)) {
			KKLog.warn(this.getName() + "add data to queue fail");
		} else {
			noticeNewData();
		}
		return true;
	}

	@Override
	public boolean addSingleToQueue(T t) {
		if (t == null) {
			return false;
		}
		
		if (!this.queue.offer(t)) {
			KKLog.warn(this.getName() + " add data to queue fail");
		} else {
			noticeNewData();
		}
		return true;
	}

	@Override
	public int getQueueSize() {
		return this.queue.size();
	}
	
}

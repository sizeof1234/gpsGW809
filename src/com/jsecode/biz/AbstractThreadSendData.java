/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz;

import com.jsecode.IGW809;
import com.jsecode.link.IMainSubLink;

/**
 * 所有独立扫描数据发送给上级平台的线程
 */
public abstract class AbstractThreadSendData<T> extends AbstractThreadDisposeDataFromQueue<T> {
	
	private IGW809 gw809;
	private Object objLinkConnected;
	
	final static long DEFAULT_WAIT_LINK_CONNECTED_TIME = 5 * 1000;
	
	public AbstractThreadSendData (IGW809 gw809, String threadName) {
		super(threadName);
		this.gw809 = gw809;
		objLinkConnected = new Object();
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
	
}

package com.jsecode.biz;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.MyExceptionHandler;

/**
 * 所有处理队列数据的抽象类，该队列中的数据由其他线程添加.<br>
 * 队列有最大长度限制,添加数据时，超过设定的队列最大长度，将最先加入的数据丢弃，保证加入新数据后，队列不超限
 * @author 	Jadic
 * @created 2014-4-2
 */
public abstract class AbstractThreadDisposeDataFromQueue<T> extends Thread implements IAddDataToQueue<T> {
	
	final static long DEFAULT_WAIT_NEW_DATA_TIME = 5 * 60 * 1000;
	
	private Object objNewData;
	private final Queue<T> queue;

	public AbstractThreadDisposeDataFromQueue(String threadName) {
		this(threadName, DEFAULT_MAX_QUEUE_SIZE);
	}
	
	public AbstractThreadDisposeDataFromQueue(String threadName, int maxQueueSize) {
		this.objNewData = new Object();
		queue = new LinkedBlockingQueue<T>(maxQueueSize);
		
		if (!KKTool.isStrNullOrBlank(threadName)) {
			this.setName(threadName);
		}
		this.setUncaughtExceptionHandler(new MyExceptionHandler());		
	}
	
	abstract public void run();
	
	protected T getQueuePollData() {
		return queue.poll();
	}
	
	/**
	 * wait new data for default milliseconds
	 */
	protected void waitNewData() {
		waitNewData(DEFAULT_WAIT_NEW_DATA_TIME);
	}
	
	/**
	 * wait new data for specified milliseconds 
	 * @param milliseconds
	 */
	protected void waitNewData(long milliseconds) {
		synchronized (objNewData) {
			try {
				objNewData.wait(milliseconds);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	protected void noticeNewData() {
		synchronized (objNewData) {
			objNewData.notify();
		}
	}

	@Override
	public boolean addListToQueue(List<T> list) {
		boolean isAdded = false;
		for (T t : list) {
			isAdded = addToQueue(t, true);
		}
		if (isAdded) {
			noticeNewData();
		}
		return isAdded;
	}

	@Override
	public boolean addSingleToQueue(T t) {
		boolean isAdded = addToQueue(t, true);
		if (isAdded) {
			noticeNewData();
		}
		return isAdded;
	}
	
	/**
	 * 向队列中添加元素
	 * @param t	  待添加元素
	 * @param isPopHeadIfFull true:移除头元素 false:抛弃待添加元素
	 * @return true: 元素被成功加入队列，不管是否移除头元素
	 */
	private boolean addToQueue(T t, boolean isPopHeadIfFull) {
		if (t != null) {
			boolean isAdded = this.queue.offer(t);
			if (!isAdded) {//queue is full
				if (isPopHeadIfFull) {
					this.queue.poll();
					isAdded = this.queue.offer(t);
					KKLog.warn(this.getName() + " add data to queue fail, the queue may be full, pop head object");
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getQueueSize() {
		return this.queue.size();
	}

}

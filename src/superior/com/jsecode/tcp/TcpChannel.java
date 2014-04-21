package superior.com.jsecode.tcp;

import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

import com.jsecode.utils.KKLog;

/**
 * wrapped with {@link org.jboss.netty.channel.Channel}
 * @author 	Jadic
 * @created 2014-4-2
 */
public class TcpChannel {
	
	final static int MAX_BUF_SIZE = 100000;
	
	private SuperiorTcpServer tcpServer;
	
	private Queue<ChannelBuffer> bufQueue;
	private Channel channel;
	
	private volatile boolean isClosed;
	private volatile boolean isDisposing;
	private Lock lock;
	private long lastRecvDataTime;
	private int gateWayNo;

	public TcpChannel(Channel channel, SuperiorTcpServer tcpServer) {
		this.tcpServer = tcpServer;
		this.bufQueue = new LinkedBlockingQueue<ChannelBuffer>(MAX_BUF_SIZE);
		this.channel = channel;
		lock = new ReentrantLock();
		this.lastRecvDataTime = System.currentTimeMillis();
		this.isClosed = false;
		this.isDisposing = false;
	}
	
	/**
	 * add recv data from client, and submit dispose task if succeed to add
	 * @param buffer
	 * @return true if succeed to add to queue
	 */
	public boolean addRecvDataAndDispose(ChannelBuffer buffer) {
		if (addRecvData(buffer)) {
			disposeBufferData();
			return true;
		}
		return false;
	}
	
	/**
	 * add recv data from client<br>
	 * if the queue is full, remove the head element
	 * @param buffer
	 * @return true if succeed to add to queue
	 */
	public boolean addRecvData(ChannelBuffer buffer) {
		if (isClosed) {
			KKLog.info(this.toString() + "-- is closed, adding recv data is rejected");
			return false;
		}
		if (buffer == null) {
			return false;
		}
		
		this.lastRecvDataTime = System.currentTimeMillis();
		
		boolean isAdded = this.bufQueue.offer(buffer);
		if (!isAdded) {
			this.bufQueue.poll();
			isAdded = this.bufQueue.offer(buffer);
			KKLog.warn("[" + this + "] buffer queue is full(100K), the disposing thread seems to be slow");
		}
		
		return isAdded;		
	}
	
	/**
	 * dispose buffer data in the queue
	 */
	public void disposeBufferData() {
		if (!isDisposing) {//avoid disposing repeatedly
			this.tcpServer.executeDisposeTask(this);
		}
	}
	
	/**
	 * send data from the wrapped channel
	 * @param buffer
	 * @return
	 */
	public boolean sendData(ChannelBuffer buffer) {
		if (channel != null && channel.isConnected()) {
			channel.write(buffer);
			return true;
		}
		return false;
	}
	
	/**
	 * close the tcp channel<br>
	 * dispose all the buffer data if the queue is not empty
	 */
	public void close() {
		if (isClosed) {
			return ;
		}
		
		this.isClosed = true;
		disposeBufferData();//the buffer data in the queue may not be all disposed
	}
	
	/**
	 * @return the first buffer in the queue
	 */
	public ChannelBuffer getNextBuffer() {
		return this.bufQueue.poll();
	}
	
	public int getBufferQueueSize() {
		return this.bufQueue.size();
	}

	public boolean isDisposing() {
		return isDisposing;
	}

	public void setDisposing(boolean isDisposing) {
		this.isDisposing = isDisposing;
	}
	
	/**
	 * if disposeFlag is false, then set the flag true, and return true<br>
	 * otherwise return false
	 * @return
	 */
	public boolean checkAndSetDisposeFlag() {
		if (isDisposing) {
			return false;
		} else {
			lock.lock();
			try {
				if (!isDisposing) {
					isDisposing = true;
					return true;
				}
				return false;
			} catch (Exception e) {
			} finally {
				lock.unlock();
			}
			return false;
		}
	}

	/**
	 * @return true if the current wrapped channel is closed
	 */
	public boolean isClosed() {
		return isClosed;
	}
	
	public int getId() {
		return this.channel.getId();
	}
	
	public SocketAddress getSocektAddress() {
		return this.channel.getRemoteAddress();
	}
	
	@Override
	public String toString() {
		return "id:" + getId() + ",address:" + getSocektAddress();
	}

	public long getLastRecvDataTime() {
		return lastRecvDataTime;
	}

	public int getGateWayNo() {
		return gateWayNo;
	}

	public void setGateWayNo(int gateWayNo) {
		this.gateWayNo = gateWayNo;
	}

	public SuperiorTcpServer getTcpServer() {
		return tcpServer;
	}

}

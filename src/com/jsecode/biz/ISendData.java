/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz;

import java.util.List;

/**
 * 所有独立发送数据的线程共有接口
 */
public interface ISendData<T> {

	/**
	 * 加入数据列表到待发送到队列中
	 * @param list
	 * @return
	 */
	boolean addListToQueue(List<T> list);
	
	/**
	 * 加入单个数据到待发送队列中
	 * @param t
	 * @return
	 */
	boolean addSingleToQueue(T t);
	
	/**
	 * 获取待发送队列当前长度
	 * @return
	 */
	int getQueueSize();
	
}

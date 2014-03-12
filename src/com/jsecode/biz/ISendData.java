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

	boolean addListToQueue(List<T> list);
	boolean addSingleToQueue(T t);
	int getQueueSize();
	
}

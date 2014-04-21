/**
 * @author 	Jadic
 * @created 2014-3-11
 */
package com.jsecode.biz;

import java.util.List;

/**
 * 添加待处理数据至队列的公共接口
 */
public interface IAddDataToQueue<T> {
	
	int DEFAULT_MAX_QUEUE_SIZE = 50000;

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

package com.jsecode;

import com.jsecode.cmd.CmdHead;

/**
 * 此接口设计用于接入三方数据时，模拟的上级平台接收后通过该接口转发数据至标准下级平台服务，<br>
 * 并通过标准下级平台服务转发数据至政府平台
 * @author 	Jadic
 * @created 2014-4-17
 */
public interface ISuperiorToInferior {
	
	/**
	 * 转发命令<br>
	 * 约定:<br>
	 * &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
	 * 1.cmd参数所有字段都已赋值，无特殊情况，标准下级平台收到该命令后，可直接转发至政府平台
	 * @param cmd
	 */
	void forwardCmd(CmdHead cmd);

}

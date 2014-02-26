/**
 * @author 	Jadic
 * @created 2014-2-20
 */
package com.jsecode.cmd.bean;

public abstract class AbstractCmdBean implements ICmdBean {

	public boolean isByteArraySameSize(byte[] buf1, byte[] buf2) {
		return buf1 != null && buf2 != null && buf1.length == buf2.length;
	}

}

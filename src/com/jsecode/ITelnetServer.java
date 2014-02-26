/**
 * @author 	Jadic
 * @created 2014-2-21
 */
package com.jsecode;

import org.jboss.netty.channel.Channel;

public interface ITelnetServer {

	public void disposeTelnetData(Channel channel, String request);
	public void doOnTelnetConnection(Channel channel);
}

package superior.com.jsecode;

import com.jsecode.ISuperiorToInferior;

import superior.com.jsecode.tcp.SuperiorTcpServer;

/**
 * 上级平台服务模拟<br>
 * 模拟的上级平台暂时只实现主链路
 * @author 	Jadic
 * @created 2014-4-2
 */
public class SuperiorSimulator {
	
	private SuperiorTcpServer superiorTcpServer;

	public SuperiorSimulator(ISuperiorToInferior iSuperiorToInferior) {
		this.superiorTcpServer = new SuperiorTcpServer(iSuperiorToInferior);
	}
	
	public void start() {
		this.superiorTcpServer.start();
	}
	
	public void stop() {
		this.superiorTcpServer.stop();
	}

	public static void main(String[] args) {
		new SuperiorSimulator(null);
	}
}

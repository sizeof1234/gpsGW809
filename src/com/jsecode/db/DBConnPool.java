/**
 * @author Jadic
 * @created 2011-10-25 
 */
package com.jsecode.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.SysParams;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBConnPool {
	private static DBConnPool connPool = null;
	private BoneCP boneCP = null;

	public static synchronized DBConnPool getInstance()
			throws ClassNotFoundException, SQLException {
		if (connPool == null) {
			connPool = new DBConnPool();
			KKLog.info("DBConnPool created");
		}
		return connPool;
	}

	private DBConnPool() throws ClassNotFoundException,
			SQLException {
		SysParams sysParams = SysParams.getInstance();
		Class.forName(sysParams.getJdbcDriver());
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(sysParams.getJdbcUrl());
		config.setUsername(sysParams.getDbUserName());
		config.setPassword(sysParams.getDbUserPass());
		config.setMinConnectionsPerPartition(2);
		config.setMaxConnectionsPerPartition(5);
		config.setPartitionCount(2);
		config.setAcquireIncrement(3);
		config.setIdleMaxAgeInMinutes(20);
		config.setIdleConnectionTestPeriodInMinutes(2);
		boneCP = new BoneCP(config);
	}

	public Connection getConnection() throws SQLException {
		return boneCP.getConnection();
	}

	public void shutDown() {
		boneCP.shutdown();
	}

}

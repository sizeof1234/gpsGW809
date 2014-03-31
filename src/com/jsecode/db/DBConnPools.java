/**
 * @author Jadic
 * @created 2011-10-25 
 */
package com.jsecode.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jsecode.utils.JDBCConfig;

public class DBConnPools {

	private static Map<String, DBConnPool> dbConnPoolMap = new ConcurrentHashMap<String, DBConnPool>();

	public static synchronized DBConnPool getDbConnPool(JDBCConfig jdbcConfig) throws ClassNotFoundException, SQLException {
		DBConnPool dbConnPool = dbConnPoolMap.get(jdbcConfig.toString());
		if (dbConnPool == null) {
			dbConnPool = new DBConnPool(jdbcConfig);
			dbConnPoolMap.put(jdbcConfig.toString(), dbConnPool);
		}
		return dbConnPool;
	}
	
	public static void releasePools() {
		Collection<DBConnPool> dbConnPools = dbConnPoolMap.values();
		for (DBConnPool dbConnPool : dbConnPools) {
			dbConnPool.shutDown();
		}
	}
	
	private DBConnPools() {
	}

}

class DBConnPool {
	private String jdbcFlag;
	private BoneCP boneCP = null;

	public DBConnPool(JDBCConfig jdbcConfig) throws ClassNotFoundException, SQLException {
		Class.forName(jdbcConfig.getJdbcDriver());
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(jdbcConfig.getJdbcUrl());
		config.setUsername(jdbcConfig.getDbUserName());
		config.setPassword(jdbcConfig.getDbUserPass());
		config.setMinConnectionsPerPartition(2);
		config.setMaxConnectionsPerPartition(5);
		config.setPartitionCount(2);
		config.setAcquireIncrement(3);
		config.setIdleMaxAgeInMinutes(20);
		config.setIdleConnectionTestPeriodInMinutes(2);
		boneCP = new BoneCP(config);
		this.jdbcFlag = jdbcConfig.getKey();
	}

	public Connection getConnection() throws SQLException {
		return boneCP.getConnection();
	}

	public void shutDown() {
		boneCP.shutdown();
	}

	/**
	 * jdbcDriver+jdbcUrl+dbUserName+dbUserPass
	 * @return
	 */
	public String getJdbcFlag() {
		return jdbcFlag;
	}
	
}

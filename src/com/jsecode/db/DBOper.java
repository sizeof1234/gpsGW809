package com.jsecode.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;

/**
 * 数据库操作
 */
public final class DBOper {
	
	private final static DBOper dbOper = new DBOper();
	private DBConnPool dbConnPool;
	
	public synchronized static DBOper getDBOper() {
		return dbOper;
	}

	private DBOper() {
		try {
			dbConnPool = DBConnPool.getInstance();
		} catch (ClassNotFoundException e) {
			KKLog.error("DBOper create ClassNotFoundException:" + e.getMessage());
		} catch (SQLException e) {
			KKLog.error("DBOper create SQLException:" + e.getMessage());
		}
	}
	
	public long getCarInfoMaxRowscn() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SQL.CAR_MAX_ROWSCN);
			while (resultSet.next()) {
				return resultSet.getLong("maxr");
			}
		} catch (SQLException e) {
			KKLog.error("getCarInfoMaxRowscn异常:" + e.getMessage());
		} finally {
			KKTool.closeRS_Statement_ConnInSilence(resultSet, statement, connection);
		}
		return 0;
	}
	
	private Connection getConnection() throws SQLException {
		return dbConnPool.getConnection();
	}
	
	public void release() {
		KKLog.info("释放数据库连接");
		this.dbConnPool.shutDown();
	}
	
}

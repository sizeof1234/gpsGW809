package com.jsecode.utils;

/**
 * @author 	Jadic
 * @created 2014-3-26
 */
public class JDBCConfig {

	private String name;
	private String jdbcDriver;
	private String jdbcUrl;
	private String dbUserName;
	private String dbUserPass;

	public JDBCConfig() {
	}

	public JDBCConfig(String name, String jdbcDriver, String jdbcUrl, String dbUserName, String dbUserPass) {
		this.name = name;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.dbUserName = dbUserName;
		this.dbUserPass = dbUserPass;
	}
	
	public String getKey() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("jdbcDriver:");
		sBuilder.append(jdbcDriver);
		sBuilder.append(",jdbcUrl:");
		sBuilder.append(jdbcUrl);
		sBuilder.append(",dbUserName:");
		sBuilder.append(dbUserName);
		sBuilder.append(",dbUserPass:");
		sBuilder.append(dbUserPass);
		return sBuilder.toString();
	}

	@Override
	public String toString() {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("name:");
		sBuilder.append(name);
		sBuilder.append(",jdbcDriver:");
		sBuilder.append(jdbcDriver);
		sBuilder.append(",jdbcUrl:");
		sBuilder.append(jdbcUrl);
		sBuilder.append(",dbUserName:");
		sBuilder.append(dbUserName);
		sBuilder.append(",dbUserPass:");
		sBuilder.append(dbUserPass);
		return sBuilder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserPass() {
		return dbUserPass;
	}

	public void setDbUserPass(String dbUserPass) {
		this.dbUserPass = dbUserPass;
	}


}

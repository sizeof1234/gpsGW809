package com.jsecode.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

public class SysParams {
	
	private final static SysParams sysParams = new SysParams();
	private KKConfig kkConfig;
	
	/* tcp相关 */
	private String mainLinkIp;//主链路IP
	private int mainLinkPort;//主链路端口
	private String subLinkIp;//从链路提供的IP
	private short subLinkPort;//从链路监听端口
	private int userId;	 //主链路登录用户ID
	private String userPass;//主链路登录用户密码
	private int telnetPort;//telnet监听端口
	
	
	/* 数据库相关参数 */
	private String jdbcDriver;
	private String jdbcUrl;
	private String dbUserName;
	private String dbUserPass;
	
	private List<JDBCConfig> jdbcList;
	
	/* 与上级本平台对接时加密数据所需参数 */
	private int gnssCenterId;//平台所需接入码
	private String verFlag;//协议版本标识
	private boolean isEncrypted;//是否加密
	private int encryptionKey;//加密密钥
	private int m1;
	private int ia1;
	private int ic1;
	
	/* 实际业务中需要配置的参数 */
	private int minVehicleNoGBKSize;//车牌号GBK编码最短长度
	private int maxVehicleNoGBKSize;//车牌号GBK编码最长长度

	public static SysParams getInstance() {
		return sysParams;
	}
	
	private SysParams() {
		String configFilePath = "." + System.getProperty("file.separator")
				+ "gw809.properties";
		kkConfig = new KKConfig(configFilePath);
		jdbcList = new ArrayList<JDBCConfig>();
		this.loadSysParams();
	}

	public void loadSysParams() {
		this.mainLinkIp = kkConfig.getStrValue("mainLinkIp");
		this.mainLinkPort = kkConfig.getIntValue("mainLinkPort");
		this.subLinkIp = kkConfig.getStrValue("subLinkIp");
		this.subLinkPort = (short)kkConfig.getIntValue("subLinkPort");
		this.userId = kkConfig.getIntValue("userId");
		this.userPass = kkConfig.getStrValue("userPass");
		this.telnetPort = kkConfig.getIntValue("telnetPort");
		
		this.gnssCenterId = kkConfig.getIntValue("gnssCenterId");
		this.verFlag = kkConfig.getStrValue("verFlag");
		this.isEncrypted = kkConfig.getIntValue("isEncrypted") == 1;
		this.encryptionKey = kkConfig.getIntValue("encryptionKey");
		this.m1 = kkConfig.getIntValue("m1");
		this.ia1 = kkConfig.getIntValue("ia1");
		this.ic1 = kkConfig.getIntValue("ic1");
		
		int jdbcCount = kkConfig.getIntValue("jdbcCount");
		String jdbcName, jdbcDriver, jdbcUrl, dbUserName, dbUserPass = null;
		for (int i = 0; i < jdbcCount; i ++) {
			jdbcName = getDefaultStr(kkConfig.getStrValue("jdbcName" + (i + 1)), "jdbc" + (i + 1));
			jdbcDriver = kkConfig.getStrValue("jdbcDriver" + (i + 1));
			jdbcUrl = kkConfig.getStrValue("jdbcUrl" + (i + 1));
			dbUserName = kkConfig.getStrValue("dbUserName" + (i + 1));
			dbUserPass = kkConfig.getStrValue("dbUserPass" + (i + 1));
			if (!isStringsNullOrEmpty(jdbcDriver, jdbcUrl, dbUserName, dbUserPass)) {
				JDBCConfig jdbcConfig = new JDBCConfig(jdbcName, jdbcDriver, jdbcUrl, dbUserName, dbUserPass);
				this.jdbcList.add(jdbcConfig);
			}
		}
		
		this.minVehicleNoGBKSize = kkConfig.getIntValue("minVehicleNoGBKSize");
		this.maxVehicleNoGBKSize = kkConfig.getIntValue("maxVehicleNoGBKSize");
	}
	
	public static String getDefaultStr(String expectedVal, String defaultVal) {
		if (KKTool.isStrNullOrBlank(expectedVal)) {
			return KKTool.isStrNullOrBlank(defaultVal) ? "" : defaultVal;
		}
		return expectedVal;
	}

	public static boolean isStringsNullOrEmpty(String... strs) {
		if (strs != null) {
			for (String s : strs) {
				if (s == null || s.length() == 0) {
					return true;
				}
			}
			return false;
		} 
		return true;
	}
	
	public String getMainLinkIp() {
		return mainLinkIp;
	}

	public int getMainLinkPort() {
		return mainLinkPort;
	}
	
	public String getSubLinkIp() {
		return subLinkIp;
	}

	public short getSubLinkPort() {
		return subLinkPort;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserPass() {
		return userPass;
	}

	public int getTelnetPort() {
		return telnetPort;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public String getDbUserPass() {
		return dbUserPass;
	}

	public int getGnssCenterId() {
		return gnssCenterId;
	}

	public String getVerFlag() {
		return verFlag;
	}

	public boolean isEncrypted() {
		return isEncrypted;
	}

	public int getEncryptionKey() {
		return encryptionKey;
	}

	public int getM1() {
		return m1;
	}

	public int getIa1() {
		return ia1;
	}

	public int getIc1() {
		return ic1;
	}

	public int getMinVehicleNoGBKSize() {
		return minVehicleNoGBKSize;
	}

	public int getMaxVehicleNoGBKSize() {
		return maxVehicleNoGBKSize;
	}

	public List<JDBCConfig> getJdbcListCopy() {
		return new ArrayList<JDBCConfig>(jdbcList);
	}

}

class KKConfig {
	private Properties properties;
    private FileInputStream inputStream;
    private String configFileName;

    public KKConfig(String configFilePath) {
        this.properties = new Properties();
        this.configFileName = System.getProperty("user.dir") + System.getProperty("file.separator") + configFilePath;
        try {
            this.inputStream = new FileInputStream(configFileName);
            this.properties.load(this.inputStream);
        } catch (FileNotFoundException ex) {
            KKLog.error("Can't find config file[" + configFilePath + "]");
        } catch (IOException ex) {
        	KKLog.error("Read config file IOException");
        } finally {
        	try {
        		if (this.inputStream != null)
        			this.inputStream.close();
			} catch (IOException e) {
			}
        }
    }

    public String getStrValue(String key) {
        if (this.properties.containsKey(key)) {
            return this.properties.getProperty(key, "").trim();
        }
        return "";
    }

    public int getIntValue(String key) {
        if (this.properties.containsKey(key)) {
            try {
                return Integer.parseInt(this.properties.getProperty(key, "0"));
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
    
    public boolean writeValue(Map<String, String> params) {
    	FileOutputStream fos;
    	try {
			fos = new FileOutputStream(this.configFileName);
			Set<Map.Entry<String, String>> paramSet = params.entrySet();
			Iterator<Entry<String, String>> ite = paramSet.iterator();
			while (ite.hasNext()) {
				Entry<String, String> param = ite.next();
				this.properties.setProperty(param.getKey(), param.getValue());
			}
			this.properties.store(fos, "set");
			return true;
		} catch (FileNotFoundException e1) {
			KKLog.error("write to config file err: " + this.configFileName + " not found");
    	} catch (IOException e) {
    		KKLog.error("Write to config file err: IOException");
		}
    	return false;
    }
}
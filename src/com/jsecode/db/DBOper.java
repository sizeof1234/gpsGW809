package com.jsecode.db;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.jsecode.bean.DriverBean;
import com.jsecode.bean.GpsBean;
import com.jsecode.bean.TerminalBean;
import com.jsecode.bean.platform.PlatformMsgBean;
import com.jsecode.bean.warnmsg.WarnMsgAdptInfoBean;
import com.jsecode.bean.warnmsg.WarnMsgUrgeTodoReqBean;
import com.jsecode.cmd.CmdHead;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgEmergencyMonitoringReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgMonitorVehicleReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTakePhotoReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTakeTravelReq;
import com.jsecode.cmd.down.req.CmdDownCtrlMsgTextInfo;
import com.jsecode.cmd.down.req.CmdDownPlatformMsgInfoReq;
import com.jsecode.cmd.down.req.CmdDownPlatformMsgPostQueryReq;
import com.jsecode.cmd.down.req.CmdDownWarnMsgExgInform;
import com.jsecode.cmd.down.req.CmdDownWarnMsgInformTips;
import com.jsecode.cmd.down.req.CmdDownWarnMsgUrgeTodoReq;
import com.jsecode.utils.Const;
import com.jsecode.utils.JDBCConfig;
import com.jsecode.utils.KKLog;
import com.jsecode.utils.KKTool;
import com.jsecode.utils.SysParams;


/**
 * 数据库操作
 */
public final class DBOper {

    private final static DBOper dbOper = new DBOper();
    private DBConnPool masterPool;

    public synchronized static DBOper getDBOper() {
        return dbOper;
    }

    private DBOper() {
    	List<JDBCConfig> jdbcList = SysParams.getInstance().getJdbcListCopy();
    	if (jdbcList.size() <= 0) {
    		KKLog.error("no jdbc config is set, please check");
    		return;
    	}
        try {
        	masterPool = DBConnPools.getDbConnPool(jdbcList.get(0));
        } catch (ClassNotFoundException e) {
            KKLog.error("DBOper create ClassNotFoundException:" + e.getMessage());
        } catch (SQLException e) {
            KKLog.error("DBOper create SQLException:" + e.getMessage());
        }
    }
    /**
     * 查询终端基础信息
     * @return
     */
    public List<TerminalBean> queryTerminals() {
        return queryForList(SQL.QUERY_TERMINAL_INFO, null, TerminalBean.class);
    }

    /**
     * 查询最新的定位数据
     * @return
     */
    public List<GpsBean> queryCurrentGpsData(Date dbLastUpdateSysTime) {
        return queryForList(SQL.QUERY_CURRENT_GPS, new Object[]{new Timestamp(dbLastUpdateSysTime.getTime())}, GpsBean.class);
    }
    
    /**
     * 获取司机基础信息
     * @return
     */
    public List<DriverBean> queryDrivers() {
    	//TODO get driver info
    	return null;
    }
    /**
     * 获取电子运单
     * @return
     */
    public String getEWayBill(String vehicleNo) { 
    	//TODO get ewaybill info
    	return null;
    }

    public Date getDBsysTime() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getMasterConnection();
            statement = connection.prepareStatement(SQL.GET_DB_SYS_TIME);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getTimestamp("sysdate");
            }
        } catch (SQLException e) {
            KKLog.error("getCarInfoMaxRowscn异常:" + e.getMessage());
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(resultSet, statement, connection);
        }
        return new Date();
    }

    public void test() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getMasterConnection();
            statement = connection.prepareStatement("select status_updatetime, pos_time from host_curstatus where status_updatetime > ?");
            statement.setObject(1, new java.sql.Timestamp(new Date().getTime()));
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                java.util.Date date = resultSet.getTimestamp("pos_time");
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date.getTime());
                KKLog.info(c.getTime());
            }
        } catch (SQLException e) {
            KKLog.error("getCarInfoMaxRowscn异常:" + e.getMessage());
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(resultSet, statement, connection);
        }
    }

    public long getCarInfoMaxRowscn() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getMasterConnection();
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

    private  <T> List<T> queryForList(String sql,Object[] paramsObj, Class<T> objClass) {
        List<T> list = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getMasterConnection();
            pstmt = connection.prepareStatement(sql);
            if (paramsObj != null) {
                for (int i = 0; i < paramsObj.length; i++) {
                    pstmt.setObject(i + 1, paramsObj[i]);
                }
            }
            Field[] fields = objClass.getDeclaredFields();
            // 执行查询
            rs = pstmt.executeQuery();
            while (rs.next()) {
                T t = objClass.newInstance();
                for (Field field : fields) {
                    try {
                        Object val = getRsVal(rs, field);
                        Method m = objClass.getMethod(getSetterMethod(field.getName()), field.getType());
                        if (val != null) {
                            m.invoke(t, val);
                        }
                    } catch (Exception e) {
                        KKLog.info("set field data err:" + KKTool.getExceptionTip(e));
                    }
                }
                list.add(t);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(rs, pstmt, connection);
        }
        return list;
    }

    private Object getRsVal(ResultSet rs, Field field) {
        Object val = null;
        String fieldType = field.getType().getName();
        String fieldName = field.getName();
        try {
            if (fieldType.equals(String.class.getName())) {
                val = rs.getString(fieldName);
            } else if (fieldType.equals(Byte.class.getName())){
                val = rs.getByte(fieldName);
            } else if (fieldType.equals(byte.class.getName())){
                val = rs.getByte(fieldName);
            } else if (fieldType.equals(Short.class.getName())){
                val = rs.getShort(fieldName);
            } else if (fieldType.equals(short.class.getName())){
                val = rs.getShort(fieldName);
            } else if (fieldType.equals(Integer.class.getName())){
                val = rs.getInt(fieldName);
            } else if (fieldType.equals(int.class.getName())){
                val = rs.getInt(fieldName);
            } else if (fieldType.equals(Long.class.getName())){
                val = rs.getLong(fieldName);
            } else if (fieldType.equals(long.class.getName())){
                val = rs.getLong(fieldName);
            } else if (fieldType.equals(BigDecimal.class.getName())){
                val = rs.getBigDecimal(fieldName);
            } else if (fieldType.equals(Boolean.class.getName())){
                val = rs.getBoolean(fieldName);
            } else if (fieldType.equals(boolean.class.getName())){
                val = rs.getBoolean(fieldName);
            } else if (fieldType.equals(Date.class.getName())){
                val = rs.getTimestamp(fieldName);
            } else if (fieldType.equals(java.sql.Date.class.getName())){
                val = rs.getDate(fieldName);
            } else if (fieldType.equals(Double.class.getName())){
                val = rs.getDouble(fieldName);
            } else if (fieldType.equals(double.class.getName())){
                val = rs.getDouble(fieldName);
            } else if (fieldType.equals(Float.class.getName())){
                val = rs.getFloat(fieldName);
            } else if (fieldType.equals(Time.class.getName())){
                val = rs.getTime(fieldName);
            } else if (fieldType.equals(Timestamp.class.getName())){
                val = rs.getTimestamp(fieldName);
            }
        } catch (SQLException e) {
        }
        return val;
    }

    private String getSetterMethod(String fieldName) {
        if (!KKTool.isStrNullOrBlank(fieldName)) {
            return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        return Const.EMPTY_STR;
    }
    
    /**
     * 存储命令信息到数据库中
     * @param cmdHead	命令父类，提取公共信息
     * @return	true:存储成功
     */
    public boolean saveCmdInfoToDB(CmdHead cmdHead) {
    	return false;
    }
/**********************************************平台间信息交互业务START*****************************************************************/

    /**
     * 平台查岗请求 DOWN_PLATFORM_MSG_POST_QUERY_REQ
     * @param cmdDownPlatformMsgPostQueryReq
     * @return
     */

    public int insertDownPlatformMsgPostQueryReq(CmdDownPlatformMsgPostQueryReq cmdDownPlatformMsgPostQueryReq){
        //平台查岗数据入库，供客户端展示
        Object [] paramsObj = new Object[8];
        int [] paramsType = new int[8];
        //01:平台查岗请求
        paramsObj[0]="01";
        paramsType[0]= Types.VARCHAR;
        paramsObj[1]=(int)cmdDownPlatformMsgPostQueryReq.getSubMsgId();
        paramsType[1]= Types.INTEGER;
        paramsObj[2]=cmdDownPlatformMsgPostQueryReq.getSubDataSize();
        paramsType[2]= Types.INTEGER;
        paramsObj[3]=cmdDownPlatformMsgPostQueryReq.getObjectType()+"";
        paramsType[3]= Types.VARCHAR;
        paramsObj[4]=KKTool.byteArrayToHexStr(cmdDownPlatformMsgPostQueryReq.getObjectId());
        paramsType[4]= Types.VARCHAR;
        paramsObj[5]=cmdDownPlatformMsgPostQueryReq.getInfoId();
        paramsType[5]= Types.INTEGER;
        paramsObj[6]=cmdDownPlatformMsgPostQueryReq.getInfoLength();
        paramsType[6]= Types.INTEGER;
        try {
            paramsObj[7]=new String (cmdDownPlatformMsgPostQueryReq.getInfoContent(),"UTF-8");;
        } catch (UnsupportedEncodingException e) {
            paramsObj[7]="dealCmdDownPlatformMsgPostQueryReq转换出错"+cmdDownPlatformMsgPostQueryReq.getInfoContent();
            e.printStackTrace();
        }
        paramsType[7]= Types.VARCHAR;
        return executeUpdate(SQL.INSERT_DOWN_PLATFORM_MSG,paramsObj,paramsType);
    }
    /**
     * 下发平台间报文请求消息 DOWN_PLATFORM_MSG_INFO_REQ。
     * @param cmdDownPlatformMsgInfoReq
     * @return
     */
    public int insertDownPlatformMsgInfoReq(CmdDownPlatformMsgInfoReq cmdDownPlatformMsgInfoReq){
        //平台查岗数据入库，供客户端展示
        Object [] paramsObj = new Object[8];
        int [] paramsType = new int[8];
        //02:下发平台间报文请求
        paramsObj[0]="02";
        paramsType[0]= Types.VARCHAR;
        paramsObj[1]=(int)cmdDownPlatformMsgInfoReq.getSubMsgId();
        paramsType[1]= Types.INTEGER;
        paramsObj[2]=cmdDownPlatformMsgInfoReq.getSubDataSize();
        paramsType[2]= Types.INTEGER;
        paramsObj[3]=cmdDownPlatformMsgInfoReq.getObjectType()+"";
        paramsType[3]= Types.VARCHAR;
        paramsObj[4]=KKTool.byteArrayToHexStr(cmdDownPlatformMsgInfoReq.getObjectId());
        paramsType[4]= Types.VARCHAR;
        paramsObj[5]=cmdDownPlatformMsgInfoReq.getInfoId();
        paramsType[5]= Types.INTEGER;
        paramsObj[6]=cmdDownPlatformMsgInfoReq.getInfoLength();
        paramsType[6]= Types.INTEGER;
        try {
            paramsObj[7]=new String (cmdDownPlatformMsgInfoReq.getInfoContent(),"UTF-8");;
        } catch (UnsupportedEncodingException e) {
            paramsObj[7]="dealCmdDownPlatformMsgInfoReq:转换出错"+cmdDownPlatformMsgInfoReq.getInfoContent();
            e.printStackTrace();
        }
        paramsType[7]= Types.VARCHAR;
        return executeUpdate(SQL.INSERT_DOWN_PLATFORM_MSG,paramsObj,paramsType);
    }
    /**
     * 平台查岗应答 UP_PLATFORM_MSG_POST_QUERY_ACK
     * @param platformMsg
     * @return
     */
    public int updatePlatformMsgPostQueryAck(PlatformMsgBean platformMsg){
        Object [] paramsObj = new Object[1];
        int [] paramsType = new int[1];
        //更新应答状态
        paramsObj[0]=platformMsg.getId();
        paramsType[0]= Types.NUMERIC;
        return executeUpdate(SQL.UPDATE_PLATFORM_MSG, paramsObj, paramsType);
    }
    /**
     * 查询需要应答信息的列表
     * @param platformMsgBean
     * @param objClass
     * @param <T>
     * @return
     */
    public  <T> List<T> findPlatformMsgDataMultiple( PlatformMsgBean platformMsgBean, Class<T> objClass){
        return executeQueryForMultiple(SQL.SELECT_PLATFORM_MSG, new Object[]{platformMsgBean.getReqType(),platformMsgBean.getReplyTime()}, objClass);
    }

    /**
     * 查询需要应答信息的列表
     * @return
     */
    public  Object findSysdate(){
        return executeQueryForObj(SQL.SELECT_SYSDATE_STR,null);
    }
/**********************************************平台间信息交互业务END*****************************************************************/

/**********************************************车辆报警信息交互START*****************************************************************/
    /**
     * 报警督办请求消息 DOWN_WARN_MSG_URGE_TODO_REQ
     * @param cmdDownWarnMsgUrgeTodoReq
     * @return
     */
    public int insertDownWarnMsgUrgeTodo(CmdDownWarnMsgUrgeTodoReq cmdDownWarnMsgUrgeTodoReq){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        paramsObj[0]=cmdDownWarnMsgUrgeTodoReq.getVehicleNo();
        paramsType[0]= Types.VARCHAR;
        paramsObj[1]=cmdDownWarnMsgUrgeTodoReq.getVehicleColor();
        paramsType[1]= Types.VARCHAR;
        paramsObj[2]=cmdDownWarnMsgUrgeTodoReq.getSubMsgId();
        paramsType[2]= Types.VARCHAR;
        paramsObj[3]=cmdDownWarnMsgUrgeTodoReq.getSubDataSize();
        paramsType[3]= Types.INTEGER;
        paramsObj[4]=cmdDownWarnMsgUrgeTodoReq.getWarnSrc();
        paramsType[4]= Types.VARCHAR;
        paramsObj[5]=cmdDownWarnMsgUrgeTodoReq.getWarnType();
        paramsType[5]= Types.VARCHAR;
        paramsObj[6]=cmdDownWarnMsgUrgeTodoReq.getWarnTime();
        paramsType[6]= Types.NUMERIC;
        paramsObj[7]=cmdDownWarnMsgUrgeTodoReq.getSupervisionId();
        paramsType[7]= Types.VARCHAR;
        paramsObj[8]=cmdDownWarnMsgUrgeTodoReq.getSupervisionEndTime();
        paramsType[8]= Types.NUMERIC;
        paramsObj[9]=cmdDownWarnMsgUrgeTodoReq.getSupervisionLevel();
        paramsType[9]= Types.VARCHAR;
        paramsObj[10]=cmdDownWarnMsgUrgeTodoReq.getSuperVisor();
        paramsType[10]= Types.VARCHAR;
        paramsObj[11]=cmdDownWarnMsgUrgeTodoReq.getSuperVisorTel();
        paramsType[12]= Types.VARCHAR;
        paramsObj[13]=cmdDownWarnMsgUrgeTodoReq.getSuperVisorEmail();
        paramsType[13]= Types.VARCHAR;
        return executeUpdate(SQL.INSERT_DOWN_WARN_MSG_URGE_TODO_REQ,paramsObj,paramsType);
    }
    /**
     * 报警预警消息 DOWN_WARN_MSG_INFORM_TIPS
     * @param cmdDownWarnMsgInformTips
     * @return
     */
    public int insertDownWarnMsgInformTips(CmdDownWarnMsgInformTips cmdDownWarnMsgInformTips){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        paramsObj[0] ="01";
        paramsType[0]= Types.VARCHAR;
        paramsObj[1] =cmdDownWarnMsgInformTips.getVehicleNo();
        paramsType[1]= Types.VARCHAR;
        paramsObj[2] =cmdDownWarnMsgInformTips.getVehicleColor();
        paramsType[2]= Types.VARCHAR;
        paramsObj[3] =cmdDownWarnMsgInformTips.getSubMsgId();
        paramsType[3]= Types.VARCHAR;
        paramsObj[4] =cmdDownWarnMsgInformTips.getSubDataSize();
        paramsType[4]= Types.INTEGER;
        paramsObj[5] =cmdDownWarnMsgInformTips.getWarnSrc();
        paramsType[5]= Types.VARCHAR;
        paramsObj[6] =cmdDownWarnMsgInformTips.getWarnType();
        paramsType[6]= Types.VARCHAR;
        paramsObj[7] =cmdDownWarnMsgInformTips.getWarnTime();
        paramsType[7]= Types.NUMERIC;
        paramsObj[8] =cmdDownWarnMsgInformTips.getWarnLength();
        paramsType[8]= Types.INTEGER;
        paramsObj[9] =cmdDownWarnMsgInformTips.getWarnContent();
        paramsType[9]= Types.VARCHAR;
        return executeUpdate(SQL.INSERT_DOWN_WARN_MSG_INFORM,paramsObj,paramsType);
    }
    /**
     * 报警预警消息 DOWN_WARN_MSG_EXG_INFORM
     * @param cmdDownWarnMsgExgInform
     * @return
     */
    public int insertDownWarnMsgExgInform(CmdDownWarnMsgExgInform cmdDownWarnMsgExgInform){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        paramsObj[0] ="02";
        paramsType[0]= Types.VARCHAR;
        paramsObj[1] =cmdDownWarnMsgExgInform.getVehicleNo();
        paramsType[1]= Types.VARCHAR;
        paramsObj[2] =cmdDownWarnMsgExgInform.getVehicleColor();
        paramsType[2]= Types.VARCHAR;
        paramsObj[3] =cmdDownWarnMsgExgInform.getSubMsgId();
        paramsType[3]= Types.VARCHAR;
        paramsObj[4] =cmdDownWarnMsgExgInform.getSubDataSize();
        paramsType[4]= Types.INTEGER;
        paramsObj[5] =cmdDownWarnMsgExgInform.getWarnSrc();
        paramsType[5]= Types.VARCHAR;
        paramsObj[6] =cmdDownWarnMsgExgInform.getWarnType();
        paramsType[6]= Types.VARCHAR;
        paramsObj[7] =cmdDownWarnMsgExgInform.getWarnTime();
        paramsType[7]= Types.NUMERIC;
        paramsObj[8] =cmdDownWarnMsgExgInform.getWarnLength();
        paramsType[8]= Types.INTEGER;
        paramsObj[9] =cmdDownWarnMsgExgInform.getWarnContent();
        paramsType[9]= Types.VARCHAR;
        return executeUpdate(SQL.INSERT_DOWN_WARN_MSG_INFORM,paramsObj,paramsType);
    }
    /**
     * 查询需要应答信息的列表
     * @param warnMsgUrgeTodoReqBean
     * @param objClass
     * @param <T>
     * @return
     */
    public  <T> List<T> findWarnMsgUrgeTodoDataMultiple( WarnMsgUrgeTodoReqBean warnMsgUrgeTodoReqBean, Class<T> objClass){
        return executeQueryForMultiple(SQL.SELECT_WARN_MSG_URGE_TODO, new Object[]{warnMsgUrgeTodoReqBean.getReplyTime()}, objClass);
    }
    /**
     * 查询需要应答信息的列表
     * @param warnMsgAdptInfoBean
     * @param objClass
     * @param <T>
     * @return
     */
    public  <T> List<T> findWarnMsgAdptDataMultiple( WarnMsgAdptInfoBean warnMsgAdptInfoBean, Class<T> objClass){
        return executeQueryForMultiple(SQL.SELECT_WARN_MSG_ADPT_INFO, new Object[]{warnMsgAdptInfoBean.getReqType(), warnMsgAdptInfoBean.getReplyTime()}, objClass);
    }
    /**
     * 更新上报预警信息
     * @param warnMsgAdptInfoBean
     * @return
     */
    public int updateWarnMsgAdpt(WarnMsgAdptInfoBean warnMsgAdptInfoBean){
        Object [] paramsObj = new Object[1];
        int [] paramsType = new int[1];
        //更新应答状态
        paramsObj[0]=warnMsgAdptInfoBean.getId();
        paramsType[0]= Types.NUMERIC;
        return executeUpdate(SQL.UPDATE_ARN_MSG_ADPT_INFO,paramsObj,paramsType);
    }/**
     * 更新督办信息
     * @param warnMsgUrgeTodoReqBean
     * @return
     */
    public int updateWarnMsgUrgeTodoData(WarnMsgUrgeTodoReqBean warnMsgUrgeTodoReqBean){
        Object [] paramsObj = new Object[1];
        int [] paramsType = new int[1];
        //更新应答状态
        paramsObj[0]=warnMsgUrgeTodoReqBean.getId();
        paramsType[0]= Types.NUMERIC;
        return executeUpdate(SQL.UPDATE_WARN_MSG_URGE_TODO,paramsObj,paramsType);
    }
    /**********************************************车辆报警信息交互END*****************************************************************/

/**********************************************车辆监管业务START*****************************************************************/
    /**
     * 车辆单向监听请求消息
     * @param cmdDownCtrlMsgMonitorVehicleReq
     * @return
     */
    public int insertDownCtrlMsgMonitorVehicle(CmdDownCtrlMsgMonitorVehicleReq cmdDownCtrlMsgMonitorVehicleReq){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];

        return executeUpdate(SQL.INSERT_DOWN_CTRL_MSG_MONITOR_VEHICLE,paramsObj,paramsType);
    }

    /**
     * 车辆拍照请求消息
     * @param cmdDownCtrlMsgTakePhotoReq
     * @return
     */
    public int insertDownCtrlMsgTakePhoto(CmdDownCtrlMsgTakePhotoReq cmdDownCtrlMsgTakePhotoReq){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        return executeUpdate(SQL.INSERT_DOWN_CTRL_MSG_TAKE_PHOTO,paramsObj,paramsType);
    }

    /**
     * 下发车辆报文请求消息
     * @param cmdDownCtrlMsgTextInfo
     * @return
     */
    public int insertDownCtrlMsgTextInfo(CmdDownCtrlMsgTextInfo cmdDownCtrlMsgTextInfo){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        return executeUpdate(SQL.INSERT_DOWN_CTRL_MSG_TEXT_INFO,paramsObj,paramsType);
    }

    /**
     * 上报车辆行驶记录请求消息
     * @param cmdDownCtrlMsgTakeTravelReq
     * @return
     */
    public int insertDownCtrlMsgTakeTravel(CmdDownCtrlMsgTakeTravelReq cmdDownCtrlMsgTakeTravelReq){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        return executeUpdate(SQL.INSERT_DOWN_CTRL_MSG_TAKE_TRAVEL,paramsObj,paramsType);
    }

    /**
     * 车辆应急接入监管平台请求消息
     * @param cmdDownCtrlMsgEmergencyMonitoringReq
     * @return
     */
    public int insertDownCtrlMsgEmergencyMonitoring(CmdDownCtrlMsgEmergencyMonitoringReq cmdDownCtrlMsgEmergencyMonitoringReq){
        Object [] paramsObj = new Object[13];
        int [] paramsType = new int[13];
        return executeUpdate(SQL.INSERT_DOWN_CTRL_MSG_EMERGENCY_MONITORING,paramsObj,paramsType);
    }




    /**********************************************车辆监管业务类END*****************************************************************/

    private Connection getMasterConnection() throws SQLException {
        return masterPool.getConnection();
    }

    public static void release() {
        KKLog.info("释放数据库连接");
        DBConnPools.releasePools();
    }
// **********************************************************数据库查询、保存操作********************************************************************

    /**
     * 获取个数
     * @param sql
     * @param paramsObj
     * @return
     */
    private  Object executeQueryForObj(String sql, Object[] paramsObj) {
        KKLog.info("executeQueryForCount sql:"+sql+";  sql参数"+ Arrays.toString(paramsObj));
        Object obj = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = getMasterConnection();
            statement = connection.prepareStatement(sql);
            if (paramsObj != null) {
                // 注入参数
                for (int i = 0; i < paramsObj.length; i++) {
                    statement.setObject(i + 1, paramsObj[i]);
                }
            }
            // 执行查询
            rs = statement.executeQuery();
            // 遍历查找的结果集
            while (rs.next()) {
                obj = rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(rs, statement, connection);
        }
        return obj;
    }

    /**
     * 保存操作
     * @param sql
     * @param paramsObj
     * @param paramsType
     * @return
     */
    private int executeUpdate(String sql, Object[] paramsObj, int[] paramsType) {
        KKLog.info("executeUpdateSql sql:"+sql+";  sql参数"+ Arrays.toString(paramsObj));
        int count = -1;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getMasterConnection();
            statement = connection.prepareStatement(sql);
            // 注入参数
            for (int i = 0; i < paramsObj.length; i++) {
                if (paramsType[i] == Types.INTEGER) {// int
                    statement.setInt(i + 1, Integer.valueOf(paramsObj[i].toString()));
                } else if (paramsType[i] == Types.VARCHAR) {// string
                    statement.setString(i + 1, paramsObj[i].toString());
                } else if (paramsType[i] == Types.NUMERIC) {//number、long
                    statement.setLong(i + 1, Long.valueOf(paramsObj[i].toString()));
                } else if (paramsType[i] == Types.DOUBLE) {//DOUBLE
                    statement.setDouble(i + 1, Double.valueOf(paramsObj[i].toString()));
                } else if (paramsType[i] == Types.FLOAT) {//FLOAT
                    statement.setFloat(i + 1, Float.valueOf(paramsObj[i].toString()));
                } else if (paramsType[i] == Types.CLOB) {//CLOB
                    statement.setClob(i + 1, (Clob) paramsObj[i]);
                } else if (paramsType[i] == Types.BLOB) {//BLOB
                    statement.setBlob(i + 1, (Blob) paramsObj[i]);
                } else {
                    KKLog.warn("没有找到" + paramsType[i] + " 类型");
                }
            }
            count = statement.executeUpdate();
        } catch (SQLException e) {
            KKLog.error("saveUpPlatformMsg异常:" + e.getMessage());
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(resultSet, statement, connection);
        }
        return count;
    }

    /**
     * 查询获取单个对象
     * @param sql
     * @param paramsObj
     * @param objClass
     * @param <T>
     * @return
     */
    private   <T> T executeQueryForSingle(String sql, Object[] paramsObj, Class<T> objClass) {
        KKLog.info("executeQueryForSingle sql:"+sql+";  sql参数"+ Arrays.toString(paramsObj));
        T t = null;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = getMasterConnection();
            pstmt = connection.prepareStatement(sql);
            // 注入参数
            if (paramsObj != null) {
                for (int i = 0; i < paramsObj.length; i++) {
                    pstmt.setObject(i + 1, paramsObj[i]);
                }
            }
            // 执行查询
            rs = pstmt.executeQuery();
            // 获取结果集的数据
            while (rs.next()) {
                // 获取类的实例
                t = objClass.newInstance();
                //获取所有的字段名称
                Field [] fields = objClass.getDeclaredFields();
                for (Field field : fields) {
                    //将从数据库中取出来的值类型转换为与javabean中的字段类型一致
                    Object val = convertFieldTypeVal(rs,field);
                    //获取需要set的方法名称
                    String methodName = getSetMethodName(field.getName());
                    //获取方法
                    Method method = objClass.getDeclaredMethod(methodName, field.getType());
                    if(val != null){
                        //赋值
                        method.invoke(t,val);
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(rs, pstmt, connection);
        }
        return t;
    }

    private  <T> List<T> executeQueryForMultiple(String sql,Object[] paramsObj, Class<T> objClass) {
        KKLog.info("executeQueryForMultiple sql:"+sql+";  sql参数"+ Arrays.toString(paramsObj));

        List<T> list = new ArrayList<T>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            connection = getMasterConnection();
            pstmt = connection.prepareStatement(sql);

            if (paramsObj != null) {
                // 注入参数
                for (int i = 0; i < paramsObj.length; i++) {
                    pstmt.setObject(i + 1, paramsObj[i]);
                }
            }
            // 执行查询
            rs = pstmt.executeQuery();
            // 获取结果集的数据
            while (rs.next()) {
                T t = null;
                // 获取类的实例
                t = objClass.newInstance();
                //获取所有的字段名称
                Field [] fields = objClass.getDeclaredFields();
                for (Field field : fields) {
                    //将从数据库中取出来的值类型转换为与javabean中的字段类型一致
                    Object val = convertFieldTypeVal(rs,field);
                    //获取需要set的方法名称
                    String methodName = getSetMethodName(field.getName());
                    //获取方法
                    Method method = objClass.getDeclaredMethod(methodName, field.getType());
                    if(val != null){
                        //赋值
                        method.invoke(t,val);
                    }
                }
                // 向集合中添加数据
                list.add(t);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            KKTool.closeRS_Statement_ConnInSilence(rs, pstmt, connection);
        }
        // 返回集合
        return list;
    }

    private Object convertFieldTypeVal(ResultSet rs,Field field){
        Object val = null;
        String fieldType = field.getType().getName();
        String fieldName = field.getName();
        try {
            if (fieldType.equals(String.class.getName())) {
                val = rs.getString(fieldName);
            } else if (fieldType.equals(Byte.class.getName())){
                val = rs.getByte(fieldName);
            } else if (fieldType.equals(Byte[].class.getName())){
                byte[] bytes = rs.getBytes(fieldName);
                Byte[] objBtye = new Byte[bytes.length];
                for(int i = 0 ; i<bytes.length;i++){
                    objBtye[i]=bytes[i];
                }
                val = objBtye;
            } else if (fieldType.equals(byte.class.getName())){
                val = rs.getByte(fieldName);
            } else if (fieldType.equals(byte[].class.getName())){
                val = rs.getBytes(fieldName);
            } else if (fieldType.equals(Short.class.getName())){
                val = rs.getShort(fieldName);
            } else if (fieldType.equals(short.class.getName())){
                val = rs.getShort(fieldName);
            } else if (fieldType.equals(Integer.class.getName())){
                val = rs.getInt(fieldName);
            } else if (fieldType.equals(int.class.getName())){
                val = rs.getInt(fieldName);
            } else if (fieldType.equals(Long.class.getName())){
                val = rs.getLong(fieldName);
            } else if (fieldType.equals(long.class.getName())){
                val = rs.getLong(fieldName);
            } else if (fieldType.equals(BigDecimal.class.getName())){
                val = rs.getBigDecimal(fieldName);
            } else if (fieldType.equals(Boolean.class.getName())){
                val = rs.getBoolean(fieldName);
            } else if (fieldType.equals(boolean.class.getName())){
                val = rs.getBoolean(fieldName);
            } else if (fieldType.equals(Date.class.getName())){
                val = rs.getTimestamp(fieldName);
            } else if (fieldType.equals(java.sql.Date.class.getName())){
                val = rs.getDate(fieldName);
            } else if (fieldType.equals(Float.class.getName())){
                val = rs.getFloat(fieldName);
            } else if (fieldType.equals(Time.class.getName())){
                val = rs.getTime(fieldName);
            } else if (fieldType.equals(Timestamp.class.getName())){
                val = rs.getTimestamp(fieldName);
            }else{
              KKLog.info(fieldName + " 字段类型匹配失败，NoSuchFie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            KKLog.info(fieldName +"======"+e.getMessage());
        }
        return val;
    }
    /**
     * 获取set方法
     * @param name
     * @return
     */
    private   String getSetMethodName(String name){
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }
// **********************************************************数据库查询、保存操作********************************************************************
}

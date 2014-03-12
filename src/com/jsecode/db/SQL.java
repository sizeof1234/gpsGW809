/**
 * @author 	Jadic
 * @created 2014-2-21
 */
package com.jsecode.db;

public final class SQL {
	
	//车辆信息
	public final static String CAR_INFO = "";
	
//获取数据库服务时间
	public final static String GET_DB_SYS_TIME = "select sysdate from dual";

	//终端信息
	public final static String QUERY_TERMINAL_INFO = " select c.terminalid as id, a.hostno, b.hostno_color as hostPlateColor, " +
													   "        c.terminalsim as simNo, c.producerid, c.terminalmodeltype as modelType " +
													   " from to_superior_map a " +
													   " inner join position_host_info b on b.hostno = a.hostno " +
													   " inner join terminal_info c on c.hostid=b.hostid " +
													   " where c.inmonitor=1 and c.status=1 ";
	
	public final static String QUERY_CURRENT_GPS = " select d.hostid, d.hostno as vehicleNo, b.hostno_color as plateColor, " +
													 "		  d.pos_time as gpsTime, d.pos_long as lon, d.pos_lat as lat, " +
													 "		  d.pos_speed as speed, d.pos_angle as direction, " +
													 "		  d.status_updatetime as dbSysTime " +
													 " from to_superior_map a " +
													 " inner join position_host_info b on b.hostno = a.hostno " +
													 " inner join terminal_info c on c.hostid=b.hostid " +
													 " inner join host_curstatus d on d.hostid=c.hostid " +
													 " where c.inmonitor=1 " +
													 "	 and c.status=1 " +
													 "	 and d.status_updatetime > ? ";
	
	public final static String CAR_MAX_ROWSCN = "select max(ora_rowscn) as maxr from tab_carinfo";


    /******************************************************平台间信息交互*************************************************************/
    //保存上级平台 的平台查岗请求、下发平台间报文请求 信息
    public final static String INSERT_DOWN_PLATFORM_MSG = "                                                \n" +
            "   INSERT INTO DOWN_PLATFORM_MSG                \n" +
            "     (ID,                                       \n" +
            "      REQ_TYPE,                                 \n" +
            "      DATA_TYPE,                                \n" +
            "      OBJECT_LENGTH,                            \n" +
            "      OBJECT_TYPE,                              \n" +
            "      OBJECT_ID,                                \n" +
            "      INFO_ID,                                  \n" +
            "      INFO_LENGTH,                              \n" +
            "      INFO_CONTENT,                             \n" +
            "      REQ_TIME,                                 \n" +
            "      REP_STATUS)                               \n" +
            "   VALUES                                       \n" +
            "     (SEQ_DOWN_PLATFORM_MSG.NEXTVAL,            \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      ?,                                        \n" +
            "      SYSDATE,                                  \n" +
            "      '01')                                     \n";
    //查询需要应答的消息列表
    public final static String SELECT_PLATFORM_MSG ="                                                             \n" +
            "SELECT ID ID,                                                \n "+
            "       REQ_TYPE REQTYPE,                                     \n "+
            "       DATA_TYPE DATATYPE,                                   \n "+
            "       OBJECT_LENGTH OBJECTLENGTH,                           \n "+
            "       OBJECT_TYPE OBJECTTYPE,                               \n "+
            "       OBJECT_ID OBJECTID,                                   \n "+
            "       INFO_ID INFOID,                                       \n "+
            "       INFO_LENGTH INFOLENGTH,                               \n "+
            "       INFO_CONTENT INFOCONTENT,                             \n "+
            "       TO_CHAR(REQ_TIME,'yyyy-mm-dd hh24:mi:ss') REQTIME,    \n "+
            "       TO_CHAR(REP_TIME,'yyyy-mm-dd hh24:mi:ss') REPTIME,    \n "+
            "       TO_CHAR(REPLY_TIME,'yyyy-mm-dd hh24:mi:ss') REPLYTIME,\n "+
            "       REP_MSG REPMSG,                                       \n "+
            "       REP_STATUS REPSTATUS                                  \n "+
            "  FROM DOWN_PLATFORM_MSG                                     \n "+
            "       WHERE REP_STATUS=?                                    \n "+
            "           AND　REPLY_TIME>TO_DATE(?,'yyyy-mm-dd hh24:mi:ss')\n "+
            "       ORDER BY REPLY_TIME DESC                              \n ";


    //发送完成后，更新状态和发送时间
    public final static String UPDATE_PLATFORM_MSG = "                                                            \n" +
            "UPDATE DOWN_PLATFORM_MSG                                     \n" +
            "   SET REP_STATUS = '03', REP_TIME = SYSDATE                 \n" +
            " WHERE ID = ?                                                 \n";
    //修改应答状态 为待应答
    public final static String SELECT_SYSDATE_STR = " SELECT TO_CHAR(SYSDATE-1,'yyyy-mm-dd hh24:mi:ss') FROM DUAL ";
/******************************************************平台间信息交互*************************************************************/


    /******************************************************车辆报警信息交互*************************************************************/

    public final static String INSERT_DOWN_WARN_MSG_URGE_TODO_REQ = "                                       \n" +
            "INSERT INTO DOWN_WARN_MSG_URGE_TODO_REQ\n" +
            "  (ID,                                 \n" +
            "   VEHICLE_NO,                         \n" +
            "   VEHICLE_COLOR,                      \n" +
            "   DATA_TYPE,                          \n" +
            "   DATA_LENGTH,                        \n" +
            "   WARN_SRC,                           \n" +
            "   WARN_TYPE,                          \n" +
            "   WARN_TIME,                          \n" +
            "   SUPERVISION_ID,                     \n" +
            "   SUPERVISION_ENDTIME,                \n" +
            "   SUPERVISION_LEVEL,                  \n" +
            "   SUPERVISOR,                         \n" +
            "   SUPERVISION_TEL,                    \n" +
            "   SUPERVISION_EMAL,                   \n" +
            "   REQ_TIME,                           \n" +
            "   REPLY_TIME,                         \n" +
            "   REPLY_CONTENT,                      \n" +
            "   REP_TIME,                           \n" +
            "   REP_STATUS)                         \n" +
            "VALUES                                 \n" +
            "  (SEQ_DOWN_WARN_MSG_URGE_TODO.NEXTVAL,\n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?,                                  \n" +
            "   ?)                                  ";

    public final static String INSERT_DOWN_WARN_MSG_INFORM ="                                               \n" +
            "INSERT INTO DOWN_WARN_MSG_INFORM               \n" +
            "  (ID,                                         \n" +
            "   REQ_TYPE,                                   \n" +
            "   VEHICLE_NO,                                 \n" +
            "   VEHICLE_COLOR,                              \n" +
            "   DATA_TYPE,                                  \n" +
            "   DATA_LENGTH,                                \n" +
            "   WARN_SRC,                                   \n" +
            "   WARN_TYPE,                                  \n" +
            "   WARN_TIME,                                  \n" +
            "   WARN_LENGTH,                                \n" +
            "   WARN_CONTENT,                               \n" +
            "   REQ_TIME,                                   \n" +
            "   REPLY_TIME,                                 \n" +
            "   REPLY_CONTENT,                              \n" +
            "   REP_TIME,                                   \n" +
            "   REP_STATUS)                                 \n" +
            "VALUES                                         \n" +
            "  (SEQ_DOWN_WARN_MSG_INFORM.NEXTVAL,           \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?,                                          \n" +
            "   ?)                                          \n";

    //查询需要报警督办请求应答列表
    public final static String SELECT_WARN_MSG_URGE_TODO ="";

    //发送完成后，更新状态和发送时间
    public final static String UPDATE_WARN_MSG_URGE_TODO = "";

    //查询需要上报报警信息消息列表
    public final static String SELECT_WARN_MSG_ADPT_INFO ="";

    //发送完成后，更新状态和发送时间
    public final static String UPDATE_ARN_MSG_ADPT_INFO = "";
    /******************************************************车辆报警信息交互*************************************************************/

    /**********************************************车辆监管业务START*****************************************************************/
    //车辆单向监听请求消
    public final static String INSERT_DOWN_CTRL_MSG_MONITOR_VEHICLE="";
    //车辆拍照请求消息
    public final static String INSERT_DOWN_CTRL_MSG_TAKE_PHOTO="";
    //下发车辆报文请求消息
    public final static String INSERT_DOWN_CTRL_MSG_TEXT_INFO="";
    //上报车辆行驶记录请求消息
    public final static String INSERT_DOWN_CTRL_MSG_TAKE_TRAVEL="";
    //车辆应急接入监管平台请求消息
    public final static String INSERT_DOWN_CTRL_MSG_EMERGENCY_MONITORING="";





    /**********************************************车辆监管业务类END*****************************************************************/

    private SQL() {
    }
}

package com.jsecode.utils;

public final class Const {
	
	public final static String EMPTY_STR = "";
	public final static String STR_UNKNOWN = "未知";
	
	/*头标识(1) + 数据头(22) + 数据体(0) + CRC校验码(2) + 尾标识(1)*/
	public final static int CMD_MIN_SIZE = 26;//命令最小长度
	
	public final static byte CMD_HEAD = 0x5B;//809消息命令头
	public final static byte CMD_END  = 0x5D;//809消息命令尾
	
	public final static long RECONNECT_INTERVAL_SECONDS = 5;			//重连间隔,单位:秒
	public final static long HEARTBEAT_INTERVAL_SECONDS = 20;			//心跳间隔,单位:秒
	public final static long OVER_TIME_MILLISECONDS     = 60 * 1000;	//超时时间,单位:毫秒	
	
	public final static char BLANK_CHAR = 0x00;//空字符 809协议中需要填充的空字符

	//业务数据类型标识
	public final static short UP_CONNECT_REQ 								=		 0x1001;//主链路登录请求消息	主链路
	public final static short UP_CONNECT_RSP								=		 0x1002;//主链路登录应答消息	主链路
	public final static short UP_DISCONNECE_REQ 							=		 0x1003;//主链路注销请求消息	主链路
	public final static short UP_DISCONNECT_RSP 							=		 0x1004;//主链路注销应答消息	主链路
	public final static short UP_LINKETEST_REQ 							=		 0x1005;//主链路连接保持请求消息	主链路
	public final static short UP_LINKTEST_RSP								=		 0x1006;//主链路连接保持应答消息	主链路
	public final static short UP_DISCONNECT_INFORM						=		 0x1007;//主链路断开通知消息	从链路
	public final static short UP_CLOSELINK_INFORM							=		 0x1008;//下级平台主动关闭链路通知消息	从链路
	public final static short DOWN_CONNECT_REQ 							= (short)0x9001;//从链路连接请求消息	从链路
	public final static short DOWN_CONNECT_RSP							= (short)0x9002;//从链路连接应答消息	从链路
	public final static short DOWN_DISCONNECT_REQ 						= (short)0x9003;//从链路注销请求消息	从链路
	public final static short DOWN_DISCONNECT_RSP							= (short)0x9004;//从链路注销应答消息	从链路
	public final static short DOWN_LINKTEST_REQ 							= (short)0x9005;//从链路连接保持请求消息	从链路
	public final static short DOWN_LINKTEST_RSP							= (short)0x9006;//从链路连接保持应答消息	从链路
	public final static short DOWN_DISCONNECT_INFORM 						= (short)0x9007;//从链路断开通知消息	从链路
	public final static short DOWN_CLOSELINK_INFORM 						= (short)0x9008;//上级平台主动关闭链路通知消息	主链路
	public final static short DOWN_TOTAL_RECV_BACK_MSG 					= (short)0x9101;//接收定位信息数量通知消息	从链路
	public final static short UP_EXG_MSG									=		 0x1200;//主链路动态信息交换消息	主链路
	public final static short DOWN_EXG_MSG 								= (short)0x9200;//从链路动态信息交换消息	从链路
	public final static short UP_PLAFORM_MSG 								=		 0x1300;//主链路平台间信息交互消息	主链路
	public final static short DOWN_PLATFORM_MSG	 						= (short)0x9300;//从链路平台间信息交互消息	从链路
	public final static short UP_WARN_MSG 								=		 0x1400;//主链路报警信息交互消息	主链路
	public final static short DOWN_WARN_MSG	 							= (short)0x9400;//从链路报警信息交互消息	从链路
	public final static short UP_CTRL_MSG	 								=		 0x1500;//主链路车辆监管消息	主链路
	public final static short DOWN_CTRL_MSG	 							= (short)0x9500;//从链路车辆监管消息	从链路
	public final static short UP_BASE_MSG	 								=		 0x1600;//主链路静态信息交换消息	主链路
	public final static short DOWN_BASE_MSG	 							= (short)0x9600;//从链路静态信息交换消息	从链路
	
	//子业务类型标识
	public final static short UP_EXG_MSG_REGISTER 						= 		 0x1201;//上传车辆注册信息
	public final static short UP_EXG_MSG_REAL_LOCATION 					= 		 0x1202;//事实上传车辆定位信息
	public final static short UP_EXG_MSG_HISTORY_LOCATION 				= 		 0x1203;//车辆定位信息自动补报
	public final static short UP_EXG_MSG_RETURN_STARTUP_ACK 			 	= 		 0x1205;//启动车辆定位信息交换应答
	public final static short UP_EXG_MSG_RETURN_END_ACK 				 	=		 0x1206;//结束车辆定位信息交换应答
	public final static short UP_EXG_MSG_APPLY_FOR_MONITOR_STARTUP 		= 		 0x1207;//申请交换指定车辆定位信息请求
	public final static short UP_EXG_MSG_APPLY_FOR_MONITOR_END 			= 		 0x1208;//取消交换指定车辆定位信息请求
	public final static short UP_EXG_MSG_APPLY_HISGNSSDATA_REQ 			= 		 0x1209;//补发车辆定位信息请求
	public final static short UP_EXG_MSG_REPORT_DRIVER_INFO_ACK 		 	= 		 0x120A;//上报车辆驾驶员身份识别信息应答
	public final static short UP_EXG_MSG_TAKE_EWAYBILL_ACK 				= 		  0x120B;//上报车辆电子运单应答
	public final static short UP_EXG_MSG_REPORT_DRIVER_INFO 			 	= 		  0x120C;//主动上报驾驶员身份信息
	public final static short UP_EXG_MSG_REPORT_EWAYBILL_INFO 			=         0x120D;//主动上报车辆电子运单信息
	public final static short DOWN_EXG_MSG_CAR_LOCATION 				 	= (short)0x9202;//交换车辆定位信息
	public final static short DOWN_EXG_MSG_HISTORY_ARCOSSAREA 			= (short)0x9203;//车辆定位信息交换补发
	public final static short DOWN_EXG_MSG_CAR_INFO 					 	= (short)0x9204;//交换车辆静态信息
	public final static short DOWN_EXG_MSG_RETURN_STARTUP 				= (short)0x9205;//启动车辆定位信息交换请求
	public final static short DOWN_EXG_MSG_RETURN_END 					= (short)0x9206;//结束车辆定位信息交换请求
	public final static short DOWN_EXG_MSG_APPLY_FOR_MONITOR_STARTUP_ACK 	= (short)0x9207;//申请交换指定车辆定位应答
	public final static short DOWN_EXG_MSG_APPLY_FOR_MONITOR_END_ACK 	 	= (short)0x9208;//取消交换指定车辆定位应答
	public final static short DOWN_EXG_MSG_APPLY_HISGNSSDATA_ACK 		 	= (short)0x9209;//补发车辆定位信息应答
	public final static short DOWN_EXG_MSG_REPORT_DRIVER_INFO 			= (short)0x920A;//上报车辆驾驶员身份识别信息请求
	public final static short DOWN_EXG_MSG_TAKE_EWAYBILL_REQ 			 	= (short)0x920B;//上报车辆电子运单请求
	public final static short UP_PLATFORM_MSG_POST_QUERY_ACK 			 	=		  0x1301;//平台查岗应答
	public final static short UP_PLATFORM_MSG_INFO_ACK 					=   	  0x1302;//下发平台间报文应答
	public final static short DOWN_PLATFORM_MSG_POST_QUERY_REQ 			= (short)0x9301;//平台查岗应答
	public final static short DOWN_PLATFORM_MSG_INFO_REQ 				 	= (short)0x9302;//下发平台间报文应答
	public final static short UP_WARN_MSG_URGE_TODO_ACK 				 	= 		 0x1401;//报警督办应答
	public final static short UP_WARN_MSG_ADPT_INFO 					 	= 		 0x1402;//上报报警信息
	public final static short UP_WARN_MSG_ADPT_TODO_INFO				 	= 		 0x1403;//主动上报报警处理结果信息
	public final static short DOWN_WARN_MSG_URGE_TODO_REQ 				= (short)0x9401;//报警督办请求
	public final static short DOWN_WARN_MSG_INFORM_TIPS 				 	= (short)0x9402;//报警预警
	public final static short DOWN_WARN_MSG_EXG_INFORM 					= (short)0x9403;//实时交换报警消息
	public final static short UP_CTRL_MSG_MONITOR_VEHICLE_ACK 			= 		 0x1501;//车辆单向监听应答
	public final static short UP_CTRL_MSG_TAKE_PHOTO_ACK 				 	= 		 0x1502;//车辆拍照应答
	public final static short UP_CTRL_MSG_TEXT_INFO_ACK 				 	= 		 0x1503;//下发车辆行驶记录应答
	public final static short UP_CTRL_MSG__TAKE_TRAVEL_ACK 				= 		 0x1504;//上报车辆行驶记录应答
	public final static short UP_CTRL_MSG_EMERGENCY_MONITORING_ACK 		= 		 0x1505;//车辆应急接入监管平台应答消息
	public final static short DOWN_CTRL_MSG_MONITOR_VEHICLE_REQ 		 	= (short)0x9501;//车辆单向监听请求
	public final static short DOWN_CTRL_MSG_TAKE_PHOTO_REQ 				= (short)0x9502;//车辆拍照请求
	public final static short DOWN_CTRL_MSG_TEXT_INFO   				 	= (short)0x9503;//下发车辆报文请求
	public final static short DOWN_CTRL_MSG_TAKE_TRAVEL_REQ 			 	= (short)0x9504;//上报车辆行驶记录应答
	public final static short DOWN_CTRL_MSG_EMERGENCY_MONITORING_REQ 	 	= (short)0x9505;//车辆应急接入监管平台请求消息
	public final static short UP_BASE_MSG_VEHICLE_ADDED_ACK 			 	= 		 0x1601;//补报车辆静态信息应答
	public final static short DOWN_BASE_MSG_VEHICLE_ADDED 				= (short)0x9601;//补报车辆静态信息请求
	
	//********************************************telnet 服务相关约定常量*********************************************/
	public final static String TELNET_CMD_QUIT 				= "quit";		//退出
	public final static String TELNET_CMD_HELP				= "help";		//列出支持的命令
	public final static String TELNET_CMD_START_MAINLINK 		= "start main";	//启动主链路
	public final static String TELNET_CMD_END_MAINLINK 		= "end main";	//关闭主链路
	public final static String TELNET_CMD_START_SUBLINK 		= "start sub";	//启动从链路
	public final static String TELNET_CMD_END_SUBLINK 		= "end sub";	//关闭从链路
	//********************************************telnet 服务相关约定常量*********************************************/
	
	//*********************************************登录应答相关常量**********************************************/
    public final static byte RET_OK 					= 0x00;
    public final static byte RET_IP_ERR 				= 0x01;
    public final static byte RET_CENTERID_ERR 		= 0x02;
    public final static byte RET_USER_UNREGISTERED 	= 0x03;
    public final static byte RET_USER_PASS_ERR 		= 0x04;
    public final static byte RET_RES_LIMIT 			= 0x05;
    public final static byte RET_OTHER 				= 0x06;
    
    public final static String SRET_OK 				= "成功";
    public final static String SRET_IP_ERR 			= "IP地址不正确";
    public final static String SRET_CENTERID_ERR 		= "接入码不正确";
    public final static String SRET_USER_UNREGISTERED = "用户未注册";
    public final static String SRET_USER_PASS_ERR 	= "密码错误";
    public final static String SRET_RES_LIMIT			= "资源紧张";
    //*********************************************登录应答相关常量**********************************************/
	
	
	//主链路断开通知 错误代码
	public final static byte UP_DISCONNECT_INFORM_ERR_DISCONNECT = 0x00;//主动断开
	public final static byte UP_DISCONNECT_INFORM_ERR_OTHER      = 0x01;//其他原因
	
	//主链路主动关闭链路通知 关闭原因代码
	public final static byte UP_CLOSE_LINK_REASON_REBOOT = 0x00;//网关重启
	public final static byte UP_CLOSE_LINK_REASON_OTHER  = 0x01;//其他原因
	
	//从链路连接应答 验证结果代码
	public final static byte DOWN_CONNECT_RESP_RET_OK              = 0x00;//成功
	public final static byte DOWN_CONNECT_RESP_RET_VERIFY_CODE_ERR = 0x01;//校验码错误
	public final static byte DOWN_CONNECT_RESP_RET_RES_LIMIT       = 0x02;//资源紧张
	public final static byte DOWN_CONNECT_RESP_RET_OTHER           = 0x03;//其他
	
	//从链路断开通知 错误代码
	public final static byte DOWN_DISCONNECT_INFORM_ERR_CONNECT_REFUSE = 0x00;//无法链接下级平台指定服务的IP和端口
	public final static byte DOWN_DISCONNECT_INFORM_ERR_DISCONNECT     = 0x01;//上级平台客户端与下级平台服务断开	
	public final static byte DOWN_DISCONNECT_INFORM_ERR_OTHER          = 0x02;//上级平台客户端与下级平台服务断开	

	//从链路主动关闭链路通知 关闭原因代码
	public final static byte DOWN_CLOSE_LINK_REASON_REBOOT = 0x00;//网关重启
	public final static byte DOWN_CLOSE_LINK_REASON_OTHER  = 0x01;//其他原因
}

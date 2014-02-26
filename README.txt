本项目为809网关

一、代码相关约定
1.主链路请求命令包 com.jsecode.cmd.up.req	
2.主链路应答命令包 com.jsecode.cmd.up.resp
3.从链路请求命令包 com.jsecode.cmd.down.req
4.从链路应答命令包 com.jsecode.cmd.down.resp

每个命令对应的类名为Cmd****(***参照809文档中定义业务类型名)
示例：
  主链路登录请求类名CmdUpConnectReq
  主链路登录请求类名CmdUpConnectResp
  从链路登录请求类名CmdDownConnectReq
  从链路登录请求类名CmdDownConnectResp
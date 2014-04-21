package com.jsecode.biz;

import java.util.Date;
import java.util.List;

import com.jsecode.bean.TerminalCmdBean;
import com.jsecode.db.DBOper;

/**
 * 加载命令列表处理情况
 * @author 	Jadic
 * @created 2014-4-1
 */
public class ThreadGetCmdFromDB implements Runnable {
	
	private IAddDataToQueue<TerminalCmdBean> iAddData;
	private Date dbLastSysTime;
	private DBOper dbOper;

	public ThreadGetCmdFromDB(IAddDataToQueue<TerminalCmdBean> iAddData) {
		this.iAddData = iAddData;
		dbOper = DBOper.getDBOper();
		dbLastSysTime = dbOper.getDBsysTime();
	}

	public void run() {
		loadCmd();
	}
	
	private void loadCmd() {
		List<TerminalCmdBean> cmdList = dbOper.getTerminalCmdList(dbLastSysTime);
		if (cmdList != null) {
			for (TerminalCmdBean cmd : cmdList) {
				if (cmd.getProcessTime().after(dbLastSysTime)) {
					dbLastSysTime = cmd.getProcessTime();
				}
				this.iAddData.addSingleToQueue(cmd);
			}
		}
	}

}

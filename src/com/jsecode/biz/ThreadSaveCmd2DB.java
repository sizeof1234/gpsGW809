package com.jsecode.biz;

import com.jsecode.cmd.CmdHead;
import com.jsecode.db.DBOper;

/**
 * @author 	Jadic
 * @created 2014-3-31
 */
public class ThreadSaveCmd2DB extends AbstractThreadDisposeDataFromQueue<CmdHead> {

	private DBOper dbOper;
	
	public ThreadSaveCmd2DB() {
		super(ThreadSaveCmd2DB.class.getName());
		dbOper = DBOper.getDBOper();
	}
			
	@Override
	public void run() {
		CmdHead cmd = null;
		while (!isInterrupted()) {
			while ((cmd = getQueuePollData()) != null) {
				dbOper.saveCmdInfoToDB(cmd);
			}
			waitNewData();
		}
	}

}

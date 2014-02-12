package edu.nju.ics.miss.smartcar.action;

import edu.nju.ics.miss.bottom.util.MyLogger;
import edu.nju.ics.miss.framework.Action;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-30 下午1:06:31<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class ExitAction extends Action {

	public ExitAction(String actionName) {
		super(actionName);
	}

	@Override
	public void execute() {
		System.exit(0);
		MyLogger.writeLog(MyLogger.DEBUG, "任务结束，系统退出");
	}

}

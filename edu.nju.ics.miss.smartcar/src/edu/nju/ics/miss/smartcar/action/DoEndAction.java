package edu.nju.ics.miss.smartcar.action;

import edu.nju.ics.miss.bottom.net.NICNetException;
import edu.nju.ics.miss.bottom.net.NICSocket;
import edu.nju.ics.miss.bottom.util.MyLogger;
import edu.nju.ics.miss.framework.Action;
import edu.nju.ics.miss.smartcar.SmartCarAContextManager;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-31 下午4:53:06<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class DoEndAction extends Action {

	public DoEndAction(String actionName) {
		super(actionName);
	}

	@Override
	public void execute() {
		NICSocket socket = SmartCarAContextManager.getNicSocket();
		if (socket != null) {
			try {
				socket.writeLine("Finish:"
						+ SmartCarAContextManager.currentTask);

			} catch (NICNetException e) {
				e.printStackTrace();
				MyLogger.writeLog(MyLogger.ERROR, "与服务器通信失败：" + e.getMessage());
			}
		}
	}

}

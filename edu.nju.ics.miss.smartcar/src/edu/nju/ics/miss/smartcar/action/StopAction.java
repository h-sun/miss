package edu.nju.ics.miss.smartcar.action;

import edu.nju.ics.miss.bottom.MISSBottom;
import edu.nju.ics.miss.bottom.RequestMessage;
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
public class StopAction extends Action {

	public StopAction(String actionName) {
		super(actionName);
	}

	@Override
	public void execute() {
		RequestMessage request = new RequestMessage();
		request.setActionType(RequestMessage.ACTION_STOP);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
		}
		
	}

}

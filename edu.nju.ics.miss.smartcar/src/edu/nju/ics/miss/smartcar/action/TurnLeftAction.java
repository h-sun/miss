package edu.nju.ics.miss.smartcar.action;

import edu.nju.ics.miss.bottom.MISSBottom;
import edu.nju.ics.miss.bottom.RequestMessage;
import edu.nju.ics.miss.framework.Action;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-30 下午2:29:55<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class TurnLeftAction extends Action {

	private int reference;

	public TurnLeftAction(String actionName) {
		super(actionName);
	}

	public void setReference(int reference) {
		this.reference = reference;
	}

	@Override
	public void execute() {
		RequestMessage request = new RequestMessage();
		request.setActionType(RequestMessage.ACTION_TURN_LEFT);
		request.setAdjustReference(reference);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
		}
	}

}

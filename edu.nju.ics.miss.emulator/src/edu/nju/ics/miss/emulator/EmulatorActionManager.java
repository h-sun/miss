package edu.nju.ics.miss.emulator;

import edu.nju.ics.miss.framework.Action;
import edu.nju.ics.miss.framework.ActionManager;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-29 上午9:22:33<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class EmulatorActionManager extends ActionManager {

	@Override
	public Action generateAction(String arg0, String arg1) {
		return new Action(arg0) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see edu.nju.ics.miss.framework.Action#execute()
			 */
			@Override
			public void execute() {
				System.out.println("execute  " + getActionName());
			}
		};
	}

}

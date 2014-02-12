package edu.nju.ics.miss.framework;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午2:57:51<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public abstract class Action {

	private String actionName;

	public Action(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public abstract void execute();

}

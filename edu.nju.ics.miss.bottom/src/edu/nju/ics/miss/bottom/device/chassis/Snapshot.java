package edu.nju.ics.miss.bottom.device.chassis;

/**
 * Title: [快照信息]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-6-26 下午1:32:31<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
class Snapshot {

	/**
	 * 记录当前动作
	 */
	private int actionType;

	/**
	 * 动作参数
	 */
	private int actionParam;

	/**
	 * 动作已完成的数据
	 */
	private int completedParam;

	/**
	 * @return the actionType
	 */
	public int getActionType() {
		return actionType;
	}

	/**
	 * @return the actionParam
	 */
	public int getActionParam() {
		return actionParam;
	}

	/**
	 * @return the completedParam
	 */
	public int getCompletedParam() {
		return completedParam;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	/**
	 * @param actionParam
	 *            the actionParam to set
	 */
	public void setActionParam(int actionParam) {
		this.actionParam = actionParam;
	}

	/**
	 * @param completedParam
	 *            the completedParam to set
	 */
	public void setCompletedParam(int completedParam) {
		this.completedParam = completedParam;
	}

}

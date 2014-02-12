package edu.nju.ics.miss.bottom;

import edu.nju.ics.miss.bottom.datatype.ActionInfo;

/**
 * 请求信息
 * 
 * @author h-sun
 * 
 */
public class RequestMessage {

	public static final int ACTION_NO = ActionInfo.ACTION_NO;

	public static final int ACTION_FORWARD = ActionInfo.ACTION_FORWARD;

	public static final int ACTION_BACKWARD = ActionInfo.ACTION_BACKWARD;

	public static final int ACTION_TURN_LEFT = ActionInfo.ACTION_TURN_LEFT;

	public static final int ACTION_TURN_RIGHT = ActionInfo.ACTION_TURN_RIGHT;

	public static final int ACTION_STOP = ActionInfo.ACTION_STOP;

	public static final int REFERENCE_NO = ActionInfo.REFERENCE_NO;

	public static final int REFERENCE_FRONT = ActionInfo.REFERENCE_FRONT;

	public static final int REFERENCE_BACK = ActionInfo.REFERENCE_BACK;

	public static final int REFERENCE_LEFT = ActionInfo.REFERENCE_LEFT;

	public static final int REFERENCE_RIGHT = ActionInfo.REFERENCE_RIGHT;

	private int param;

	private int adjustReference = REFERENCE_NO;

	private int procReference = REFERENCE_NO;

	private int powerLevel = 70;

	/**
	 * 动作类型
	 */
	private int actionType;

	/**
	 * @return the adjustReference
	 */
	public int getAdjustReference() {
		return adjustReference;
	}

	/**
	 * @param adjustReference
	 *            the adjustReference to set
	 */
	public void setAdjustReference(int adjustReference) {
		this.adjustReference = adjustReference;
	}

	/**
	 * @return the procReference
	 */
	public int getProcReference() {
		return procReference;
	}

	/**
	 * @param procReference
	 *            the procReference to set
	 */
	public void setProcReference(int procReference) {
		this.procReference = procReference;
	}

	/**
	 * @return the powerLevel
	 */
	public int getPowerLevel() {
		return powerLevel;
	}

	/**
	 * @param powerLevel
	 *            the powerLevel to set
	 */
	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
	}

	/**
	 * @return the param
	 */
	public int getParam() {
		return param;
	}

	/**
	 * @param arg
	 *            the param to set
	 */
	public void setParam(int param) {
		this.param = param;
	}

	/**
	 * 
	 * @return 动作类型
	 */
	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

}

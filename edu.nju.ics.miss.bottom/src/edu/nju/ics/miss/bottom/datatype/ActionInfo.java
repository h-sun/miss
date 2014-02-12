package edu.nju.ics.miss.bottom.datatype;

/**
 * 
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2012-12-8 下午10:21:16<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class ActionInfo {

	/**
	 * 当前无动作
	 */
	public static final int ACTION_NO = 0;

	/**
	 * 前进
	 */
	public static final int ACTION_FORWARD = 1;

	public static final int ACTION_BACKWARD = 2;

	public static final int ACTION_TURN_LEFT = 3;

	public static final int ACTION_TURN_RIGHT = 4;

	public static final int ACTION_STOP = 5;

	public static final int REFERENCE_NO = 0;

	public static final int REFERENCE_FRONT = 1;

	public static final int REFERENCE_BACK = 2;

	public static final int REFERENCE_LEFT = 3;

	public static final int REFERENCE_RIGHT = 4;

	/**
	 * 动作类型
	 */
	private int actionType;

	private int end;

	private int adjustReference;

	private int referenceDistance = 0;

	private int powerLevel;

	/**
	 * 服务质量等级
	 */
	private int qualityAssurance;

	/**
	 * @see edu.nju.ics.miss.bottom.datatype.ActionInfo#qualityAssurance
	 * @return the qualityAssurance
	 */
	public int getQualityAssurance() {
		return qualityAssurance;
	}

	/**
	 * 设置软件质量参数
	 * 
	 * @see edu.nju.ics.miss.bottom.datatype.ActionInfo#qualityAssurance
	 * @param qualityAssurance
	 *            the qualityAssurance to set
	 */
	public void setQualityAssurance(int qualityAssurance) {
		this.qualityAssurance = qualityAssurance;
	}

	/**
	 * 获得调整参照物，即左右参照物
	 * 
	 * @return the adjustReference
	 */
	public int getAdjustReference() {
		return adjustReference;
	}

	/**
	 * 设置调整参照物，即左右参照物
	 * 
	 * @param adjustReference
	 *            the adjustReference to set
	 */
	public void setAdjustReference(int adjustReference) {
		this.adjustReference = adjustReference;
	}

	public int getReferenceDistance() {
		return referenceDistance;
	}

	public void setReferenceDistance(int referenceDistance) {
		this.referenceDistance = referenceDistance;
	}

	/**
	 * 获得功率
	 * 
	 * @return the powerLevel
	 */
	public int getPowerLevel() {
		return powerLevel;
	}

	/**
	 * 设置功率
	 * 
	 * @param powerLevel
	 *            the powerLevel to set
	 */
	public void setPowerLevel(int powerLevel) {
		this.powerLevel = powerLevel;
	}

	/**
	 * 获得动作类型
	 * 
	 * @return 动作类型
	 */
	public int getActionType() {
		return actionType;
	}

	/**
	 * 设置动作类型
	 * 
	 * @param actionType
	 */
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ActionType=").append(actionType);
		buffer.append("\nQualityAssurance=").append(qualityAssurance);
		buffer.append("\nAdjustReference=").append(adjustReference);
		buffer.append("\nReferenceDistance=").append(referenceDistance);
		buffer.append("\nEnd=").append(end);
		buffer.append("\nPowerLevel=").append(powerLevel);

		return buffer.toString();
	}

}

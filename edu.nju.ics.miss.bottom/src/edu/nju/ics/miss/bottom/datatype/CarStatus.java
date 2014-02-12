package edu.nju.ics.miss.bottom.datatype;

public class CarStatus {

	private UltrasonicDatas ultrasonicDatas = new UltrasonicDatas();

	/** 当前动作 */
	private int currentAction;

	/** 当前动作已完成的距离 */
	private int completedDistance = 0;

	/**
	 * @see lejos.nic.datatype.CarStatus#ultrasonicDatas
	 * 
	 * @return the ultrasonicDatas
	 */
	public UltrasonicDatas getUltrasonicDatas() {
		return ultrasonicDatas;
	}

	/**
	 * @see lejos.nic.datatype.CarStatus#currentAction
	 * 
	 * @return the currentAction
	 */
	public int getCurrentAction() {
		return currentAction;
	}

	/**
	 * @see lejos.nic.datatype.CarStatus#ultrasonicDatas
	 * 
	 * @param ultrasonicDatas
	 *            the ultrasonicDatas to set
	 */
	public void setUltrasonicDatas(UltrasonicDatas ultrasonicDatas) {
		this.ultrasonicDatas = ultrasonicDatas;
	}

	/**
	 * @see lejos.nic.datatype.CarStatus#currentAction
	 * 
	 * @param currentAction
	 *            the currentAction to set
	 */
	public void setCurrentAction(int currentAction) {
		this.currentAction = currentAction;
	}

	/**
	 * @see lejos.nic.datatype.CarStatus#completedDistance
	 * 
	 * @return the completedDistance
	 */
	public int getCompletedDistance() {
		return completedDistance;
	}

	/**
	 * @see lejos.nic.datatype.CarStatus#completedDistance
	 * 
	 * @param completedDistance
	 *            the completedDistance to set
	 */
	public void setCompletedDistance(int completedDistance) {
		this.completedDistance = completedDistance;
	}

}

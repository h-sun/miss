package edu.nju.ics.miss.bottom;

import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;

/**
 * 响应信息
 * 
 * @author h-sun
 * 
 */
public class RespondMessage {

	public static final int RESPOND_NO_ACTION = 0x1;

	/** 当前正在执行请求动作 */
	public static final int RESPOND_ACTION_EXECUTING = 0x2;

	public static final int RESPOND_ACTION_FINISHED = 0x03;

	/** 请求动作结果 */
	private int actionResult;

	private int walkedDistance = -1;

	private UltrasonicDatas ud;

	/**
	 * 
	 * @return 请求动作结果
	 */
	public int getActionResult() {
		return actionResult;
	}

	public void setActionResult(int actionResult) {
		this.actionResult = actionResult;
	}

	/**
	 * @return the walkedDistance
	 */
	public int getWalkedDistance() {
		return walkedDistance;
	}

	/**
	 * @param walkedDistance
	 *            the walkedDistance to set
	 */
	public void setWalkedDistance(int walkedDistance) {
		this.walkedDistance = walkedDistance;
	}

	/**
	 * @return the UltrasonicDatas
	 */
	public UltrasonicDatas getUltrasonicDatas() {
		return ud;
	}

	/**
	 * @param ud
	 *            the UltrasonicDatas to set
	 */
	public void setUltrasonicDatas(UltrasonicDatas ud) {
		this.ud = ud;
	}

}

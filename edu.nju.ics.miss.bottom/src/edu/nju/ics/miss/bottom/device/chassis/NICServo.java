package edu.nju.ics.miss.bottom.device.chassis;

/**
 * 舵机
 * 
 * @author hsun
 * @version v0.1.0
 * 
 */
class NICServo {

	public static final NICServo S1 = new NICServo(1);

	public static final NICServo S2 = new NICServo(2);

	public static final NICServo S3 = new NICServo(3);

	public static final NICServo S4 = new NICServo(4);

	/** 舵机编号 */
	private int servoId;

	private NICServo(int id) {
		servoId = id;
	}

	public int getAngle() {
		return servoAngleQuery(servoId);
	}

	/**
	 * 设置舵机角度
	 * 
	 * @param angle
	 *            角度(0-1800)
	 */
	public void setAngle(int angle) {
		singleServoAngleCtrl(servoId, angle);
	}

	public static void setAllServosAngle(int angle1, int angle2, int angle3,
			int angle4) {
		broadcastServoAngleCtrl(angle1, angle2, angle3, angle4);
	}

	private static native int servoAngleQuery(int id);

	/**
	 * 根据Id设置舵机角度
	 * 
	 * @param id
	 *            舵机编号
	 * @param angel
	 *            角度
	 */
	private static native void singleServoAngleCtrl(int id, int angel);

	private static native void broadcastServoAngleCtrl(int angle1, int angle2,
			int angle3, int angle4);
}

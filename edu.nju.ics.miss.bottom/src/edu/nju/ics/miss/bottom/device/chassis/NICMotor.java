package edu.nju.ics.miss.bottom.device.chassis;

/**
 * 
 * Title: 电机<br>
 * <br>
 * Description: 电机控制<br>
 * <br>
 * Create-time: 2012-11-23 下午8:21:18<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
class NICMotor {

	/** 1号电机 */
	public static final NICMotor S1 = new NICMotor(1);

	/** 2号电机 */
	public static final NICMotor S2 = new NICMotor(2);

	/** 3号电机 */
	public static final NICMotor S3 = new NICMotor(3);

	/** 4号电机 */
	public static final NICMotor S4 = new NICMotor(4);

	/** 电机编号 */
	private int motorId;

	/**
	 * 构造函数
	 * 
	 * @param id
	 *            电机编号
	 */
	private NICMotor(int id) {
		motorId = id;
	}

	/**
	 * 查询当前电机累积转圈数，单位:角度
	 * 
	 * @return 累积转圈数
	 */
	public int getTachoCount() {
		return motorTachoCountQuery(motorId);
	}

	/**
	 * 重置电机累积转圈数(设为0)
	 */
	public void resetTachoCount() {
		motorTachoCountReset(motorId);
	}

	public void powerAndAngleCtrl(int power, int dis, int angle) {
		singleMotorPowerCtrl(motorId, power, dis, angle);
	}

	public static void groupPowerAndAngleCtrl(int[] param1, int[] param2,
			int[] param3, int[] param4) {
		broadcastMotorPowerCtrl(param1, param2, param3, param4);
	}

	public void speedAndAngleCtrl(int speed, int dis, int angle) {
		singleMotorSpeedCtrl(motorId, speed, dis, angle);
	}

	public static void groupSpeedAndAngleCtrl(int[] param1, int[] param2,
			int[] param3, int[] param4) {
		broadcastMotorSpeedCtrl(param1, param2, param3, param4);
	}

	private static native int motorTachoCountQuery(int id);

	private static native void motorTachoCountReset(int id);

	private static native void singleMotorPowerCtrl(int id, int power, int dis,
			int angle);

	private static native void broadcastMotorPowerCtrl(int[] param1,
			int[] param2, int[] param3, int[] param4);

	private static native void singleMotorSpeedCtrl(int id, int speed, int dis,
			int angle);

	private static native void broadcastMotorSpeedCtrl(int[] param1,
			int[] param2, int[] param3, int[] param4);
}

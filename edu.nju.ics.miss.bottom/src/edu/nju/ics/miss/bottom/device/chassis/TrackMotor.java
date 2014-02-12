package edu.nju.ics.miss.bottom.device.chassis;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-2-25 下午9:22:21<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class TrackMotor {

	/**
	 * 
	 * @param power1
	 * @param mode1
	 * @param distance
	 * @param power2
	 * @param mode2
	 * @param distance2
	 */
	public static void controlMotors(int power1, int mode1, int distance,
			int power2, int mode2, int distance2) {
		control2Motors((mode1 >= 3 ? 0 : (mode1 == 2 ? -power1 : power1)),
				(mode1 == 3 ? 1 : (mode1 == 4 ? 0 : mode1)), distance,
				(mode2 >= 3 ? 0 : (mode2 == 2 ? -power2 : power2)),
				(mode2 == 3 ? 1 : (mode2 == 4 ? 0 : mode2)), distance2);
	}

	/**
	 * 
	 * @param power1
	 * @param mode1
	 * @param dis1
	 * @param power2
	 * @param mode2
	 * @param dis2
	 */
	private static synchronized native void control2Motors(int power1,
			int mode1, int dis1, int power2, int mode2, int dis2);
}

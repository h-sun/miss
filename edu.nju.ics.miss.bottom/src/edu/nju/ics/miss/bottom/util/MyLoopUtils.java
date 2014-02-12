package edu.nju.ics.miss.bottom.util;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-6-26 下午6:49:22<br>
 *
 * @author hsun
 * 
 * @since v0.1.1
 *
 */
public class MyLoopUtils {
	
	public static void loopPause(){
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}


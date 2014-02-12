package edu.nju.ics.miss.smartcar;

import edu.nju.ics.miss.bottom.net.NICNetException;
import edu.nju.ics.miss.bottom.net.NICSocket;
import edu.nju.ics.miss.bottom.util.MyLogger;
import edu.nju.ics.miss.bottom.util.MyStringUtils;
import edu.nju.ics.miss.framework.ContextManager;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-30 下午8:46:22<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class SmartCarAContextManager extends ContextManager {

	private static NICSocket socketClient = null;

	private static boolean initial = true;

	public static String currentTask = null;

	// x1,y1,x2,y2
	private int startArea[] = new int[] { 100, 100, 100, 1000 };

	// x1,y1,x2,y2
	private int endArea[] = new int[] { 1000, 100, 1000, 1000 };

	@Override
	public void updateAllConditions() {
		if (initial) {
			try {
				myInit();
			} catch (NICNetException e) {
				MyLogger.writeLog(MyLogger.ERROR,
						"与服务器的初始化连接失败：" + e.getMessage());
				return;
			}
			initial = false;
		}

		try {
			int[] a = updateFromServer();
			if (a[0] >= startArea[0] && a[0] <= startArea[2]
					&& a[0] >= startArea[1] && a[0] <= startArea[3]) {
				addCondition(getCondition("InStartLocation"));
			} else if (a[0] >= endArea[0] && a[0] <= endArea[2]
					&& a[0] >= endArea[1] && a[0] <= endArea[3]) {
				addCondition(getCondition("InEndLocation"));
			}
		} catch (NICNetException e) {
			MyLogger.writeLog(MyLogger.ERROR, "从服务器更新数据失败：" + e.getMessage());
		}

	}

	private static void myInit() throws NICNetException {
		MyLogger.writeLog(MyLogger.DEBUG, "开始与服务器(192.168.9.112:5678)进行初始化连接");
		socketClient = NICSocket.getInstance("192.168.9.112", 5678);
		socketClient.connect();
		socketClient.writeLine("I_AM_CAR");
		String readStr = socketClient.readLine().trim();
		// 检查服务器端暗语是否正确
		if (!readStr.equalsIgnoreCase("I_AM_PC_SERVER")) {
			throw new NICNetException("wrong server code word:" + readStr);
		}
		MyLogger.writeLog(MyLogger.DEBUG, "与服务器初始化连接成功");
	}

	private int[] updateFromServer() throws NICNetException {

		socketClient.writeLine("GetData");

		String readStr = socketClient.readLine().trim();
		String[] datas1 = MyStringUtils.split(readStr, ";");
		if (datas1.length == 5) {
			try {
				int x1 = Integer.valueOf(datas1[1]);
				int y1 = Integer.valueOf(datas1[2]);
				int d1 = Integer.valueOf(datas1[3]);

				return new int[] { x1, y1, d1 };
			} catch (Exception e) {
				throw new NICNetException(e.getMessage());
			}
		}

		throw new NICNetException("从服务器获取数据失败！");
	}

	public static NICSocket getNicSocket() {
		if (initial) {
			try {
				myInit();
			} catch (NICNetException e) {
				MyLogger.writeLog(MyLogger.ERROR,
						"与服务器的初始化连接失败：" + e.getMessage());
				return null;
			}
			initial = false;
		}

		return socketClient;
	}

}

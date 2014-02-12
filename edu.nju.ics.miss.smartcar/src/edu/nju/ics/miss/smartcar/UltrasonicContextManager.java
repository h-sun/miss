package edu.nju.ics.miss.smartcar;

import edu.nju.ics.miss.bottom.MISSBottom;
import edu.nju.ics.miss.bottom.datatype.CarStatus;
import edu.nju.ics.miss.bottom.datatype.NICException;
import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.framework.ContextManager;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-29 下午8:55:12<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class UltrasonicContextManager extends ContextManager {

	private int frontSafeDistance = 500;
	private int rightSafeDistance = 500;
	private int backSafeDistance = 500;
	private int leftSafeDistance = 500;

	@Override
	public void updateAllConditions() {
		CarStatus status = null;
		try {
			status = MISSBottom.queryCarStatus();
		} catch (NICException e) {
			e.printStackTrace();
		}

		UltrasonicDatas ud = status.getUltrasonicDatas();

		// 处理前方数据
		int ldata = ud.getFrontDatas()[0];
		int rdata = ud.getFrontDatas()[1];
		int distance = getDistance(ldata, rdata);
		if (distance > frontSafeDistance) {
			addCondition(getCondition("frontIsSafe"));
		} else {
			addCondition(getCondition("!frontIsSafe"));
		}

		// 处理右方数据
		ldata = ud.getRightDatas()[0];
		rdata = ud.getRightDatas()[1];
		distance = getDistance(ldata, rdata);
		if (distance > rightSafeDistance) {
			addCondition(getCondition("rightIsSafe"));
		} else {
			addCondition(getCondition("!rightIsSafe"));
		}

		// 处理后方数据
		ldata = ud.getBackDatas()[0];
		rdata = ud.getBackDatas()[1];
		distance = getDistance(ldata, rdata);
		if (distance > backSafeDistance) {
			addCondition(getCondition("backIsSafe"));
		} else {
			addCondition(getCondition("!backIsSafe"));
		}

		// 处理左方数据
		ldata = ud.getLeftDatas()[0];
		rdata = ud.getLeftDatas()[1];
		distance = getDistance(ldata, rdata);
		if (distance > leftSafeDistance) {
			addCondition(getCondition("leftIsSafe"));
		} else {
			addCondition(getCondition("!leftIsSafe"));
		}

	}

	private int getDistance(int ldata, int rdata) {
		if (Math.abs(ldata - rdata) <= 100) {
			return (ldata + rdata) / 2;
		} else if (ldata > rdata) {
			return rdata;
		}

		return ldata;

	}

}

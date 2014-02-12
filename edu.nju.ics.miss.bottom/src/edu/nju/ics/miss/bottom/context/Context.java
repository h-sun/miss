package edu.nju.ics.miss.bottom.context;

import lejos.nxt.LCD;
import edu.nju.ics.miss.bottom.RequestMessage;
import edu.nju.ics.miss.bottom.datatype.ActionInfo;
import edu.nju.ics.miss.bottom.datatype.NICException;
import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.SmartCar;
import edu.nju.ics.miss.bottom.net.NICNetException;
import edu.nju.ics.miss.bottom.net.NICSocket;

public class Context {
	private ActionInfo actionInfo;

	private SmartCar smartCar = SmartCar.getInstance();

	private UltrasonicDatas ud;

	private static Context instance = null;

	private double turnRightCorrectionFactor = 0;

	private double turnLeftCorrectionFactor = 0;

	private int currentActionType = ActionInfo.ACTION_NO;

	private int minDistance = 320;

	private int maxDistance = 1800;

	private NICSocket socketClient = null;

	private Context() {
		actionInfo = new ActionInfo();
		ud = new UltrasonicDatas();

		//
		// socketClient = NICSocket.getInstance("192.168.9.111", 5678);
		// try {
		// socketClient.connect();
		// socketClient.writeLine("I_AM_CAR");
		// String readStr = socketClient.readLine().trim();
		// // 检查服务器端暗语是否正确
		// if (!readStr.equalsIgnoreCase("I_AM_PC_SERVER")) {
		// throw new NICNetException("wrong server code word:" + readStr);
		// }
		//
		// } catch (NICNetException e) {
		// socketClient = null;
		// }
	}

	public static Context getInstance() {

		if (instance == null) {
			instance = new Context();
		}

		return instance;
	}

	public void reset(RequestMessage request) throws NICException {

		updateDatas();

		actionInfo.setActionType(request.getActionType());
		actionInfo.setReferenceDistance(-1);
		actionInfo.setEnd(request.getParam());

		int adjustReference = request.getAdjustReference();
		int qualityAssurance = -1;
		int referenceDistance;
		switch (adjustReference) {
		case ActionInfo.REFERENCE_LEFT:
			referenceDistance = getDistance(ud.getLeftDatas()[0],
					ud.getLeftDatas()[1]);
			break;
		case ActionInfo.REFERENCE_RIGHT:
			referenceDistance = getDistance(ud.getRightDatas()[0],
					ud.getRightDatas()[1]);
			break;
		case ActionInfo.REFERENCE_FRONT:
			referenceDistance = getDistance(ud.getFrontDatas()[0],
					ud.getFrontDatas()[1]);
			break;
		case ActionInfo.REFERENCE_BACK:
			referenceDistance = getDistance(ud.getBackDatas()[0],
					ud.getBackDatas()[1]);
			break;
		default:
			adjustReference = -1;
			referenceDistance = -1;
			break;
		}

		if (referenceDistance == -1) {
			qualityAssurance = -1;
		} else {
			qualityAssurance = 1;
		}

		actionInfo.setAdjustReference(adjustReference);
		actionInfo.setReferenceDistance(referenceDistance);
		actionInfo.setQualityAssurance(qualityAssurance);

		// LCD.drawString("----reset----\n", 0, 0);
		// LCD.drawString("left:" + ud.getLeftDatas()[0] + "|"
		// + ud.getLeftDatas()[1] + "\n", 0, 1);
		// LCD.drawString("rd=" + referenceDistance + "\n", 0, 0);

		int power = request.getPowerLevel();
		actionInfo.setPowerLevel(power > 0 ? (power < 100 ? power : 100) : 0);

	}

	private int getDistance(int ldata, int rdata) {
		if (ldata > maxDistance || ldata < minDistance) {
			return -1;
		}
		if (rdata > maxDistance || rdata < minDistance) {
			return -1;
		}
		if (Math.abs(ldata - rdata) < 100) {
			return (ldata + rdata) / 2;
		}
		return ldata > rdata ? rdata : ldata;
	}

	private double param1 = smartCar.getAngleCorrectionFactor() * 180.0 / Math.PI;

	public double getAngleBias() {
		int ldata, rdata;
		switch (actionInfo.getAdjustReference()) {
		case ActionInfo.REFERENCE_LEFT:
			ldata = ud.getLeftDatas()[0];
			rdata = ud.getLeftDatas()[1];
			break;
		case ActionInfo.REFERENCE_RIGHT:
			ldata = ud.getRightDatas()[0];
			rdata = ud.getRightDatas()[1];
			break;
		default:
			return 0;
		}

		double interval = smartCar.getDistanceBetweenSensors();

		// 角度偏差
		double angleBias = Math.asin((rdata - ldata) / interval) * param1;

		return angleBias;
	}

	public double getLineBias() {
		int ldata, rdata;
		switch (actionInfo.getAdjustReference()) {
		case ActionInfo.REFERENCE_LEFT:
			ldata = ud.getLeftDatas()[0];
			rdata = ud.getLeftDatas()[1];
			break;
		case ActionInfo.REFERENCE_RIGHT:
			ldata = ud.getRightDatas()[0];
			rdata = ud.getRightDatas()[1];
			break;
		default:
			return 0;
		}

		// 小车与墙壁的实时距离
		int currentDistance = (ldata + rdata) / 2;

		double walkedDistance = smartCar.getDistanceWalked();

		// 线路偏差
		double lineBias = Math.asin((currentDistance - actionInfo
				.getReferenceDistance()) / walkedDistance)
				* param1;

		return lineBias;
	}

	synchronized public void updateDatas() {
		smartCar.obtainUltrasonicDatas(ud);
	}

	public boolean checkDatas() {
		if (socketClient != null) {
			try {
				socketClient.writeLine("GET_DATA");
				String readStr = socketClient.readLine().trim();
				LCD.drawString("\n-------network data--------\n", 0, 0);
				LCD.drawString(readStr, 0, 0);
				LCD.drawString("\n---------------------------\n", 0, 0);
			} catch (NICNetException e) {
				socketClient = null;
			}
		}

		int ldata, rdata;

		switch (actionInfo.getAdjustReference()) {
		case ActionInfo.REFERENCE_LEFT:
			ldata = ud.getLeftDatas()[0];
			rdata = ud.getLeftDatas()[1];
			break;
		case ActionInfo.REFERENCE_RIGHT:
			ldata = ud.getRightDatas()[0];
			rdata = ud.getRightDatas()[1];
			break;
		case ActionInfo.REFERENCE_FRONT:
			ldata = ud.getFrontDatas()[0];
			rdata = ud.getFrontDatas()[1];
			break;
		case ActionInfo.REFERENCE_BACK:
			ldata = ud.getBackDatas()[0];
			rdata = ud.getBackDatas()[1];
			break;
		default:
			return true;
		}

		// 存在无效数据
		if (ldata < minDistance || rdata < minDistance || ldata > maxDistance
				|| rdata > maxDistance) {

			return false;
		}

		if (Math.abs(ldata - rdata) > 500) {
			return false;
		}

		return true;
	}

	/**
	 * @return the actionInfo
	 */
	public ActionInfo getActionInfo() {
		return actionInfo;
	}

	/**
	 * @return the ud
	 */
	public UltrasonicDatas getUltrasonicDatas() {
		return ud;
	}

	/**
	 * @return the car
	 */
	public SmartCar getSmartCar() {
		return smartCar;
	}

	/**
	 * @return the turnRightCorrectionFactor
	 */
	public double getTurnRightCorrectionFactor() {
		return turnRightCorrectionFactor;
	}

	/**
	 * @return the turnLeftCorrectionFactor
	 */
	public double getTurnLeftCorrectionFactor() {
		return turnLeftCorrectionFactor;
	}

	/**
	 * @param turnRightCorrectionFactor
	 *            the turnRightCorrectionFactor to set
	 */
	public void setTurnRightCorrectionFactor(double turnRightCorrectionFactor) {
		this.turnRightCorrectionFactor = turnRightCorrectionFactor;
	}

	/**
	 * @param turnLeftCorrectionFactor
	 *            the turnLeftCorrectionFactor to set
	 */
	public void setTurnLeftCorrectionFactor(double turnLeftCorrectionFactor) {
		this.turnLeftCorrectionFactor = turnLeftCorrectionFactor;
	}

	/**
	 * @return the current Action
	 */
	public int getCurrentActionType() {
		return currentActionType;
	}

	/**
	 * @param currentActionType
	 *            the currentActionType to set
	 */
	public void setCurrentActionType(int currentActionType) {
		this.currentActionType = currentActionType;
	}

}

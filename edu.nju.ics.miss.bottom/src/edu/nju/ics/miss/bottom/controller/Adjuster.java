package edu.nju.ics.miss.bottom.controller;

import edu.nju.ics.miss.bottom.MISSBottom;
import edu.nju.ics.miss.bottom.RequestMessage;
import edu.nju.ics.miss.bottom.context.Context;
import edu.nju.ics.miss.bottom.datatype.ActionInfo;
import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.DeviceException;

public class Adjuster {

	/** 公共数据 */
	private Context context = Context.getInstance();

	private static Adjuster instance = null;

	private Adjuster() {
	}

	public static Adjuster getInstance() {
		if (instance == null) {
			instance = new Adjuster();
		}

		return instance;
	}

	public boolean doBalance() throws DeviceException {
		// // 调整前，小车与参照物的角度误差
		// double angleTheta = 0;
		//
		// // 系统不需要调整
		// if (context.getActionInfo().getQualityAssurance() <= 0) {
		// return false;
		// }
		//
		// // 更新数据
		// context.updateDatas();
		//
		// int ldata, rdata;
		// // 检查超声波数
		// if (!checkUltrasonicDatas()) {
		// if (context.getActionInfo().getActionType() ==
		// ActionInfo.ACTION_FORWARD
		// || context.getActionInfo().getActionType() ==
		// ActionInfo.ACTION_BACKWARD) {
		// doBalance4Insecurity();
		// }
		// return false;
		// }
		//
		// switch (context.getActionInfo().getAdjustReference()) {
		// case ActionInfo.REFERENCE_FRONT:
		// ldata = context.getUltrasonicDatas().getFrontDatas()[0];
		// rdata = context.getUltrasonicDatas().getFrontDatas()[1];
		// break;
		// case ActionInfo.REFERENCE_RIGHT:
		// ldata = context.getUltrasonicDatas().getRightDatas()[0];
		// rdata = context.getUltrasonicDatas().getRightDatas()[1];
		// break;
		// case ActionInfo.REFERENCE_BACK:
		// ldata = context.getUltrasonicDatas().getBackDatas()[0];
		// rdata = context.getUltrasonicDatas().getBackDatas()[1];
		// break;
		// case ActionInfo.REFERENCE_LEFT:
		// ldata = context.getUltrasonicDatas().getLeftDatas()[0];
		// rdata = context.getUltrasonicDatas().getLeftDatas()[1];
		// break;
		// default:
		// return false;
		// }
		//
		// // 角度误差
		// angleTheta = Math.asin((rdata - ldata)
		// / context.getSmartCar().getDistanceBetweenSensors())
		// * 180 / Math.PI;
		//
		// // 如果角度误差太大，则不做处理，使用历史信息进行处理
		// if (Math.abs(angleTheta) > 40) {
		// return false;
		// }
		//
		// // 1.调整小车姿态，将小车调整至与参照物平行的姿态
		// if (angleTheta > 0) {// 向右转多了
		// context.getSmartCar().rotateLeft(angleTheta);
		// } else {// 向左转多了
		// context.getSmartCar().rotateRight(-angleTheta);
		// }

		return true;
	}

	public void adjust2() {
		if (context.getActionInfo().getQualityAssurance() <= 0) {
			return;
		}
		switch (context.getActionInfo().getActionType()) {
		case ActionInfo.ACTION_FORWARD:
		case ActionInfo.ACTION_BACKWARD:
			break;
		default:
			return;
		}
/*
		while (context.isAdjust()) {
			if (!context.getSmartCar().isRunning()) {
				break;
			}

			context.updateDatas();
			if (!context.checkDatas()) {
				continue;
			}
			int ldata = -1, rdata = -1;
			switch (context.getActionInfo().getAdjustReference()) {
			case ActionInfo.REFERENCE_LEFT:
				ldata = context.getUltrasonicDatas().getLeftDatas()[0];
				rdata = context.getUltrasonicDatas().getLeftDatas()[1];
				break;
			case ActionInfo.REFERENCE_RIGHT:
				ldata = context.getUltrasonicDatas().getRightDatas()[0];
				rdata = context.getUltrasonicDatas().getRightDatas()[1];
				break;
			}
			if (ldata == rdata && ldata == -1) {
				break;
			}
			// 角度误差
			double angle = Math.asin((rdata - ldata)
					/ context.getSmartCar().getDistanceBetweenSensors());

			boolean isPositive = angle >= 0 ? true : false;
			double absAngle = Math.abs(angle);
			if (absAngle < 1) {
				continue;
			}

			try {
				if (absAngle < 5) {
					if (isPositive)
						context.getSmartCar().fineTuning(-1);
					else
						context.getSmartCar().fineTuning(1);
				} else if (absAngle < 10) {
					if (isPositive)
						context.getSmartCar().fineTuning(-3);
					else
						context.getSmartCar().fineTuning(3);
				} else {
					if (isPositive)
						context.getSmartCar().fineTuning(-5);
					else
						context.getSmartCar().fineTuning(5);
				}
			} catch (DeviceException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}*/

	}

	/**
	 * public boolean adjust() throws DeviceException {
	 * NICLogger.writeLog(NICLogger.DEBUG, "-------adjust start------"); //
	 * 系统不需要调整 if (context.getActionInfo().getQualityAssurance() <= 0) { return
	 * false; }
	 * 
	 * // 非前进或后退动作，不调整 switch (context.getActionInfo().getActionType()) { case
	 * ActionInfo.ACTION_FORWARD: case ActionInfo.ACTION_BACKWARD: break;
	 * default: return false; }
	 * 
	 * NICLogger.writeLog(NICLogger.DEBUG, "maxAdjustAngle=" +
	 * context.getActionInfo().getMaxAdjustAngle());
	 * NICLogger.writeLog(NICLogger.DEBUG, "maxResetAngle=" +
	 * context.getActionInfo().getMaxResetAngle());
	 * 
	 * // 更新数据 context.updateDatas();
	 * 
	 * // 检查超声波数据 if (!checkUltrasonicDatas()) {
	 * context.getActionInfo().setLastReferenceDistance(0);
	 * context.getUltrasonicDatas().printDatas(); doBalance4Insecurity(); return
	 * false; }
	 * 
	 * int ldata, rdata; switch (context.getActionInfo().getAdjustReference()) {
	 * case ActionInfo.REFERENCE_LEFT: ldata =
	 * context.getUltrasonicDatas().getLeftDatas()[0]; rdata =
	 * context.getUltrasonicDatas().getLeftDatas()[1]; break;
	 * 
	 * default: ldata = context.getUltrasonicDatas().getRightDatas()[0]; rdata =
	 * context.getUltrasonicDatas().getRightDatas()[1]; break; }
	 * 
	 * // 角度误差 double angleTheta = Math.asin((rdata - ldata) /
	 * context.getSmartCar().getDistanceBetweenSensors());
	 * 
	 * // 当前小车与参照物的距离 int currDistance = (ldata + rdata) / 2; currDistance =
	 * (int) (currDistance * Math.cos(angleTheta));
	 * 
	 * angleTheta = angleTheta * 180 / Math.PI;
	 * 
	 * context.getActionInfo().setBalanceFactor(
	 * context.getActionInfo().getBalanceFactor() * 0.3 + angleTheta 0.7);
	 * 
	 * // 小车与参照物的原始距离 int refDistance =
	 * context.getActionInfo().getReferenceDistance(); double dvalue =
	 * currDistance - refDistance;
	 * 
	 * // 已行走的距离 int fs = context.getActionInfo().getFinishedSectionsCount();
	 * double walkedDistance = (fs < 0 ? 0 : fs)
	 * context.getActionInfo().getSection();
	 * 
	 * context.getUltrasonicDatas().printDatas();
	 * NICLogger.writeLog(NICLogger.DEBUG, "walkedDistance=" + walkedDistance);
	 * NICLogger.writeLog(NICLogger.DEBUG, "refDistance=" + refDistance);
	 * NICLogger.writeLog(NICLogger.DEBUG, "currDistance=" + currDistance);
	 * NICLogger.writeLog(NICLogger.DEBUG, "angleTheta=" + angleTheta);
	 * 
	 * if (walkedDistance <= 0) { NICLogger.writeLog(NICLogger.DEBUG,
	 * "walkedDistance=" + walkedDistance); // 复位，返回
	 * context.getActionInfo().setLastReferenceDistance(currDistance);
	 * 
	 * if (angleTheta > 0) {// 向右转多了 context.getSmartCar().rotateLeft(angleTheta
	 * * 1.5); } else {// 向左转多了 context.getSmartCar().rotateRight(-angleTheta *
	 * 1.5); }
	 * 
	 * return false; }
	 * 
	 * if (Math.abs(dvalue) >= Math.abs(walkedDistance)) {
	 * NICLogger.writeLog(NICLogger.DEBUG, "dvalue=" + dvalue);
	 * context.getActionInfo().setLastReferenceDistance(0);
	 * 
	 * if (angleTheta > 0) {// 向右转多了 context.getSmartCar().rotateLeft(angleTheta
	 * * 1.5); } else {// 向左转多了 context.getSmartCar().rotateRight(-angleTheta *
	 * 1.5); }
	 * 
	 * return false; }
	 * 
	 * double lastDistance = context.getActionInfo()
	 * .getLastReferenceDistance(); double lastRunningAngle =
	 * context.getActionInfo().getLastRunningAngle();
	 * 
	 * // 线路偏差角度 // double lineTheta = Math.asin(dvalue / walkedDistance) * 180
	 * / // Math.PI; double lineTheta = Math.asin((currDistance - lastDistance)
	 * / context.getActionInfo().getSection()) 180 / Math.PI;
	 * 
	 * double adjustAngle = angleTheta + lineTheta * 0.5 - lastRunningAngle;
	 * 
	 * NICLogger.writeLog(NICLogger.DEBUG, "lineTheta=" + lineTheta);
	 * NICLogger.writeLog(NICLogger.DEBUG, "adjustetAngle=" + adjustAngle);
	 * 
	 * // 向反方向调整 if (adjustAngle > 0) {// 右偏
	 * context.getSmartCar().rotateLeft(reallyAdjustAngle(adjustAngle)); } else
	 * if (adjustAngle < 0) {// 左偏
	 * context.getSmartCar().rotateRight(reallyAdjustAngle(adjustAngle)); }
	 * 
	 * // 2.4做前进或后退自适应调制，纠正使其走直线
	 * 
	 * context.getActionInfo().setLastReferenceDistance(currDistance);
	 * context.getActionInfo().setLastRunningAngle(-lineTheta * 0.5);
	 * 
	 * if (lastDistance <= 0) { return true; }
	 * 
	 * double reallyAngle = Math.asin((currDistance - lastDistance) /
	 * context.getActionInfo().getSection()) 180 / Math.PI; double dAngle =
	 * reallyAngle - lastRunningAngle;
	 * 
	 * // context.getSmartCar().reset(-reallyResetAngle(dAngle));
	 * 
	 * NICLogger.writeLog(NICLogger.DEBUG, "lastRunningAngle=" +
	 * lastRunningAngle); NICLogger.writeLog(NICLogger.DEBUG, "dAngle=" + dAngle
	 * + "\n");
	 * 
	 * return true; }
	 * 
	 * 
	 * private void doBalance4Insecurity() throws DeviceException { if
	 * (context.getActionInfo().getBalanceFactor() > 0) {
	 * context.getSmartCar().rotateLeft(
	 * context.getActionInfo().getBalanceFactor()); } else {
	 * context.getSmartCar().rotateRight(
	 * -context.getActionInfo().getBalanceFactor()); }
	 * 
	 * }
	 */

	/*
	private double reallyAdjustAngle(double adjustAngle) {
		double a = Math.abs(adjustAngle);
		if (a > context.getActionInfo().getMaxAdjustAngle()) {
			return context.getActionInfo().getMaxAdjustAngle();
		} else {
			// maxAdjustAngle = maxAdjustAngle * 0.25 + a * 0.75 + 1;
			context.getActionInfo().setMaxAdjustAngle(
					context.getActionInfo().getMaxAdjustAngle() * 0.5 + 2);
			return a;
		}
	}

	private double reallyResetAngle(double resetAngle) {
		double r = Math.abs(resetAngle);
		if (r > context.getActionInfo().getMaxResetAngle()) {
			return resetAngle > 0 ? context.getActionInfo().getMaxResetAngle()
					: -context.getActionInfo().getMaxResetAngle();
		} else {
			// maxResetAngle = maxResetAngle * 0.25 + r * 0.75 + 1;
			context.getActionInfo().setMaxResetAngle(
					context.getActionInfo().getMaxResetAngle() * 0.5 + 3);
			return resetAngle;
		}
	}
*/
//	private boolean checkUltrasonicDatas() {
//		int ldata, rdata;
//		int retryCount = 0;
//		boolean b = false;
//		switch (context.getActionInfo().getAdjustReference()) {
//		case ActionInfo.REFERENCE_LEFT:
//			while (true) {
//				ldata = context.getUltrasonicDatas().getLeftDatas()[0];
//				rdata = context.getUltrasonicDatas().getLeftDatas()[1];
//				// 存在无效数据
//				if (ldata < context.getMinDistance()
//						|| rdata < context.getMinDistance()
//						|| ldata > context.getMaxDistance()
//						|| rdata > context.getMaxDistance()) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//
//				if (Math.abs(ldata - rdata) > 300) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//				b = true;
//				break;
//			}
//			// LCD.drawString(b + "\n\n", 0, 0);
//			return b;
//		case ActionInfo.REFERENCE_RIGHT:
//			while (true) {
//				ldata = context.getUltrasonicDatas().getRightDatas()[0];
//				rdata = context.getUltrasonicDatas().getRightDatas()[1];
//				// 存在无效数据
//				if (ldata < context.getMinDistance()
//						|| rdata < context.getMinDistance()
//						|| ldata > context.getMaxDistance()
//						|| rdata > context.getMaxDistance()) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//
//				if (Math.abs(ldata - rdata) > 300) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//				b = true;
//				break;
//			}
//			return b;
//		case ActionInfo.REFERENCE_FRONT:
//			while (true) {
//				ldata = context.getUltrasonicDatas().getFrontDatas()[0];
//				rdata = context.getUltrasonicDatas().getFrontDatas()[1];
//				// 存在无效数据
//				if (ldata < context.getMinDistance()
//						|| rdata < context.getMinDistance()
//						|| ldata > context.getMaxDistance()
//						|| rdata > context.getMaxDistance()) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//
//				if (Math.abs(ldata - rdata) > 300) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//				b = true;
//				break;
//			}
//			return b;
//		case ActionInfo.REFERENCE_BACK:
//			while (true) {
//				ldata = context.getUltrasonicDatas().getBackDatas()[0];
//				rdata = context.getUltrasonicDatas().getBackDatas()[1];
//				// 存在无效数据
//				if (ldata < context.getMinDistance()
//						|| rdata < context.getMinDistance()
//						|| ldata > context.getMaxDistance()
//						|| rdata > context.getMaxDistance()) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//
//				if (Math.abs(ldata - rdata) > 300) {
//					if (retryCount > 0) {
//						retryCount--;
//						context.updateDatas();
//						continue;
//					}
//					break;
//				}
//				b = true;
//				break;
//			}
//			return b;
//
//		}
//
//		return true;
//	}

	public boolean selfLearning() {
		// 1.先获取超声波传感器数据
		context.updateDatas();

		UltrasonicDatas ud = context.getUltrasonicDatas();

		// 2.分析数据
		int leftL = ud.getLeftDatas()[0];
		int leftR = ud.getLeftDatas()[1];
		int leftD = leftL + leftR;
		int rightL = ud.getRightDatas()[0];
		int rightR = ud.getRightDatas()[1];
		int rightD = rightL + rightR;
		int frontL = ud.getFrontDatas()[0];
		int frontR = ud.getFrontDatas()[1];
		int frontD = frontL + frontR;
		int backL = ud.getBackDatas()[0];
		int backR = ud.getBackDatas()[1];
		int backD = backL + backR;

		int datas[] = new int[] { frontD, rightD, backD, leftD };

		// LCD.clear();
		// for (int i = 0; i < datas.length; i++) {
		// LCD.drawString("" + datas[i], 0, i);
		// }
		// Button.waitForAnyPress();

		for (int i = 0; i < datas.length; i++) {
			int t = -1;
			for (int j = i - 1; j >= 0; j--) {
				if (datas[i] < datas[j]) {
					continue;
				} else {
					t = j;
					break;
				}

			}
			t++;
			int d = datas[i];
			for (int j = i; j >= t + 1; j--) {
				datas[j] = datas[j - 1];
			}
			datas[t] = d;
		}

		int min = -1;
//		for (int data : datas) {
//			if (data < context.getMinDistance()
//					|| data > context.getMaxDistance()) {
//				continue;
//			}
//			min = data;
//			break;
//		}
//
//		if (min < context.getMinDistance()) {
//			return false;
//		}

		int actionType1;
		int ref1;
		int actionType2;
		int ref2;

		if (frontD == min) {
			if (leftD > 0 && rightD >= 0) {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_LEFT;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_FRONT;
			} else if (leftD > 0) {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_LEFT;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_FRONT;
			} else {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_RIGHT;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_FRONT;
			}
		} else if (rightD == min) {
			if (frontD > 0 && backD >= 0) {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_FRONT;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_RIGHT;
			} else if (frontD > 0) {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_FRONT;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_RIGHT;
			} else {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_BACK;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_RIGHT;
			}
		} else if (backD == min) {
			if (leftD > 0 && rightD >= 0) {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_LEFT;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_BACK;
			} else if (leftD > 0) {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_LEFT;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_BACK;
			} else {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_RIGHT;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_BACK;
			}
		} else {
			if (frontD > 0 && backD >= 0) {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_FRONT;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_LEFT;
			} else if (frontD > 0) {
				actionType1 = ActionInfo.ACTION_TURN_LEFT;
				ref1 = ActionInfo.REFERENCE_FRONT;
				actionType2 = ActionInfo.ACTION_TURN_RIGHT;
				ref2 = ActionInfo.REFERENCE_LEFT;
			} else {
				actionType1 = ActionInfo.ACTION_TURN_RIGHT;
				ref1 = ActionInfo.REFERENCE_BACK;
				actionType2 = ActionInfo.ACTION_TURN_LEFT;
				ref2 = ActionInfo.REFERENCE_LEFT;
			}
		}

		// LCD.clear();
		// LCD.drawString("a1:" + actionType1, 0, 0);
		// LCD.drawString("r1:" + ref1, 0, 1);
		//
		// LCD.drawString("a2:" + actionType2, 0, 2);
		// LCD.drawString("r2:" + ref2, 0, 3);
		//
		// Button.waitForAnyPress();

		RequestMessage request = new RequestMessage();
		request.setActionType(actionType1);
		request.setAdjustReference(ref1);
		request.setParam(90);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		request = new RequestMessage();
		request.setActionType(actionType2);
		request.setAdjustReference(ref2);
		request.setParam(90);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// LCD.clear();
		// LCD.drawString("r:" + context.getTurnRightCorrectionFactor(), 0, 0);
		// LCD.drawString("l:" + context.getTurnLeftCorrectionFactor(), 0, 1);
		// Button.waitForAnyPress();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		request = new RequestMessage();
		request.setActionType(actionType1);
		request.setAdjustReference(ref1);
		request.setParam(90);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		request = new RequestMessage();
		request.setActionType(actionType2);
		request.setAdjustReference(ref2);
		request.setParam(90);

		try {
			MISSBottom.startAction(request);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		// LCD.clear();
		// LCD.drawString("r:" + context.getTurnRightCorrectionFactor(), 0, 0);
		// LCD.drawString("l:" + context.getTurnLeftCorrectionFactor(), 0, 1);
		// Button.waitForAnyPress();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return true;
	}

}

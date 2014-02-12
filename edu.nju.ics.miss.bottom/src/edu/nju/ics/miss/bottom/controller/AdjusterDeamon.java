package edu.nju.ics.miss.bottom.controller;

import lejos.nxt.LCD;
import edu.nju.ics.miss.bottom.context.Context;
import edu.nju.ics.miss.bottom.datatype.ActionInfo;
import edu.nju.ics.miss.bottom.device.DeviceException;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-3-26 下午7:40:31<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class AdjusterDeamon {

	/** 公共数据 */
	private Context context = Context.getInstance();

	private boolean isDeamon = false;

	private static AdjusterDeamon instance = null;

	private AdjusterDeamon() {
	}

	public static AdjusterDeamon getInstance() {
		if (instance == null) {
			instance = new AdjusterDeamon();
		}

		return instance;
	}

	public void startDeamon(boolean immediateReturn) {
		if (isDeamon) {
			stopDeamon();
		}

		isDeamon = true;

		delay(100);

		if (immediateReturn) {

			new Thread() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					doDeamon();
				}

			}.start();
		} else {
			doDeamon();
		}

	}

	private void doDeamon() {
		// LCD.drawString(".....doDeamon 111.....\n", 0, 0);
		if (context.getActionInfo().getQualityAssurance() <= 0) {
			return;
		}

		// LCD.drawString(".....doDeamon 222.....\n", 0, 0);

		switch (context.getActionInfo().getActionType()) {
		case ActionInfo.ACTION_FORWARD:
		case ActionInfo.ACTION_BACKWARD:
			break;
		default:
			return;
		}

		double lastAngleBias = 0.0;
		double lastLineBias = 0.0;

		// LCD.drawString(".....doDeamon 333.....\n", 0, 0);
		while (isDeamon) {

			delay(100);

			if (!isDeamon) {
				break;
			}

			if (!context.getSmartCar().isRunning()) {
				break;
			}

			context.updateDatas();
			// 2.如超声波数据错误，则返回到循环开始位置
			if (!context.checkDatas()) {
				continue;
			}

			if (!isDeamon) {
				break;
			}

			// 角度偏差
			double angleBias = context.getAngleBias();
			// 线路偏差
			double lineBias = context.getLineBias();

			if (!isDeamon) {
				break;
			}

			double ftAngle = 0.0;

			if (lineBias > 0) {
				if (lineBias > lastLineBias) {
					if (lastLineBias < 0) {
						ftAngle = -lineBias;
					} else {
						ftAngle = lastLineBias - lineBias;
					}
				}
			} else if (lineBias < 0) {
				if (lineBias < lastLineBias) {
					if (lastLineBias > 0) {
						ftAngle = -lineBias;
					} else {
						ftAngle = lastLineBias - lineBias;
					}
				}
			}

			if (ftAngle > 0 && ftAngle < 0.1) {
				ftAngle = 0.1;
			} else if (ftAngle < 0 && ftAngle > -0.1) {
				ftAngle = -0.1;
			}

			double tAngle = 0;
			if (angleBias > 1.5) {
				if (angleBias > lastAngleBias) {
					if (lastAngleBias < 0) {
						tAngle = -angleBias;
					} else {
						tAngle = lastAngleBias - angleBias;
					}
				}
			} else if (angleBias < -1.5) {
				if (angleBias < lastAngleBias) {
					if (lastAngleBias > 0) {
						tAngle = -angleBias;
					} else {
						tAngle = lastAngleBias - angleBias;
					}
				}
			}

			if (tAngle > 0 && tAngle < 1.5) {
				tAngle = 1.5;
			} else if (tAngle < 0 && tAngle > -1.5) {
				tAngle = -1.5;
			}

			tAngle = tAngle * 1.5;

			// if (Math.abs(lineBias) < 1) {
			// tAngle = tAngle * 1.5;
			// } else if (Math.abs(lineBias) < 2) {
			// tAngle = tAngle * 3;
			// } else if (Math.abs(lineBias) < 3) {
			// tAngle = tAngle * 6;
			// } else {
			// tAngle = tAngle * 8;
			// }

			if (!isDeamon) {
				break;
			}

			// context.getUltrasonicDatas().printDatas();
			// LCD.drawString("walkedDistance:"
			// + context.getSmartCar().getDistanceWalked() + "\n", 0, 0);
			// LCD.drawString("angleBias:" + angleBias + "\n", 0, 0);
			// LCD.drawString("lineBias:" + lineBias + "\n", 0, 0);

			try {
				context.getSmartCar().turnAndFineTuning(tAngle, ftAngle);
//				if (context.getActionInfo().getActionType() == ActionInfo.ACTION_FORWARD) {
//					context.getSmartCar().turnAndFineTuning(tAngle, ftAngle);
//				} else {
//					context.getSmartCar().turnAndFineTuning(tAngle, -ftAngle);
//				}
			} catch (DeviceException e) {
				e.printStackTrace();
			}

			lastLineBias = lineBias;
			if (Math.abs(angleBias) >= 1.5) {
				lastAngleBias = angleBias;
			}
		}
	}

	public void stopDeamon() {
		isDeamon = false;
		delay(500);
	}

	public boolean isDeamon() {
		return isDeamon;
	}

	public boolean doBalance() throws DeviceException {
		// 系统不需要调整
		if (context.getActionInfo().getQualityAssurance() <= 0) {
			return false;
		}

		// 更新数据
		context.updateDatas();

		// 检查超声波数
		if (!context.checkDatas()) {
			return false;
		}

		// 调整前，小车与参照物的角度误差
		double angleTheta = context.getAngleBias();

		// LCD.drawString("...doBalance...\n", 0, 0);
		// LCD.drawString("angleTheta:" + angleTheta + "\n", 0, 0);

		// 如果角度误差太大，则不做处理，使用历史信息进行处理
		if (angleTheta > 5) {
			angleTheta = 5;
		} else if (angleTheta < -5) {
			angleTheta = -5;
		}

		// 1.调整小车姿态，将小车调整至与参照物平行的姿态
		if (angleTheta > 0) {// 向右转多了
			context.getSmartCar().rotateLeft(angleTheta);
		} else {// 向左转多了
			context.getSmartCar().rotateRight(-angleTheta);
		}

		return true;
	}

	public boolean doBalance4Turn() throws DeviceException {
		// 系统不需要调整
		if (context.getActionInfo().getQualityAssurance() <= 0) {
			return false;
		}

		// 更新数据
		context.updateDatas();

		// 检查超声波数
		if (!context.checkDatas()) {
			return false;
		}

		// 调整前，小车与参照物的角度误差
		double angleTheta = context.getAngleBias();

		// 如果角度误差太大，则不做处理，使用历史信息进行处理
		if (Math.abs(angleTheta) > 40) {
			return false;
		}

		// 1.调整小车姿态，将小车调整至与参照物平行的姿态
		if (angleTheta > 0) {// 向右转多了
			context.getSmartCar().rotateLeft(angleTheta);
		} else {// 向左转多了
			context.getSmartCar().rotateRight(-angleTheta);
		}

		if (context.getActionInfo().getActionType() == ActionInfo.ACTION_TURN_LEFT) {
			double nangle = context.getTurnLeftCorrectionFactor() + angleTheta
					* 0.85;
			context.setTurnLeftCorrectionFactor(nangle);
		} else {
			double nangle = context.getTurnRightCorrectionFactor() + angleTheta
					* 0.85;
			context.setTurnRightCorrectionFactor(nangle);
		}

		return false;
	}

	private void delay(long millisecond) {
		if (millisecond <= 0) {
			return;
		}

		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
		}
	}

}

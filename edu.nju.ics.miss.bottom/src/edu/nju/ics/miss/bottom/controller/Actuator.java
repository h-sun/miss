package edu.nju.ics.miss.bottom.controller;

import java.io.IOException;

import edu.nju.ics.miss.bottom.context.Context;
import edu.nju.ics.miss.bottom.datatype.ActionInfo;
import edu.nju.ics.miss.bottom.device.DeviceException;
import edu.nju.ics.miss.bottom.device.ModelParser;

public class Actuator {

	private Context context = Context.getInstance();

	private double turnRightAngle = 90;

	private double turnLeftAngle = 90;

	private static Actuator instance = null;

	private Actuator() {
		try {
			turnLeftAngle = ModelParser.getInstance().getTurnLeftAngle();
			turnRightAngle = ModelParser.getInstance().getTurnRightAngle();
		} catch (IOException e) {
			turnRightAngle = 90;
			turnLeftAngle = 90;
		}
	}

	public static Actuator getInstance() {
		if (instance == null) {
			instance = new Actuator();
		}

		return instance;
	}

	/**
	 * 执行行走动作
	 * 
	 * @param immediatelyReturn
	 *            是否立即返回
	 */
	public void executeTravel(boolean immediatelyReturn) {
		int distance = context.getActionInfo().getEnd();
		if (immediatelyReturn) {
			if (distance >= 0) {
				try {
					switch (context.getActionInfo().getActionType()) {
					case ActionInfo.ACTION_FORWARD:
						context.getSmartCar().forward(distance, true);
						break;
					case ActionInfo.ACTION_BACKWARD:
						context.getSmartCar().backward(distance, true);
						break;
					}
				} catch (DeviceException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (distance > 0) {
				try {
					switch (context.getActionInfo().getActionType()) {
					case ActionInfo.ACTION_FORWARD:
						context.getSmartCar().forward(distance, false);
						break;
					case ActionInfo.ACTION_BACKWARD:
						context.getSmartCar().backward(distance, false);
						break;
					}
				} catch (DeviceException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 执行转弯动作<br>
	 * [注]:此动作为同步动作,即执行完才返�?
	 * 
	 * @throws DeviceException
	 */
	public void executeTurn() throws DeviceException {

		switch (context.getActionInfo().getActionType()) {
		case ActionInfo.ACTION_TURN_LEFT: {
			context.getSmartCar().rotateLeft(
					turnLeftAngle + context.getTurnLeftCorrectionFactor());
			break;
		}
		case ActionInfo.ACTION_TURN_RIGHT: {
			context.getSmartCar().rotateRight(
					turnRightAngle + context.getTurnRightCorrectionFactor());
			break;
		}
		default:
			throw new DeviceException("Error action type:"
					+ context.getActionInfo().getActionType());
		}

	}

	/**
	 * 终止当前动作
	 */
	public void terminateExecute() {

		try {
			context.getSmartCar().brake();
		} catch (DeviceException e) {
			e.printStackTrace();
		}

	}
}

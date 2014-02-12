package edu.nju.ics.miss.bottom.device.chassis;

import java.io.IOException;

import lejos.nxt.MotorPort;
import edu.nju.ics.miss.bottom.device.Chassis;
import edu.nju.ics.miss.bottom.device.DeviceException;
import edu.nju.ics.miss.bottom.device.ModelParser;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-2-25 下午9:19:19<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class Track extends Chassis {

	private double distanceWalkedFactor = Math.PI * 43.74 / 360.0;

	private double turnFactor = Math.PI * 127.5 / 180;

	private double middleAngle = 0.0;

	private final int action_forward = 1;

	private final int action_null = 0;

	private final int action_backward = -1;

	private int currentAction = 0;

	private int requiredDistance = -1;

	private boolean isTurning = false;

	public Track() {
		power = 70;
		try {
			middleAngle = ModelParser.getInstance().getMiddleAngleParams()[0];
		} catch (IOException e) {
			middleAngle = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#forward(int, boolean)
	 */
	@Override
	public void forward(int distance, boolean immediateReturn)
			throws DeviceException {
		currentAction = action_forward;
		requiredDistance = distance;

		MotorPort.A.resetTachoCount();
		MotorPort.B.resetTachoCount();

		walk(action_forward, distance);

		if (!immediateReturn) {
			while (true) {
				if (!isRunning()) {
					break;
				}
			}
		}
	}

	private void walk(int direction, int distance) throws DeviceException {
		int model;
		if (direction == action_forward) {
			model = 1;
		} else if (direction == action_backward) {
			model = 2;
		} else {
			return;
		}

		onlyTurn(middleAngle);

		TrackMotor
				.controlMotors(30, model, distance, 30, model, distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#backward(int, boolean)
	 */
	@Override
	public void backward(int distance, boolean immediateReturn)
			throws DeviceException {
		currentAction = action_backward;
		requiredDistance = distance;

		MotorPort.A.resetTachoCount();
		MotorPort.B.resetTachoCount();

		walk(action_backward, distance);

		if (!immediateReturn) {
			while (true) {
				if (!isRunning()) {
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#stop()
	 */
	@Override
	public void brake() throws DeviceException {
		while (isTurning) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		TrackMotor.controlMotors(0, 3, 0, 0, 3, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#turn(double)
	 */
	@Override
	public void turn(double angle) throws DeviceException {

		int walkedDistance = 0;

		boolean isRunning = false;

		// 判断小车当前是否在运行
		if (isRunning()) {
			isRunning = true;
			if (currentAction != action_null) {
				walkedDistance = getDistanceWalked();
			}
			brake();
		}

		onlyTurn(angle);

		if (isRunning && currentAction != action_null) {
			int remainderDistance;
			if (requiredDistance == 0) {
				remainderDistance = 0;
			} else {
				remainderDistance = requiredDistance - walkedDistance;
			}
			walk(currentAction, remainderDistance);
		}
	}

	private void onlyTurn(double angle) throws DeviceException {
		int dis = (int) (angle * turnFactor);

		if (Math.abs(dis) < 2) {
			return;
		}

		isTurning = true;

		if (dis > 0) {
			TrackMotor.controlMotors(power, 1, dis, power, 2, dis);
		} else {
			TrackMotor.controlMotors(power, 2, -dis, power, 1, -dis);
		}

		while (true) {
			if (!isRunning()) {
				break;
			}
		}

		isTurning = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#fineTuning(double)
	 */
	@Override
	public void fineTuning(double angle) throws DeviceException {
		// middleAngle += angle;
		// if (middleAngle > 5) {
		// middleAngle = 5;
		// } else if (middleAngle < -5) {
		// middleAngle = -5;
		// }

		// turn(middleAngle);
		turn(angle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#turnAndFineTuning(double,
	 * double)
	 */
	@Override
	public void turnAndFineTuning(double tAngle, double ftAngle)
			throws DeviceException {
		middleAngle += ftAngle;
		if (middleAngle > 5) {
			middleAngle = 5;
		} else if (middleAngle < -5) {
			middleAngle = -5;
		}

		// double angle = tAngle + ftAngle;
		turn(tAngle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#getDistanceWalked()
	 */
	@Override
	public int getDistanceWalked() {
		int tachoCount = Math.abs(MotorPort.A.getTachoCount())
				+ Math.abs(MotorPort.B.getTachoCount());

		tachoCount /= 2;

		return (int) (distanceWalkedFactor * tachoCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#isRunning()
	 */
	@Override
	public boolean isRunning() {
		int tachoCount1 = MotorPort.A.getTachoCount();
		int tachoCount3 = MotorPort.B.getTachoCount();

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}

		int tachoCount2 = MotorPort.A.getTachoCount();
		int tachoCount4 = MotorPort.B.getTachoCount();

		if ((tachoCount1 == tachoCount2) && (tachoCount3 == tachoCount4)) {
			return false;
		}

		return true;
	}

}

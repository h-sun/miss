package edu.nju.ics.miss.bottom.device.chassis;

import java.io.IOException;

import edu.nju.ics.miss.bottom.device.Chassis;
import edu.nju.ics.miss.bottom.device.DeviceException;
import edu.nju.ics.miss.bottom.device.ModelParser;

public class Quadricycle extends Chassis {

	private NICMotor motor1 = NICMotor.S1;

	private NICServo servo1 = NICServo.S1;

	private NICMotor motor2 = NICMotor.S2;

	private NICServo servo2 = NICServo.S2;

	private NICMotor motor3 = NICMotor.S3;

	private NICServo servo3 = NICServo.S3;

	private NICMotor motor4 = NICMotor.S4;

	private NICServo servo4 = NICServo.S4;

	private float timeFactor = 6.0f / 9;

	private double turnFactor = 3.1415926 * 141.5 / 180;

	private double distanceWalkedFactor = 3.1415926f * 40 / 180;

	private final int action_forward = 1;

	private final int action_null = 0;

	private final int action_backward = -1;

	private int currentAction = 0;

	private int requiredDistance = -1;

	private int servo1MiddleAngle = 880;

	private int servo2MiddleAngle = 910;

	private int servo3MiddleAngle = 920;

	private int servo4MiddleAngle = 900;

	private int totalAdjustAngle = 0;

	private boolean isTurning = false;

	public Quadricycle() {
		try {
			servo1MiddleAngle = ModelParser.getInstance()
					.getMiddleAngleParams()[0];
			servo2MiddleAngle = ModelParser.getInstance()
					.getMiddleAngleParams()[1];
			servo3MiddleAngle = ModelParser.getInstance()
					.getMiddleAngleParams()[2];
			servo4MiddleAngle = ModelParser.getInstance()
					.getMiddleAngleParams()[3];
		} catch (IOException e) {
			servo1MiddleAngle = 900;
			servo2MiddleAngle = 900;
			servo3MiddleAngle = 900;
			servo4MiddleAngle = 900;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#forward(int, boolean)
	 */
	@Override
	public void forward(int distance, boolean immediateReturn)
			throws DeviceException {
		currentAction = action_forward;
		requiredDistance = distance;

		motor1.resetTachoCount();
		motor2.resetTachoCount();
		motor3.resetTachoCount();
		motor4.resetTachoCount();

		walk(action_forward, distance);

		if (immediateReturn) {
			return;
		}
		while (true) {
			if (!isRunning()) {
				break;
			}
		}
	}

	private void walk(int direction, int distance) throws DeviceException {
		if (0 < distance && distance <= 5) {
			return;
		}

		int a1 = servo1.getAngle();
		int a2 = servo2.getAngle();
		int a3 = servo3.getAngle();
		int a4 = servo4.getAngle();

		NICServo.setAllServosAngle(servo1MiddleAngle, servo2MiddleAngle,
				servo3MiddleAngle, servo4MiddleAngle);

		waitForServoTurning(new int[] { servo1MiddleAngle, servo2MiddleAngle,
				servo3MiddleAngle, servo4MiddleAngle }, new int[] { a1, a2, a3,
				a4 });

		int power, speed;
		if (direction == action_forward) {
//			power = this.power;
			power = 40;
			speed = this.speed;
		} else {
//			power = -this.power;
			power = -40;
			speed = -this.speed;
		}

		if (isPower) {
			NICMotor.groupPowerAndAngleCtrl(new int[] { power, distance,
					servo1MiddleAngle }, new int[] { power, distance,
					servo2MiddleAngle }, new int[] { power, distance,
					servo3MiddleAngle }, new int[] { power, distance,
					servo4MiddleAngle });
		} else {
			NICMotor.groupSpeedAndAngleCtrl(new int[] { speed, distance,
					servo1MiddleAngle }, new int[] { speed, distance,
					servo2MiddleAngle }, new int[] { speed, distance,
					servo3MiddleAngle }, new int[] { speed, distance,
					servo4MiddleAngle });
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#backward(int, boolean)
	 */
	@Override
	public void backward(int distance, boolean immediateReturn)
			throws DeviceException {
		currentAction = action_backward;
		requiredDistance = distance;

		motor1.resetTachoCount();
		motor2.resetTachoCount();
		motor3.resetTachoCount();
		motor4.resetTachoCount();

		walk(action_backward, distance);

		if (immediateReturn) {
			return;
		}
		while (true) {
			if (!isRunning()) {
				break;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#stop()
	 */
	@Override
	public void brake() throws DeviceException {
		while (isTurning) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		int a1 = servo1.getAngle();
		int a2 = servo2.getAngle();
		int a3 = servo3.getAngle();
		int a4 = servo4.getAngle();

		NICMotor.groupPowerAndAngleCtrl(new int[] { 0, 0, a1 }, new int[] { 0,
				0, a2 }, new int[] { 0, 0, a3 }, new int[] { 0, 0, a4 });
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#turn(double)
	 */
	@Override
	synchronized public void turn(double angle) throws DeviceException {
		int distance = (int) (turnFactor * angle);

		if (Math.abs(distance) < 3) {
			return;
		}

		int walkedDistance = 0;

		boolean isRunning = false;

		// 判断小车当前是否在运行
		if (isRunning()) {
			isRunning = true;
			if (currentAction != 0) {
				walkedDistance = getDistanceWalked();
			}
			brake();
		}

		isTurning = true;

		int power, speed;
		if (distance > 0) {
			power = this.power;
			speed = this.speed;
		} else {
			distance = -distance;
			power = -this.power;
			speed = -this.speed;
		}

		int a1 = servo1.getAngle();
		int a2 = servo2.getAngle();
		int a3 = servo3.getAngle();
		int a4 = servo4.getAngle();

		NICServo.setAllServosAngle(1350, 450, 450, 1350);

		int sleepTime = (int) (calculateBiggestDifference(
				new int[] { a1, 1350 }, new int[] { a2, 450 }, new int[] { a3,
						450 }, new int[] { a4, 1350 }) * timeFactor);
		delay(sleepTime);

		if (isPower) {
			NICMotor.groupPowerAndAngleCtrl(
					new int[] { power, distance, 1350 }, new int[] { power,
							distance, 450 },
					new int[] { -power, distance, 450 }, new int[] { -power,
							distance, 1350 });
		} else {
			NICMotor.groupSpeedAndAngleCtrl(
					new int[] { speed, distance, 1350 }, new int[] { speed,
							distance, 450 },
					new int[] { -speed, distance, 450 }, new int[] { -speed,
							distance, 1350 });
		}

		while (true) {
			if (!isRunning()) {
				break;
			}
		}

		NICServo.setAllServosAngle(a1, a2, a3, a4);
		delay(sleepTime);

		isTurning = false;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#fineTuning(double)
	 */
	@Override
	public void fineTuning(double angle) throws DeviceException {
		angle = 10 * angle;

		totalAdjustAngle += angle;
		if (totalAdjustAngle > 50) {
			angle = 50 - totalAdjustAngle + angle;
			totalAdjustAngle = 50;
		} else if (totalAdjustAngle < -50) {
			angle = -50 - totalAdjustAngle + angle;
			totalAdjustAngle = -50;
		}

		servo1MiddleAngle += angle;
		servo2MiddleAngle += angle;
		servo3MiddleAngle += angle;
		servo4MiddleAngle += angle;

		NICServo.setAllServosAngle(servo1MiddleAngle, servo2MiddleAngle,
				servo3MiddleAngle, servo4MiddleAngle);
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
		turn(tAngle);
		fineTuning(ftAngle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#getDistanceWalked()
	 */
	@Override
	public int getDistanceWalked() {
		int tc = motor1.getTachoCount();

		return (int) (tc * distanceWalkedFactor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#isRunning()
	 */
	@Override
	public boolean isRunning() {
		int tc11 = motor1.getTachoCount();
		int tc21 = motor2.getTachoCount();
		int tc31 = motor3.getTachoCount();
		int tc41 = motor4.getTachoCount();
		delay(50);
		int tc12 = motor1.getTachoCount();
		int tc22 = motor2.getTachoCount();
		int tc32 = motor3.getTachoCount();
		int tc42 = motor4.getTachoCount();

		if (tc11 == tc12 && tc21 == tc22 && tc31 == tc32 && tc41 == tc42) {
			return false;
		}

		return true;
	}

	private void waitForServoTurning(int[] newAngles, int[] angles) {

		int tmp = 0;
		for (int angle : angles) {
			for (int na : newAngles) {
				int a = Math.abs(na - angle);
				tmp = tmp < a ? a : tmp;
			}
		}

		int sleepTime = (int) (tmp * timeFactor);

		delay(sleepTime);

	}

	private int calculateBiggestDifference(int[]... params) {

		int maxDiff = 0;
		for (int[] param : params) {
			int d = Math.abs(param[0] - param[1]);
			maxDiff = maxDiff < d ? d : maxDiff;
		}

		return maxDiff;
	}

	/**
	 * 线程暂停
	 * 
	 * @param millisecond
	 *            毫秒
	 */
	private void delay(int millisecond) {
		if (millisecond <= 0) {
			return;
		}
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			return;
		}
	}

}

package edu.nju.ics.miss.bottom.device.chassis;

import edu.nju.ics.miss.bottom.device.Chassis;
import edu.nju.ics.miss.bottom.device.DeviceException;

public class Tricycle extends Chassis {

	/** 三轮车主动转向轮电机 */
	private NICMotor motor = NICMotor.S1;

	/** 三轮车主动转向轮舵机 */
	private NICServo servo = NICServo.S1;

	private float timeFactor = 5.5f / 9;

	private double turnFactor = 3.14159265 * 120 / 180;

	private double distanceWalkedFactor = 3.14159265 * 40 / 180;

	private int minMiddleAngle = 800;

	private int maxMiddleAngle = 830;

	private int middleAngle = 815;

	private final int action_forward = 1;

	private final int action_null = 0;

	private final int action_backward = -1;

	private int currentAction = 0;

	private int requiredDistance = -1;

	private boolean isTurning = false;

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

		motor.resetTachoCount();

		walk(action_forward, distance);

		if (immediateReturn) {
			return;
		}

		while (true) {
			if (!isRunning()) {
				break;
			}
		}

		return;
	}

	private void walk(int direction, int distance) throws DeviceException {
		if (0 < distance && distance <= 5) {
			return;
		}

		int oldAngle = servo.getAngle();
		if (oldAngle != middleAngle) {
			servo.setAngle(middleAngle);
			waitForServoTurning(middleAngle, oldAngle);
		}

		int power, speed;
		if (direction == action_forward) {
//			power = this.power;
			power = 40;
			speed = this.speed;
		} else {
//			power = -this.power;
			power = 40;
			speed = -this.speed;
		}

		if (isPower) {
			motor.powerAndAngleCtrl(power, distance, middleAngle);
		} else {
			motor.speedAndAngleCtrl(speed, distance, middleAngle);
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

		motor.resetTachoCount();

		walk(action_backward, distance);

		if (immediateReturn) {
			return;
		}

		while (true) {
			if (!isRunning()) {
				break;
			}
		}

		return;

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

		int angle = servo.getAngle();
		motor.powerAndAngleCtrl(0, 0, angle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#turning(double)
	 */
	@Override
	synchronized public void turn(double angle) throws DeviceException {
		int distance = (int) (angle * turnFactor);
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

		int targetAngle;
		if (distance > 0) {
			// 右转
			targetAngle = 1800;
		} else {
			// 左转
			targetAngle = 0;
			distance = -distance;
		}

		isTurning = true;

		int oldAngle = servo.getAngle();
		servo.setAngle(targetAngle);

		// 等待舵机转完
		waitForServoTurning(targetAngle, oldAngle);

		if (isPower) {
			motor.powerAndAngleCtrl(power, distance, targetAngle);
		} else {
			motor.speedAndAngleCtrl(speed, distance, targetAngle);
		}

		while (true) {
			if (!isRunning()) {
				break;
			}
		}

		servo.setAngle(oldAngle);
		waitForServoTurning(oldAngle, targetAngle);

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
		middleAngle += angle * 10;
		if (middleAngle > maxMiddleAngle) {
			middleAngle = maxMiddleAngle;
		} else if (middleAngle < minMiddleAngle) {
			middleAngle = minMiddleAngle;
		}

		servo.setAngle(middleAngle);
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
		int tc = motor.getTachoCount();

		return (int) (tc * distanceWalkedFactor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.devices.Car#isRunning()
	 */
	@Override
	public boolean isRunning() {
		int tc1 = motor.getTachoCount();
		delay(50);
		int tc2 = motor.getTachoCount();

		if (tc1 == tc2) {
			return false;
		}

		return true;
	}

	/**
	 * 等待舵机从oldAngle转到newAngle角度
	 * 
	 * @param newAngle
	 *            要转到的角度
	 * 
	 * @param oldAngle
	 *            当前角度
	 */
	private void waitForServoTurning(int newAngle, int oldAngle) {
		int sleepTime = (int) (Math.abs(newAngle - oldAngle) * timeFactor);

		if (sleepTime > 0) {
			delay(sleepTime);
		}

	}

	/**
	 * 线程暂停
	 * 
	 * @param millisecond
	 *            毫秒
	 */
	private void delay(int millisecond) {
		try {
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			return;
		}
	}
}

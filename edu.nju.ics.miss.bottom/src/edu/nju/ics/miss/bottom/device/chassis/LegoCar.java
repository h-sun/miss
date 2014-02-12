package edu.nju.ics.miss.bottom.device.chassis;

import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import edu.nju.ics.miss.bottom.device.Chassis;
import edu.nju.ics.miss.bottom.device.DeviceException;
import edu.nju.ics.miss.bottom.device.ModelParser;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-3-6 下午9:32:18<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class LegoCar extends Chassis {

	private DifferentialPilot legoPilot;

	private double wheelDiameter = 73;

	private double trackWidth = 190;

	private double param1 = Math.PI * wheelDiameter / 360.0;

	private final int action_forward = 1;

	private final int action_null = 0;

	private final int action_backward = -1;

	private int currentAction = 0;

	private int requiredDistance = -1;

	private double middleAngle = 0.0;

	private boolean isTurning = false;

	public LegoCar() {
		legoPilot = new DifferentialPilot(wheelDiameter, trackWidth, Motor.B,
				Motor.C, true);
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

		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();

		if (distance == 0) {
			walk(action_forward, Double.POSITIVE_INFINITY, immediateReturn);
		} else {
			walk(action_forward, distance, immediateReturn);
		}
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

		Motor.B.resetTachoCount();
		Motor.C.resetTachoCount();

		if (distance == 0) {
			walk(action_forward, Double.NEGATIVE_INFINITY, immediateReturn);
		} else {
			walk(action_forward, -distance, immediateReturn);
		}
	}

	private void walk(int direction, double distance, boolean immediateReturn)
			throws DeviceException {

		onlyTurn(middleAngle);

		legoPilot.setTravelSpeed(100);
		legoPilot.travel(distance, immediateReturn);
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

		legoPilot.stop();
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
			double remainderDistance;
			if (requiredDistance == 0) {
				if (currentAction == action_forward) {
					remainderDistance = Double.POSITIVE_INFINITY;
				} else {
					remainderDistance = Double.NEGATIVE_INFINITY;
				}
			} else {
				remainderDistance = requiredDistance - walkedDistance;
				if (remainderDistance <= 0) {
					return;
				}
				if (currentAction == action_backward) {
					remainderDistance = -remainderDistance;
				}
			}

			// LCD.clear();
			//
			// LCD.drawString(currentAction + "", 0, 0);
			// LCD.drawString("" + requiredDistance, 0, 1);
			// LCD.drawString("" + walkedDistance, 0, 2);
			// LCD.drawString("" + remainderDistance, 0, 3);
			// LCD.drawString("" + Double.POSITIVE_INFINITY, 0, 4);
			// LCD.drawString("" + Double.NEGATIVE_INFINITY, 0, 5);
			//
			// Button.waitForAnyPress();

			walk(currentAction, remainderDistance, true);
		}
	}

	private void onlyTurn(double angle) throws DeviceException {
		if (Math.abs(angle) <= 0.5) {
			return;
		}

		isTurning = true;

		legoPilot.setRotateSpeed(150);

		// 1.3333333=12/9
		angle = angle * 1.3333333;

		legoPilot.rotate(-angle, false);

		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
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
		middleAngle += angle;
		if (middleAngle > 5) {
			middleAngle = 5;
		} else if (middleAngle < -5) {
			middleAngle = -5;
		}
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
		if (middleAngle > 2) {
			middleAngle = 2;
		} else if (middleAngle < -2) {
			middleAngle = -2;
		}

		LCD.clear();
		LCD.drawString("" + tAngle, 0, 0);
		LCD.drawString("" + ftAngle, 0, 1);
		LCD.drawString("" + middleAngle, 0, 2);
		// Button.waitForAnyPress();

		// double angle = tAngle + ftAngle;
		if (tAngle > 2.5) {
			tAngle = 2.5;
		} else if (tAngle < -2.5) {
			tAngle = -2.5;
		}
		turn(tAngle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#getDistanceWalked()
	 */
	@Override
	public int getDistanceWalked() {
		int tachoCount = Math.abs(Motor.B.getTachoCount())
				+ Math.abs(Motor.C.getTachoCount());
		tachoCount /= 2;

		return (int) (tachoCount * param1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.nju.ics.miss.bottom.device.Chassis#isRunning()
	 */
	@Override
	public boolean isRunning() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int rotationSpeedC = Motor.C.getRotationSpeed();
		int rotationSpeedB = Motor.B.getRotationSpeed();
		if (rotationSpeedC == -1 && rotationSpeedB == -1) {
			return false;
		}

		return true;
	}

}

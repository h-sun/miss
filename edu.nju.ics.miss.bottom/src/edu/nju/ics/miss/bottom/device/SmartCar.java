package edu.nju.ics.miss.bottom.device;

import java.io.IOException;

import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.chassis.LegoCar;
import edu.nju.ics.miss.bottom.device.chassis.Quadricycle;
import edu.nju.ics.miss.bottom.device.chassis.Track;
import edu.nju.ics.miss.bottom.device.chassis.Tricycle;
import edu.nju.ics.miss.bottom.device.sensor.UltrasonicSensor_2440;
import edu.nju.ics.miss.bottom.device.sensor.UltrasonicSensor_LegoCar;
import edu.nju.ics.miss.bottom.device.sensor.UltrasonicSensor_Track;

public class SmartCar {

	private static final int CAR_TYPE_TRACK = PlatformParser.PLATFORM_TYPE_TRACK;

	private static final int CAR_TYPE_TRICYCLE = PlatformParser.PLATFORM_TYPE_TRICYCLE;

	private static final int CAR_TYPE_QUADRICYCLE = PlatformParser.PLATFORM_TYPE_QUADRICYCLE;

	private static final int CAR_TYPE_LEGO = PlatformParser.PLATFORM_TYPE_LEGO;

	private Chassis chassis = null;

	private UltrasonicSensorGroup ultrasonicSensor = null;

	/** 小车类型 */
	private int carType;

	private double distanceBetweenSensors;

	private double angleCorrectionFactor = 1.0;

	/**
	 * int[0]:address<br>
	 * int[1]:left sensor number<br>
	 * int[2]:right sensor number<br>
	 * if int[i]==-1, it is invalid!
	 */
	private int[] frontSensorParams;

	/**
	 * int[0]:address<br>
	 * int[1]:left sensor number<br>
	 * int[2]:right sensor number<br>
	 * if int[i]==-1, it is invalid!
	 */
	private int[] rightSensorParams;

	/**
	 * int[0]:address<br>
	 * int[1]:left sensor number<br>
	 * int[2]:right sensor number<br>
	 * if int[i]==-1, it is invalid!
	 */
	private int[] backSensorParams;

	/**
	 * int[0]:address<br>
	 * int[1]:left sensor number<br>
	 * int[2]:right sensor number<br>
	 * if int[i]==-1, it is invalid!
	 */
	private int[] leftSensorParams;

	private static SmartCar instance = null;

	private SmartCar() {

		carType = PlatformParser.getPlatformType();
		distanceBetweenSensors = PlatformParser.getDistanceBetweenSensors();

		try {
			ModelParser mp = ModelParser.getInstance();
			frontSensorParams = mp.getFrontParams();
			rightSensorParams = mp.getRightParams();
			backSensorParams = mp.getBackParams();
			leftSensorParams = mp.getLeftParams();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (carType) {
		case CAR_TYPE_TRACK:
			chassis = new Track();
			ultrasonicSensor = new UltrasonicSensor_Track();
			angleCorrectionFactor = 2.3;
			break;
		case CAR_TYPE_TRICYCLE:
			chassis = new Tricycle();
			ultrasonicSensor = new UltrasonicSensor_2440();
			break;
		case CAR_TYPE_QUADRICYCLE:
			chassis = new Quadricycle();
			ultrasonicSensor = new UltrasonicSensor_2440();
			break;
		case CAR_TYPE_LEGO:
			chassis = new LegoCar();
			ultrasonicSensor = new UltrasonicSensor_LegoCar();
			break;
		default:
			break;
		}

	}

	public static SmartCar getInstance() {
		if (instance == null) {
			instance = new SmartCar();
		}

		return instance;
	}

	public void setModel(boolean isPower) {
		chassis.setModel(isPower);
	}

	public void forward(int distance, boolean immediateReturn)
			throws DeviceException {
		chassis.forward(distance, immediateReturn);
	}

	public void backward(int distance, boolean immediateReturn)
			throws DeviceException {
		chassis.backward(distance, immediateReturn);
	}

	/**
	 * 刹车
	 * 
	 * @throws DeviceException
	 */
	public void brake() throws DeviceException {
		chassis.brake();
	}

	/**
	 * 
	 * 小车转弯操作<br>
	 * 进行转弯操作时，小车会先停止，再转弯
	 * 
	 * @param angle
	 *            转弯角度 >0:向右; <0:向左
	 * @throws DeviceException
	 */
	public void rotateLeft(double angle) throws DeviceException {
		chassis.turn(-angle);
	}

	public void rotateRight(double angle) throws DeviceException {
		chassis.turn(angle);
	}

	public void turn(double angle) throws DeviceException {
		chassis.turn(angle);
	}

	/**
	 * 小车在前进或后退时，进行的左右微调<br>
	 * 进行微调时，小车不会停止下来，是一边运行一边微调
	 * 
	 * @param angle
	 *            微调角度 >0:向右; <0:向左
	 * @throws DeviceException
	 */
	public void fineTuning(double angle) throws DeviceException {
		chassis.fineTuning(angle);
	}

	public void turnAndFineTuning(double tAngle, double ftAngle)
			throws DeviceException {
		chassis.turnAndFineTuning(tAngle, ftAngle);
	}

	public int getDistanceWalked() {
		return chassis.getDistanceWalked();
	}

	public boolean isRunning() {
		return chassis.isRunning();
	}

	/**
	 * obtain all the ultrasonic datas
	 * 
	 * @param ultrasonicDatas
	 */
	public void obtainUltrasonicDatas(UltrasonicDatas ultrasonicDatas) {
		// LCD.drawString("111\n", 0, 0);
		ultrasonicSensor.obtainUltrasonicDatas(ultrasonicDatas);
		// LCD.drawString("222\n", 0, 0);
	}

	/**
	 * @return the chassis
	 */
	public Chassis getChassis() {
		return chassis;
	}

	/**
	 * @return the ultrasonicSensor
	 */
	public UltrasonicSensorGroup getUltrasonicSensor() {
		return ultrasonicSensor;
	}

	/**
	 * @return the carType
	 */
	public int getCarType() {
		return carType;
	}

	/**
	 * @return the distanceBetweenSensors
	 */
	public double getDistanceBetweenSensors() {
		return distanceBetweenSensors;
	}

	/**
	 * @return the frontSensorParams
	 */
	public int[] getFrontSensorParams() {
		return frontSensorParams;
	}

	/**
	 * @return the rightSensorParams
	 */
	public int[] getRightSensorParams() {
		return rightSensorParams;
	}

	/**
	 * @return the backSensorParams
	 */
	public int[] getBackSensorParams() {
		return backSensorParams;
	}

	/**
	 * @return the leftSensorParams
	 */
	public int[] getLeftSensorParams() {
		return leftSensorParams;
	}

	/**
	 * @return the angleCorrectionFactor
	 */
	public double getAngleCorrectionFactor() {
		return angleCorrectionFactor;
	}

	/**
	 * @param angleCorrectionFactor
	 *            the angleCorrectionFactor to set
	 */
	public void setAngleCorrectionFactor(double angleCorrectionFactor) {
		this.angleCorrectionFactor = angleCorrectionFactor;
	}

}

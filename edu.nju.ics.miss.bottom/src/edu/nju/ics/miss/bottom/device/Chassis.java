package edu.nju.ics.miss.bottom.device;

public abstract class Chassis {
	/**
	 * 功率
	 */
	protected int power = 50;

	/**
	 * 速度
	 */
	protected int speed = 250;

	/**
	 * 是否为调功模式，否的话为调速模式
	 */
	protected boolean isPower = true;

	/**
	 * 是否为调功模式，否的话为调功模式
	 * 
	 * @param isPower
	 */
	public void setModel(boolean isPower) {
		this.isPower = isPower;
	}

	/**
	 * 小车前进
	 * 
	 * @param distance
	 *            前进距离，单位mm 0表示不停前进
	 * @param immediateReturn
	 *            是否立即返回
	 * @throws DeviceException
	 */
	public abstract void forward(int distance, boolean immediateReturn)
			throws DeviceException;

	/**
	 * 小车后�?
	 * 
	 * @param distance
	 *            后退距离，单位mm 0表示不停后退
	 * @param immediateReturn
	 *            是否立即返回
	 * @throws DeviceException
	 */
	public abstract void backward(int distance, boolean immediateReturn)
			throws DeviceException;

	/**
	 * 刹车
	 * 
	 * @throws DeviceException
	 */
	public abstract void brake() throws DeviceException;

	/**
	 * 
	 * 小车转弯操作<br>
	 * 进行转弯操作时，小车会先停止，再转弯
	 * 
	 * @param angle
	 *            转弯角度 >0:向右; <0:向左
	 * @throws DeviceException
	 */
	public abstract void turn(double angle) throws DeviceException;

	/**
	 * 小车在前进或后退时，进行的左右微调<br>
	 * 进行微调时，小车不会停止下来，是一边运行一边微调
	 * 
	 * @param angle
	 *            微调角度 >0:向右; <0:向左
	 * @throws DeviceException
	 */
	public abstract void fineTuning(double angle) throws DeviceException;

	public abstract void turnAndFineTuning(double tAngle, double ftAngle)
			throws DeviceException;

	/**
	 * 小车已行走的距离，单位mm<br>
	 * >0为前进，<0为后退
	 * 
	 * @return 行走的距离
	 * @throws DeviceException
	 */
	public abstract int getDistanceWalked();

	/**
	 * 判断小车是否在运行
	 * 
	 * @return 是否运行
	 * @throws DeviceException
	 */
	public abstract boolean isRunning();
}

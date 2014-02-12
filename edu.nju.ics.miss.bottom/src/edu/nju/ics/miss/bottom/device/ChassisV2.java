package edu.nju.ics.miss.bottom.device;

public abstract class ChassisV2 {
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
	 * 设置功率<br>
	 * 范围:1-100为刹车，>100为惰行
	 * 
	 * @param power
	 *            功率
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * 设置速度<br>
	 * 范围:200-300，单位mm/s
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

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
	 * 左转，转弯之后，轮子恢复到转弯前的角度
	 * 
	 * @param angle
	 *            左转角度
	 * @throws DeviceException
	 */
	public abstract void rotateLeft(double angle) throws DeviceException;

	/**
	 * 右转，转弯之后，轮子恢复到转弯前的角度
	 * 
	 * @param angle
	 *            右转角度
	 * @throws DeviceException
	 */
	public abstract void rotateRight(double angle) throws DeviceException;

	/**
	 * 小车已行走的距离，单位mm<br>
	 * >0为前进，<0为后退
	 * 
	 * @return 行走的距离
	 * @throws DeviceException
	 */
	public abstract int getDistanceWalked() throws DeviceException;

	/**
	 * 重置小车：车轮恢复至初始状态，已行走距离清零
	 * 
	 * @param angle
	 *            小车在原先的初始状态基础上的偏移角度,<br>
	 *            >0,向右再偏移|angle|角度<br>
	 *            <0,向左再偏移|angle|角度
	 * @throws DeviceException
	 */
	public abstract void reset(double angle) throws DeviceException;

	/**
	 * 判断小车是否在运行
	 * 
	 * @return 是否运行
	 * @throws DeviceException
	 */
	public abstract boolean isRunning() throws DeviceException;
}

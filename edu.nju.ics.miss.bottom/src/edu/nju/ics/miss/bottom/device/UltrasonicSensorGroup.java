package edu.nju.ics.miss.bottom.device;

import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;

public abstract class UltrasonicSensorGroup {

	/**
	 * obtain all the ultrasonic datas 
	 * 
	 * @param ultrasonicDatas
	 */
	public abstract void obtainUltrasonicDatas(UltrasonicDatas ultrasonicDatas);

}

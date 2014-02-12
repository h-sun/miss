package edu.nju.ics.miss.bottom.device.sensor;

import java.io.IOException;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.ModelParser;
import edu.nju.ics.miss.bottom.device.UltrasonicSensorGroup;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-3-6 下午9:58:06<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class UltrasonicSensor_LegoCar extends UltrasonicSensorGroup {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.nju.ics.miss.bottom.device.UltrasonicSensor#obtainUltrasonicDatas
	 * (edu.nju.ics.miss.bottom.datatype.UltrasonicDatas)
	 */
	@Override
	synchronized public void obtainUltrasonicDatas(
			UltrasonicDatas ultrasonicDatas) {
		ultrasonicDatas.clear();
		ModelParser parser = null;
		try {
			parser = ModelParser.getInstance();
		} catch (IOException e) {
			return;
		}

		// 处理前方数据
		int[] frontParams = parser.getFrontParams();
		ultrasonicDatas.setFrontDatas(getUltrasonicDatas(frontParams));

		// 处理右方数据
		int[] rightParams = parser.getRightParams();
		ultrasonicDatas.setRightDatas(getUltrasonicDatas(rightParams));

		// 处理后方数据
		int[] backParams = parser.getBackParams();
		ultrasonicDatas.setBackDatas(getUltrasonicDatas(backParams));

		// 处理左方数据
		int[] leftParams = parser.getLeftParams();
		ultrasonicDatas.setLeftDatas(getUltrasonicDatas(leftParams));
	}

	private int[] getUltrasonicDatas(int params[]) {
		int datas[] = new int[] { -1, -1 };

		if (params[1] != -1) {
			UltrasonicSensor sensor = getLejosUltrasonicSensorById(params[1]);
			datas[0] = getDistance(sensor);
		}
		if (params[2] != -1) {
			UltrasonicSensor sensor = getLejosUltrasonicSensorById(params[2]);
			datas[1] = getDistance(sensor);
		}

		return datas;
	}

	private UltrasonicSensor getLejosUltrasonicSensorById(int id) {
		UltrasonicSensor sensor = null;
		switch (id) {
		case 1:
			sensor = new UltrasonicSensor(SensorPort.S1);
			break;
		case 2:
			sensor = new UltrasonicSensor(SensorPort.S2);
			break;
		case 3:
			sensor = new UltrasonicSensor(SensorPort.S3);
			break;
		case 4:
			sensor = new UltrasonicSensor(SensorPort.S4);
			break;
		}
		return sensor;
	}

	private int getDistance(UltrasonicSensor sensor) {
		if (sensor == null) {
			return -1;
		}
		double d1 = sensor.getDistance();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		double d2 = sensor.getDistance();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		double d3 = sensor.getDistance();

		double v1 = Math.abs(d1 - d2);
		double v2 = Math.abs(d1 - d3);

		double distance;

		if (v1 < 10 && v2 < 10) {
			distance = (d1 + d2 + d3) / 3;
		} else if (v1 < 10) {
			distance = (d1 + d2) / 2;
		} else if (v2 < 10) {
			distance = (d1 + d3) / 2;
		} else if (Math.abs(v1 - v2) < 10) {
			distance = (d2 + d3) / 2;
		} else {
			distance = 255;
		}

		return (int) (distance * 10);
	}

	@SuppressWarnings("unused")
	private int getDistance2(UltrasonicSensor sensor) {
		sensor.setMode(UltrasonicSensor.MODE_PING);
		int n = 3;
		int[] datas = new int[n];
		sensor.getDistances(datas);

		int k = 0, sum = 0;
		for (int i = 0; i < n; i++) {
			if (datas[i] > 0 && datas[i] <= 200) {
				sum += datas[i];
				k++;
			}
		}
		if (k == 0) {
			return 2550;
		}

		return (sum / k) * 10;
	}
}

package edu.nju.ics.miss.bottom.device.sensor;

import java.io.IOException;

import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.ModelParser;
import edu.nju.ics.miss.bottom.device.UltrasonicSensorGroup;

public class UltrasonicSensor_2440 extends UltrasonicSensorGroup {

	private int readAmount = 7;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.nju.ics.miss.bottom.device.UltrasonicSensor#obtainUltrasonicDatas
	 * (org.nju.ics.miss.bottom.datatype.UltrasonicDatas)
	 */
	@Override
	public void obtainUltrasonicDatas(UltrasonicDatas ultrasonicDatas) {
		// LCD.drawString("start\n", 0, 0);
		ultrasonicDatas.clear();
		ModelParser parser = null;
		try {
			parser = ModelParser.getInstance();
		} catch (IOException e) {
			return;
		}

		// 获取1号地址数据
		UltrasonicSensorUnit us = UltrasonicSensorUnit.S1;
		int[] datas1 = us.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);

		// 获取2号地址数据
		us = UltrasonicSensorUnit.S2;
		int[] datas2 = us.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);

		// 处理前方数据
		int[] frontParams = parser.getFrontParams();
		int ldata = -1, rdata = -1;
		switch (frontParams[0]) {
		case 1:
			if (frontParams[1] != -1) {
				ldata = getValue(datas1, readAmount * (frontParams[1] - 1),
						readAmount * (frontParams[1] - 1) + readAmount - 1);
			}
			if (frontParams[2] != -1) {
				rdata = getValue(datas1, readAmount * (frontParams[2] - 1),
						readAmount * (frontParams[2] - 1) + readAmount - 1);
			}
			break;
		case 2:
			if (frontParams[1] != -1) {
				ldata = getValue(datas2, readAmount * (frontParams[1] - 1),
						readAmount * (frontParams[1] - 1) + readAmount - 1);
			}
			if (frontParams[2] != -1) {
				rdata = getValue(datas2, readAmount * (frontParams[2] - 1),
						readAmount * (frontParams[2] - 1) + readAmount - 1);
			}
			break;
		}
		ultrasonicDatas.setFrontDatas(new int[] { ldata, rdata });

		// 处理右方数据
		ldata = -1;
		rdata = -1;
		int[] rightParams = parser.getRightParams();
		switch (rightParams[0]) {
		case 1:
			if (rightParams[1] != -1) {
				ldata = getValue(datas1, readAmount * (rightParams[1] - 1),
						readAmount * (rightParams[1] - 1) + readAmount - 1);
			}
			if (rightParams[2] != -1) {
				rdata = getValue(datas1, readAmount * (rightParams[2] - 1),
						readAmount * (rightParams[2] - 1) + readAmount - 1);
			}
			break;
		case 2:
			if (rightParams[1] != -1) {
				ldata = getValue(datas2, readAmount * (rightParams[1] - 1),
						readAmount * (rightParams[1] - 1) + readAmount - 1);
			}
			if (rightParams[2] != -1) {
				rdata = getValue(datas2, readAmount * (rightParams[2] - 1),
						readAmount * (rightParams[2] - 1) + readAmount - 1);
			}
			break;
		}
		ultrasonicDatas.setRightDatas(new int[] { ldata, rdata });

		// 处理后方数据
		ldata = -1;
		rdata = -1;
		int[] backParams = parser.getBackParams();
		switch (backParams[0]) {
		case 1:
			if (backParams[1] != -1) {
				ldata = getValue(datas1, readAmount * (backParams[1] - 1),
						readAmount * (backParams[1] - 1) + readAmount - 1);
			}
			if (backParams[2] != -1) {
				rdata = getValue(datas1, readAmount * (backParams[2] - 1),
						readAmount * (backParams[2] - 1) + readAmount - 1);
			}
			break;
		case 2:
			if (backParams[1] != -1) {
				ldata = getValue(datas2, readAmount * (backParams[1] - 1),
						readAmount * (backParams[1] - 1) + readAmount - 1);
			}
			if (backParams[2] != -1) {
				rdata = getValue(datas2, readAmount * (backParams[2] - 1),
						readAmount * (backParams[2] - 1) + readAmount - 1);
			}
			break;
		}
		ultrasonicDatas.setBackDatas(new int[] { ldata, rdata });

		// 处理左方数据
		ldata = -1;
		rdata = -1;
		int[] leftParams = parser.getLeftParams();
		switch (leftParams[0]) {
		case 1:
			if (leftParams[1] != -1) {
				ldata = getValue(datas1, readAmount * (leftParams[1] - 1),
						readAmount * (leftParams[1] - 1) + readAmount - 1);
			}
			if (leftParams[2] != -1) {
				rdata = getValue(datas1, readAmount * (leftParams[2] - 1),
						readAmount * (leftParams[2] - 1) + readAmount - 1);
			}
			break;
		case 2:
			if (leftParams[1] != -1) {
				ldata = getValue(datas2, readAmount * (leftParams[1] - 1),
						readAmount * (leftParams[1] - 1) + readAmount - 1);
			}
			if (leftParams[2] != -1) {
				rdata = getValue(datas2, readAmount * (leftParams[2] - 1),
						readAmount * (leftParams[2] - 1) + readAmount - 1);
			}
			break;
		}
		ultrasonicDatas.setLeftDatas(new int[] { ldata, rdata });

	}

	/**
	 * 处理数组datas中下标start至end之间的数据，返回处理后的结果
	 * 
	 * @param datas
	 * @param start
	 * @param end
	 * @return
	 */
	private int getValue(int[] datas, int start, int end) {
		int length = end - start + 1;
		int[] tags = new int[length];
		for (int i = 0; i < tags.length; i++) {
			if (datas[start + i] == 65535) {
				tags[i] = 0;
			} else {
				tags[i] = 1;
			}
		}
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == 0) {
				continue;
			}
			int d = datas[start + i];
			for (int j = i + 1; j < tags.length; j++) {
				if (tags[j] == 0) {
					continue;
				}
				if (Math.abs(d - datas[start + j]) <= 50) {
					tags[i]++;
					tags[j] = 0;
					datas[start + i] += datas[start + j];
				}
			}
		}
		int index = -1;
		int num = 0;
		for (int i = 0; i < tags.length; i++) {
			if (tags[i] == 0) {
				continue;
			}
			if (num == tags[i]) {
				datas[start + index] += datas[start + i];
				tags[index] += tags[i];
			}
			if (num < tags[i]) {
				num = tags[i];
				index = i;
			}
		}

		if (index >= 0) {
			return datas[start + index] / tags[index];
		}
		return 65535;
	}

}

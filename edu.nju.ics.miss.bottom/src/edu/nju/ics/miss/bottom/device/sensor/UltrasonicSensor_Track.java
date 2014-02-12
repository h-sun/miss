package edu.nju.ics.miss.bottom.device.sensor;

import java.io.IOException;

import edu.nju.ics.miss.bottom.datatype.UltrasonicDatas;
import edu.nju.ics.miss.bottom.device.ModelParser;
import edu.nju.ics.miss.bottom.device.UltrasonicSensorGroup;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-2-25 下午9:39:04<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class UltrasonicSensor_Track extends UltrasonicSensorGroup {

	private int readAmount = 7;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.nju.ics.miss.bottom.device.UltrasonicSensor#obtainUltrasonicDatas
	 * (edu.nju.ics.miss.bottom.datatype.UltrasonicDatas)
	 */
	@Override
	public void obtainUltrasonicDatas(UltrasonicDatas ultrasonicDatas) {
		ultrasonicDatas.clear();
		ModelParser parser = null;
		try {
			parser = ModelParser.getInstance();
		} catch (IOException e) {
			return;
		}

		// 获取0,1,2,3号地址数据
		int[] datas0 = UltrasonicSensorUnit.S0.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);
		int[] datas1 = UltrasonicSensorUnit.S1.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);
		int[] datas2 = UltrasonicSensorUnit.S2.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);
		int[] datas3 = UltrasonicSensorUnit.S3.readSensorValue(
				UltrasonicSensorUnit.MODE_NOPROC_BACK, readAmount);

		// 处理前方数据
		int[] frontParams = parser.getFrontParams();
		int rd[] = new int[] { -1, -1 };
		switch (frontParams[0]) {
		case 0:
			rd = handleDatas(datas0, frontParams);
			break;
		case 1:
			rd = handleDatas(datas1, frontParams);
			break;
		case 2:
			rd = handleDatas(datas2, frontParams);
			break;
		case 3:
			rd = handleDatas(datas3, frontParams);
			break;
		}
		ultrasonicDatas.setFrontDatas(new int[] { rd[0], rd[1] });

		// 处理右方数据
		int[] rightParams = parser.getRightParams();
		rd = new int[] { -1, -1 };
		switch (rightParams[0]) {
		case 0:
			rd = handleDatas(datas0, rightParams);
			break;
		case 1:
			rd = handleDatas(datas1, rightParams);
			break;
		case 2:
			rd = handleDatas(datas2, rightParams);
			break;
		case 3:
			rd = handleDatas(datas3, rightParams);
			break;
		}
		ultrasonicDatas.setRightDatas(new int[] { rd[0], rd[1] });

		// 处理后方数据
		int[] backParams = parser.getBackParams();
		rd = new int[] { -1, -1 };
		switch (backParams[0]) {
		case 0:
			rd = handleDatas(datas0, backParams);
			break;
		case 1:
			rd = handleDatas(datas1, backParams);
			break;
		case 2:
			rd = handleDatas(datas2, backParams);
			break;
		case 3:
			rd = handleDatas(datas3, backParams);
			break;
		}
		ultrasonicDatas.setBackDatas(new int[] { rd[0], rd[1] });

		// 处理左方数据
		int[] leftParams = parser.getLeftParams();
		rd = new int[] { -1, -1 };
		switch (leftParams[0]) {
		case 0:
			rd = handleDatas(datas0, leftParams);
			break;
		case 1:
			rd = handleDatas(datas1, leftParams);
			break;
		case 2:
			rd = handleDatas(datas2, leftParams);
			break;
		case 3:
			rd = handleDatas(datas3, leftParams);
			break;
		}
		ultrasonicDatas.setLeftDatas(new int[] { rd[0], rd[1] });

	}

	private int[] handleDatas(int[] datas, int[] params) {
		int rd[] = new int[] { -1, -1 };

		if (params[1] != -1) {
			rd[0] = getValue(datas, readAmount * params[1], readAmount
					* params[1] + readAmount - 1);
		}
		if (params[2] != -1) {
			rd[1] = getValue(datas, readAmount * params[2], readAmount
					* params[2] + readAmount - 1);
		}

		return rd;
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

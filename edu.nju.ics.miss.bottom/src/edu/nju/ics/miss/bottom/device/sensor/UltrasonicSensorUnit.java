package edu.nju.ics.miss.bottom.device.sensor;


public class UltrasonicSensorUnit  {

	public static final UltrasonicSensorUnit S0 = new UltrasonicSensorUnit(0);

	public static final UltrasonicSensorUnit S1 = new UltrasonicSensorUnit(1);

	public static final UltrasonicSensorUnit S2 = new UltrasonicSensorUnit(2);

	public static final UltrasonicSensorUnit S3 = new UltrasonicSensorUnit(3);

	public static final int MODE_NOPROC_BACK = 0;

	public static final int MODE_MID_BACK = 1;

	public static final int MODE_MEAN_BACK = 2;

	public static final int MODE_MID_MEAN_BACK = 3;

	private int uPortId;

	private UltrasonicSensorUnit(int id) {
		this.uPortId = id;
	}

	public int[] readSensorValue(int mode, int measureCount) {
		if (mode < 0 || mode > 3 || measureCount < 1 || measureCount > 7) {
			return null;
		}

		return readSensorAllValues(uPortId, mode, measureCount);
	}

    private static native int[] readSensorAllValues(int id,
			int mode, int measureCount);


}

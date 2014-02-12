package edu.nju.ics.miss.bottom.device;

import lejos.nxt.NXT;

public class PlatformParser {
	
	public static final int PLATFORM_TYPE_TRACK = 2;
	
	public static final int PLATFORM_TYPE_TRICYCLE = 3;
	
	public static final int PLATFORM_TYPE_QUADRICYCLE = 4;
	
	public static final int PLATFORM_TYPE_LEGO = 11;
	
	private static int carType = -1;
	
	private static int distanceBetweenSensors = -1;
	
	static{
		int r = NXT.getFirmwareRawVersion();
		switch (r) {
		case 2:
			carType = PLATFORM_TYPE_TRACK;
			distanceBetweenSensors = 176;
			break;
		case 3:
			carType = PLATFORM_TYPE_TRICYCLE;
			distanceBetweenSensors = 168;
			break;
		case 4:
			carType = PLATFORM_TYPE_QUADRICYCLE;
			distanceBetweenSensors = 140;
			break;
		default:
			if (r > 10) {
				carType = PLATFORM_TYPE_LEGO;
				distanceBetweenSensors = 128;
			}
			break;
		}
	}
	
	public static int getPlatformType(){
		return carType;
	}
	
	public static int getDistanceBetweenSensors(){
		return distanceBetweenSensors;
	}
	
}

package edu.nju.ics.miss.bottom.util;

import lejos.nxt.LCD;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-29 上午10:59:03<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class MyLogger {

	public static final int DEBUG = 1;

	public static final int INFO = 2;

	public static final int WARN = 3;

	public static final int ERROR = 4;

	private static final int CLOSE = 10;

	private static int currentLevel = 1;

	private static int previousLevel = 1;

	public static void setLevel(int level) {
		if (level >= DEBUG && level <= ERROR) {
			currentLevel = level;
			previousLevel = level;
		}
	}

	public static int getLevel() {
		return currentLevel;
	}

	public static void open() {
		currentLevel = previousLevel;
	}

	public static void close() {
		previousLevel = currentLevel;
		currentLevel = CLOSE;
	}

	public static void writeLog(int level, String log) {
		if (level >= currentLevel) {
			String prefix;
			switch (level) {
			case DEBUG:
				prefix = "[DEBUG] ";
				break;
			case INFO:
				prefix = "[INFO] ";
				break;
			case ERROR:
				prefix = "[ERROR] ";
				break;
			default:
				prefix = "[" + level + "] ";
				break;
			}
			LCD.drawString(prefix + log + "\n", 0, 0);
		}
	}

	public static void writeLog(int level, String log, int x, int y) {
		if (level >= currentLevel) {
			LCD.drawString(level + " " + log, x, y);
		}
	}

}

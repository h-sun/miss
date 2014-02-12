package edu.nju.ics.miss.bottom.datatype;

import lejos.nxt.LCD;

public class UltrasonicDatas {

	private int[] frontDatas = new int[2];

	private int[] rightDatas = new int[2];

	private int[] backDatas = new int[2];

	private int[] leftDatas = new int[2];

	public void setFrontDatas(int[] datas) {
		for (int i = 0; i < frontDatas.length; i++) {
			frontDatas[i] = datas[i];
		}
	}

	public void setRightDatas(int[] datas) {
		for (int i = 0; i < rightDatas.length; i++) {
			rightDatas[i] = datas[i];
		}
	}

	public void setBackDatas(int[] datas) {
		for (int i = 0; i < backDatas.length; i++) {
			backDatas[i] = datas[i];
		}
	}

	public void setLeftDatas(int[] datas) {
		for (int i = 0; i < leftDatas.length; i++) {
			leftDatas[i] = datas[i];
		}
	}

	public int[] getFrontDatas() {

		return frontDatas;
	}

	public int[] getRightDatas() {

		return rightDatas;
	}

	public int[] getBackDatas() {

		return backDatas;
	}

	public int[] getLeftDatas() {

		return leftDatas;
	}

	public void clear() {
		for (int i = 0; i < frontDatas.length; i++) {
			frontDatas[i] = -1;
		}
		for (int i = 0; i < rightDatas.length; i++) {
			rightDatas[i] = -1;
		}
		for (int i = 0; i < backDatas.length; i++) {
			backDatas[i] = -1;
		}
		for (int i = 0; i < leftDatas.length; i++) {
			leftDatas[i] = -1;
		}
	}

	public void printDatas() {
		LCD.drawString("[FRONT]  L:" + frontDatas[0] + "  R:" + frontDatas[1]
				+ "\n", 0, 0);
		LCD.drawString("[RIGHT]  L:" + rightDatas[0] + "  R:" + rightDatas[1]
				+ "\n", 0, 0);
		LCD.drawString("[BACK]   L:" + backDatas[0] + "  R:" + backDatas[1]
				+ "\n", 0, 0);
		LCD.drawString("[LEFT]   L:" + leftDatas[0] + "  R:" + leftDatas[1]
				+ "\n", 0, 0);
	}

}

package edu.nju.ics.miss.bottom.device;

import java.io.IOException;

import edu.nju.ics.miss.bottom.io.File;
import edu.nju.ics.miss.bottom.io.FileReader;

public class ModelParser {

	private int[] frontParams = new int[] { -1, -1, -1 };

	private int[] rightParams = new int[] { -1, -1, -1 };

	private int[] backParams = new int[] { -1, -1, -1 };

	private int[] leftParams = new int[] { -1, -1, -1 };

	private int[] middleAngleParams = new int[] { 0, 0, 0, 0 };

	private int minDistance = 300;

	private int maxDistance = 1800;

	private double turnRightAngle = 90;

	private double turnLeftAngle = 90;

	private FileReader reader = null;

	private static ModelParser paser = null;

	private ModelParser() throws IOException {
		init();
	}

	public static ModelParser getInstance() throws IOException {
		if (paser == null) {
			paser = new ModelParser();
		}

		return paser;
	}

	private void init() throws IOException {
		File modelFile = new File("model.xml");
		reader = new FileReader(modelFile);
		String lineStr = reader.readLine();
		while (lineStr != null) {
			if (lineStr.equalsIgnoreCase("</model>")) {
				break;
			}
			if (lineStr.equalsIgnoreCase("<SensorDistances>")) {
				parseSensorDistance();
			} else if (lineStr.equalsIgnoreCase("<SensorsID>")) {
				parseSensorsID();
			} else if (lineStr.equalsIgnoreCase("<Servos>")) {
				parseServos();
			} else if (lineStr.equalsIgnoreCase("<Angles>")) {
				parseAngles();
			}
			lineStr = reader.readLine();
		}

		reader.close();

	}

	private void parseSensorDistance() {
		String lineStr = reader.readLine();
		while (lineStr != null) {
			if (lineStr.equalsIgnoreCase("</SensorDistances>")) {
				break;
			}
			if (lineStr.startsWith("<max ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);
				maxDistance = string2Int(value);
			} else if (lineStr.startsWith("<min ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);
				minDistance = string2Int(value);
			}

			lineStr = reader.readLine();
		}

	}

	private void parseSensorsID() {
		String lineStr = reader.readLine();
		while (lineStr != null) {
			if (lineStr.equalsIgnoreCase("</SensorsID>")) {
				break;
			}
			if (lineStr.startsWith("<front ")) {
				int from = lineStr.indexOf("address");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);

				frontParams[0] = string2Int(value);

				from = lineStr.indexOf("lnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				frontParams[1] = string2Int(value);

				from = lineStr.indexOf("rnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				frontParams[2] = string2Int(value);
			} else if (lineStr.startsWith("<right ")) {
				int from = lineStr.indexOf("address");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);

				rightParams[0] = string2Int(value);

				from = lineStr.indexOf("lnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				rightParams[1] = string2Int(value);

				from = lineStr.indexOf("rnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				rightParams[2] = string2Int(value);
			} else if (lineStr.startsWith("<back ")) {
				int from = lineStr.indexOf("address");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);

				backParams[0] = string2Int(value);

				from = lineStr.indexOf("lnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				backParams[1] = string2Int(value);

				from = lineStr.indexOf("rnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				backParams[2] = string2Int(value);
			} else if (lineStr.startsWith("<left ")) {
				int from = lineStr.indexOf("address");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);

				leftParams[0] = string2Int(value);

				from = lineStr.indexOf("lnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				leftParams[1] = string2Int(value);

				from = lineStr.indexOf("rnum");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				leftParams[2] = string2Int(value);
			}
			lineStr = reader.readLine();
		}

	}

	private void parseServos() {
		String lineStr = reader.readLine();
		while (lineStr != null) {
			if (lineStr.equalsIgnoreCase("</Servos>")) {
				break;
			}
			if (lineStr.startsWith("<middleAngle ")) {
				int from = lineStr.indexOf("id");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end);
				int id = string2Int(value);
				from = lineStr.indexOf("value");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				value = lineStr.substring(start + 1, end);
				middleAngleParams[id - 1] = string2Int(value);
			}

			lineStr = reader.readLine();
		}
	}

	private void parseAngles() {
		String lineStr = reader.readLine();
		while (lineStr != null) {
			if (lineStr.equalsIgnoreCase("</Angles>")) {
				break;
			}
			if (lineStr.startsWith("<angle ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String str = lineStr.substring(start + 1, end);
				double value = string2Double(str);
				from = lineStr.indexOf("name");
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				str = lineStr.substring(start + 1, end);
				if (str.equalsIgnoreCase("turnRight")) {
					turnRightAngle = value;
				} else if (str.equalsIgnoreCase("turnLeft")) {
					turnLeftAngle = value;
				}
			}

			lineStr = reader.readLine();
		}

	}

	@SuppressWarnings("boxing")
	private int string2Int(String str) {
		try {
			return Integer.valueOf(str.trim());
		} catch (Exception e) {
			return -1;
		}
	}

	@SuppressWarnings("boxing")
	private double string2Double(String str) {
		try {
			return Double.valueOf(str.trim());
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @return the frontParams
	 */
	public int[] getFrontParams() {
		return frontParams;
	}

	/**
	 * @return the rightParams
	 */
	public int[] getRightParams() {
		return rightParams;
	}

	/**
	 * @return the backParams
	 */
	public int[] getBackParams() {
		return backParams;
	}

	/**
	 * @return the leftParams
	 */
	public int[] getLeftParams() {
		return leftParams;
	}

	/**
	 * @return the minDistance
	 */
	public int getMinDistance() {
		return minDistance;
	}

	/**
	 * @return the maxDistance
	 */
	public int getMaxDistance() {
		return maxDistance;
	}

	/**
	 * @param minDistance
	 *            the minDistance to set
	 */
	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	/**
	 * @param maxDistance
	 *            the maxDistance to set
	 */
	public void setMaxDistance(int maxDistance) {
		this.maxDistance = maxDistance;
	}

	/**
	 * @return the middleAngleParams
	 */
	public int[] getMiddleAngleParams() {
		return middleAngleParams;
	}

	/**
	 * @return the turnRightAngle
	 */
	public double getTurnRightAngle() {
		return turnRightAngle;
	}

	/**
	 * @return the turnLeftAngle
	 */
	public double getTurnLeftAngle() {
		return turnLeftAngle;
	}

}

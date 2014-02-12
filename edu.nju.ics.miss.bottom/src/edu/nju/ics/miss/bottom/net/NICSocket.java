package edu.nju.ics.miss.bottom.net;

import edu.nju.ics.miss.bottom.device.PlatformParser;

public abstract class NICSocket {

	private static NICSocket instance;

	/**
	 * Get a socket instance
	 * 
	 * @param serverAddr
	 *            Server IP address
	 * @param serverPort
	 *            Server port
	 * @return a socket instance
	 * @throws NICNetException
	 */
	public static NICSocket getInstance(String serverAddr, int serverPort) {
		if (instance == null) {
			if (PlatformParser.getPlatformType() == PlatformParser.PLATFORM_TYPE_LEGO) {
				instance = new BluetoothSocket(serverAddr, serverPort);
			} else {
				instance = new WirelessSocket(serverAddr, serverPort);
			}
		}

		return instance;
	}

	/**
	 * Connect to serverAddr:serverPort
	 * 
	 * @throws NICNetException
	 *             If connected failed, throw an exception
	 */
	public abstract void connect() throws NICNetException;

	/**
	 * Disconnect from server
	 */
	public abstract void disconnect() throws NICNetException;

	/**
	 * In the end of the string will add a char '\n'
	 * 
	 * @param line
	 */
	public abstract void writeLine(String line) throws NICNetException;

	public abstract void writeString(String str) throws NICNetException;

	public abstract String readLine() throws NICNetException;

}

package edu.nju.ics.miss.bottom.net;

/**
 * 
 * @author hsun
 * 
 */
public class WirelessSocket extends NICSocket {

	/**
	 * Server IP address
	 */
	private String servAddr;

	/**
	 * Server port
	 */
	private int servPort;

	/**
	 * Constructor
	 * 
	 * @param serverAddr
	 *            Server IP address
	 * @param serverPort
	 *            Server port
	 */
	protected WirelessSocket(String serverAddr, int serverPort) {
		this.servAddr = serverAddr;
		this.servPort = serverPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#connect()
	 */
	@Override
	public void connect() throws NICNetException {
		int r = wifiConnect(servAddr, servPort);

		switch (r) {
		case 0: // 0:successful
			break;
		case 1: // 1:socket error
			throw new NICNetException("socket error");
		default: // 2:connect error
			throw new NICNetException("connect to " + servAddr + ":" + servPort
					+ " error");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#disconnect()
	 */
	@Override
	public void disconnect() {
		wifiDisconnect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#writeLine(java.lang.String)
	 */
	@Override
	public void writeLine(String line) {
		line += "\n";
//		byte buf[] = line.getBytes();
		wifiWrite(line, line.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#writeString(java.lang.String)
	 */
	@Override
	public void writeString(String str) {
//		byte buf[] = str.getBytes();
		wifiWrite(str, str.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#readLine()
	 */
	@Override
	public String readLine() throws NICNetException {
		byte[] buf = new byte[50];
		int l = wifiRead(buf, 50);
		return new String(buf, 0, l);
	}

	/**
	 * @param serverAddr
	 *            Server IP address
	 * 
	 * @param serverPort
	 *            Server Port
	 * 
	 * @throws NICNetException
	 */
	private static void connect(String serverAddr, int serverPort)
			throws NICNetException {
		// return 1:socket error; 2:connect error; 0:successful
		int r = wifiConnect(serverAddr, serverPort);

		switch (r) {
		case 0:
			break;
		case 1:
			throw new NICNetException("socket error");
		default:
			throw new NICNetException("connect to " + serverAddr + ":"
					+ serverPort + " error");
		}

	}

	/**
	 * @param serverAddr
	 *            Server IP address
	 * 
	 * @param serverPort
	 *            Server Port
	 * @return 1:socket error; 2:connect error; 0:successful
	 */
	synchronized private static native int wifiConnect(String serverAddr, int serverPort);

	/**
	 * Disconnect from server
	 */
	synchronized private static native void wifiDisconnect();

	/**
	 * Low-level method to write wireless data
	 * 
	 * @param buf
	 *            the buffer to write
	 * @param len
	 *            the number of bytes to send
	 * @return number of bytes actually written
	 */
	synchronized private static native int wifiWrite(String str, int len);

	/**
	 * Low-level method to read wireless data
	 * 
	 * @param buf
	 *            the buffer to read data into
	 * @param len
	 *            the number of bytes to read
	 * @return number of bytes actually read
	 */
	synchronized private static native int wifiRead(byte[] buf, int len);

}

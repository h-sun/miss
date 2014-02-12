package edu.nju.ics.miss.bottom.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.NXTSocketUtils;
import java.net.Socket;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * 
 * @author hsun
 * 
 */
public class BluetoothSocket extends NICSocket {

	/**
	 * Server IP address
	 */
	private String servAddr;

	/**
	 * Server port
	 */
	private int servPort;

	private BTConnection btc = null;

	private DataInputStream ins;
	private DataOutputStream outs;

	private Socket sock = null;

	/**
	 * Constructor
	 * 
	 * @param serverAddr
	 *            Server IP address
	 * @param serverPort
	 *            Server port
	 */
	protected BluetoothSocket(String serverAddr, int serverPort) {
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
		btc = Bluetooth.waitForConnection();
		// Set the NXTConnection to be used by Socket
		NXTSocketUtils.setNXTConnection(btc);
		try {
			sock = new Socket(servAddr, servPort);
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}

		try {
			ins = new DataInputStream(sock.getInputStream());
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}
		try {
			outs = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#disconnect()
	 */
	@Override
	public void disconnect() throws NICNetException {
		try {
			ins.close();
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}
		try {
			outs.close();
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}
		sock.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#writeLine(java.lang.String)
	 */
	@Override
	public void writeLine(String line) throws NICNetException {
		try {
			outs.writeChars(line + "\n");
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#writeString(java.lang.String)
	 */
	@Override
	public void writeString(String str) throws NICNetException {
		try {
			outs.writeChars(str);
		} catch (IOException e) {
			throw new NICNetException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see lejos.nic.net.NICSocket#readLine()
	 */
	@Override
	public String readLine() throws NICNetException {
		StringBuffer sb = new StringBuffer();

		while (true) {
			char c;
			try {
				c = ins.readChar();
			} catch (IOException e) {
				throw new NICNetException(e.getMessage());
			}
			if (c == '\n')
				break;
			sb.append(c);
		}
		return sb.toString();
	}

}

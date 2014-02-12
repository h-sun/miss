package edu.nju.ics.miss.bottom.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.internal.charset.CharsetDecoder;
import lejos.internal.charset.UTF8Decoder;
import lejos.internal.io.LejosInputStreamReader;
import edu.nju.ics.miss.bottom.io.exception.FileNotFoundException;

public class FileReader {

	private File file;

	private FileSystem fs = null;

	private BufferedReader br;

	private int lineNum;

	public FileReader(File file) throws IOException {
		this.file = file;

		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		if (file.isLeJOSFile()) {
			java.io.File f = new java.io.File(file.getName());
			InputStream is = new FileInputStream(f);
			CharsetDecoder cd = new UTF8Decoder();
			LejosInputStreamReader lisr = new LejosInputStreamReader(is, cd, 64);
			br = new BufferedReader(lisr);
		} else {
			fs = FileSystem.getInstance();
			lineNum = 1;
		}

	}

	public int getTotalLineNum() {
		return fs.getTotalLineNum(file.getName());
	}

	public int readBytes(byte[] buf, int length, int offset) {
		return fs.readBytes(file.getName(), buf, length, offset);
	}

	public String readLine() {
		if (file.isLeJOSFile()) {
			try {
				String lineStr = br.readLine();
				return lineStr == null ? null : lineStr.trim();
			} catch (IOException e) {
				return null;
			}
		}
		byte[] buf = new byte[60];
		int len = fs.readLine(file.getName(), buf, 60, lineNum++);

		if (len == -1) {
			return null;
		}

		String lineStr = new String(buf, 0, len);

		while (lineStr.charAt(lineStr.length() - 1) != '\n') {
			buf = new byte[60];
			len = fs.readLine(file.getName(), buf, 60, lineNum++);
			if (len == -1) {
				break;
			}
			lineStr += new String(buf, 0, len);
		}

		return lineStr.trim();
	}

	public void close() {
		if (file.isLeJOSFile()) {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void reset() {
		if (file.isLeJOSFile()) {
			try {
				br.close();
			} catch (IOException e) {
			}
			java.io.File f = new java.io.File(file.getName());
			InputStream is;
			try {
				is = new FileInputStream(f);
				CharsetDecoder cd = new UTF8Decoder();
				LejosInputStreamReader lisr = new LejosInputStreamReader(is,
						cd, 64);
				br = new BufferedReader(lisr);
			} catch (java.io.FileNotFoundException e) {
			}
		} else {
			lineNum = 1;
		}
	}

}

package edu.nju.ics.miss.bottom.io;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午9:12:39<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class FileReader {

	private BufferedReader bw;

	public FileReader(File file) throws IOException {
		bw = new BufferedReader(new java.io.FileReader(file.file));
	}

	public String readLine() {
		try {
			String lineStr = bw.readLine();
			return lineStr == null ? null : lineStr.trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		try {
			bw.close();
		} catch (IOException e) {
		}
	}

	public void reset() {
		try {
			bw.reset();
		} catch (IOException e) {
		}
	}

}

package edu.nju.ics.miss.bottom.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import lejos.internal.charset.CharsetEncoder;
import lejos.internal.charset.UTF8Encoder;
import lejos.internal.io.LejosOutputStreamWriter;


import edu.nju.ics.miss.bottom.io.exception.FileNotFoundException;
import edu.nju.ics.miss.bottom.util.MyHashMap;

public class FileWriter {

	private File file;

	private FileSystem fs = FileSystem.getInstance();

	private BufferedWriter bw = null;

	private static MyHashMap<String, StringBuffer> bufMap = new MyHashMap<String, StringBuffer>();
	
	public FileWriter(File file) throws IOException {
		this.file = file;
		if (file.isLeJOSFile()) {
			java.io.File f = new java.io.File(file.getName());
			OutputStream os = new FileOutputStream(f);
			CharsetEncoder ce = new UTF8Encoder();
			LejosOutputStreamWriter lisr = new LejosOutputStreamWriter(os, ce,
					64);
			bw = new BufferedWriter(lisr);
		} else {
			if (!file.exists()) {
				throw new FileNotFoundException();
			}
		}
	}

	public void write(String str) throws IOException {
		if (file.isLeJOSFile()) {
			bw.write(str);
		} else {
			fs.write(file.getName(), str);
		}
	}

	public void append(String str) throws IOException {
		if (file.isLeJOSFile()) {
			StringBuffer buffer = bufMap.get(file.getName());
			if(buffer == null){
				bufMap.put(file.getName(), new StringBuffer());
				buffer = bufMap.get(file.getName());
			}
			buffer.append(str);
			bw.append(buffer.toString());
		} else {
			fs.append(file.getName(), str);
		}
	}

	public void close() throws IOException {
		if (file.isLeJOSFile()) {
			if (bw != null) {
				bw.close();
			}
		}
	}

}

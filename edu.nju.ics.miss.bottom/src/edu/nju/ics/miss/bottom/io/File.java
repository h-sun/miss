package edu.nju.ics.miss.bottom.io;

import java.io.IOException;

import edu.nju.ics.miss.bottom.device.PlatformParser;

public class File {

	private static FileSystem fs = FileSystem.getInstance();

	private String fileName;

	private boolean isLeJOSFile = false;

	private java.io.File lejosFile = null;

	public File(String name) {
		fileName = name;
		if (PlatformParser.getPlatformType() == PlatformParser.PLATFORM_TYPE_LEGO) {
			isLeJOSFile = true;
			lejosFile = new java.io.File(name);
		}
	}

	public boolean createNewFile() throws IOException {
		if (isLeJOSFile) {
			return lejosFile.createNewFile();
		}
		return fs.createNewFile(fileName);
	}

	public boolean delete() {
		if (isLeJOSFile) {
			return lejosFile.delete();
		}
		return fs.delete(fileName);
	}

	public boolean mkdirs() {
		if (isLeJOSFile) {
			return false;
		}
		return fs.mkdirs(fileName);
	}

	public boolean exists() {
		if (isLeJOSFile) {
			return lejosFile.exists();
		}
		return fs.exists(fileName);
	}

	public boolean canRead() {
		if (isLeJOSFile) {
			return lejosFile.canRead();
		}
		return fs.canRead(fileName);
	}

	public boolean canWrite() {
		if (isLeJOSFile) {
			return lejosFile.canWrite();
		}
		return fs.canWrite(fileName);
	}

	public boolean isHidden() {
		if (isLeJOSFile) {
			return lejosFile.isHidden();
		}
		return fs.isHidden(fileName);
	}

	public boolean isFolder() {
		if (isLeJOSFile) {
			return false;
		}
		return fs.isFolder(fileName);
	}

	public File[] listFiles() {
		if (isLeJOSFile) {
			return null;
		}
		return fs.listFiles(fileName);
	}

	public File[] listFiles(String path) {
		if (isLeJOSFile) {
			return null;
		}
		return fs.listFiles(path);
	}

	public String getName() {
		return fileName;
	}

	public int getLength() {
		if (isLeJOSFile) {
			return (int) lejosFile.length();
		}
		return 0;
	}

	public boolean isLeJOSFile() {
		return isLeJOSFile;
	}

}

package edu.nju.ics.miss.bottom.io;

import java.util.LinkedList;
import java.util.List;

public class FileSystem {

	private static final int PLATFORM_LEGO = 1;

	private static final int PLATFORM_S3C2440 = 2;

	private static final int OPER_GEN_CREATE = 1;

	private static final int OPER_GEN_DELETE = 2;

	private static final int OPER_GEN_MKDIRS = 3;

	private static final int OPER_QUERY_EXIST = 11;

	private static final int OPER_QUERY_READ = 12;

	private static final int OPER_QUERY_WRITE = 13;

	private static final int OPER_QUERY_HIDDEN = 14;

	private static final int OPER_QUERY_FOLDER = 15;

	private static final int OPER_QUERY_LINE_NUMS = 16;

	private static final int OPER_IO_W_COVER = 21;

	private static final int OPER_IO_W_APPEND = 22;

	private static FileSystem instance = null;

	/**
	 * 1:ARM7(LEGO); 2:ARM9(S3C2440AL-40, K9F1208U0C); Others:Unknow.
	 * 
	 * @return Platform Information.
	 */
	private int getPlatformInfo() {
		return PLATFORM_S3C2440;
	}

	protected static FileSystem getInstance() {
		if (instance == null) {
			instance = new FileSystem();
		}

		return instance;
	}

	// ============ For File start ================
	protected boolean createNewFile(String fileName) {
		return fileGeneralOperation(fileName, OPER_GEN_CREATE);
	}

	protected boolean delete(String fileName) {
		return fileGeneralOperation(fileName, OPER_GEN_DELETE);
	}

	protected boolean mkdirs(String pathName) {
		return fileGeneralOperation(pathName, OPER_GEN_MKDIRS);
	}

	protected boolean exists(String fileName) {
		return fileQueryOperation(fileName, OPER_QUERY_EXIST) == 1 ? true
				: false;
	}

	protected boolean canRead(String fileName) {
		return fileQueryOperation(fileName, OPER_QUERY_READ) == 1 ? true
				: false;
	}

	protected boolean canWrite(String fileName) {
		return fileQueryOperation(fileName, OPER_QUERY_WRITE) == 1 ? true
				: false;
	}

	protected boolean isHidden(String fileName) {
		return fileQueryOperation(fileName, OPER_QUERY_HIDDEN) == 1 ? true
				: false;
	}

	protected boolean isFolder(String fileName) {
		return fileQueryOperation(fileName, OPER_QUERY_FOLDER) == 1 ? true
				: false;
	}

	synchronized protected File[] listFiles(String path) {
		byte[] buf = new byte[60];
		int i = 1;
		int len = fileSpecificQueryOperation(path, buf, 60, i);
		List<String> list = new LinkedList<String>();
		while(len>0){
			list.add(new String(buf, 0, len));
			len = fileSpecificQueryOperation(path, buf, 60, ++i);
		}
		File[] files = new File[list.size()];
		for(i=0; i<list.size(); i++){
			files[i] = new File(list.get(i));
		}
		return files;
	}

	// ============ For File end ==================

	// ============ For FileReader start ==========
	protected int getTotalLineNum(String fname) {
		return fileQueryOperation(fname, OPER_QUERY_LINE_NUMS);
	}

	protected int readBytes(String fname, byte[] buf, int length, int param) {
		return fileReadOperation(fname, buf, length, 2, param);
	}

	protected int readLine(String fname, byte[] buf, int length, int param) {
		return fileReadOperation(fname, buf, length, 1, param);
	}

	// ============ For FileReader end ============

	// ============ For FileWriter start ==========
	protected boolean write(String fname, String str) {
		return fileWriteOperation(fname, str, OPER_IO_W_COVER);
	}

	protected boolean append(String fname, String str) {
		return fileWriteOperation(fname, str, OPER_IO_W_APPEND);
	}

	// ============ For FileWriter end ============

	private static native boolean fileGeneralOperation(String fname, int mode);

	private static native int fileQueryOperation(String fname, int mode);
	
	private static native int fileSpecificQueryOperation(String path, byte[] buf, int len, int num);

	private static native boolean fileWriteOperation(String fname, String str, int mode);

	private static native int fileReadOperation(String fname, byte[] buf, int length, int mode, int param);
}

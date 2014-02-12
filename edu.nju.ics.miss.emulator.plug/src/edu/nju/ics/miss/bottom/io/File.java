package edu.nju.ics.miss.bottom.io;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午9:12:28<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class File {

	protected java.io.File file = null;

	public File(String fileName) {
		file = new java.io.File(fileName);
	}

}

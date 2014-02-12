package edu.nju.ics.miss.framework;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午2:58:18<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class Condition {

	private String name;

	private int id;

	public Condition(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public boolean isTrue() {
		return true;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}

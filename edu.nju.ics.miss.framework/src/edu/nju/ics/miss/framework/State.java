package edu.nju.ics.miss.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午2:59:14<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class State {

	private String name;

	private boolean isInitial = false;

	private List<Rule> rules = new ArrayList<Rule>();

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @return the isInitial
	 */
	public boolean isInitial() {
		return isInitial;
	}

	/**
	 * @param isInitial
	 *            the isInitial to set
	 */
	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}

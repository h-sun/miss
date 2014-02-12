package edu.nju.ics.miss.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午2:58:41<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class Rule {

	/** rule name */
	private String ruleName;

	private String currentState;

	private String nextState;

	private int conditions = 0;

	private List<Action> actions = new ArrayList<Action>();

	public Rule() {
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @return the currentState
	 */
	public String getCurrentState() {
		return currentState;
	}

	/**
	 * @return the nextState
	 */
	public String getNextState() {
		return nextState;
	}

	/**
	 * @return the conditions
	 */
	public int getConditions() {
		return conditions;
	}

	/**
	 * @return the actions
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	/**
	 * @param nextState
	 *            the nextState to set
	 */
	public void setNextState(String nextState) {
		this.nextState = nextState;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(int conditions) {
		this.conditions = conditions;
	}

}

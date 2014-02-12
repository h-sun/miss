package edu.nju.ics.miss.framework;

import java.util.List;

import edu.nju.ics.miss.bottom.util.MyHashMap;
import edu.nju.ics.miss.bottom.util.MyLogger;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午4:25:28<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public abstract class ContextManager {

	private State currentState = null;

	private List<State> states;

	private MyHashMap<String, Condition> conditions;

	private ActionManager actionManager = null;

	private int currentAllConditions = 0;

	protected void init() {
		StateParser stateParser = new StateParser();
		stateParser.setActionManager(actionManager);
		stateParser.parse();

		states = stateParser.getAllStates();

		conditions = stateParser.getAllConditions();

		for (State state : states) {
			if (state.isInitial()) {
				currentState = state;
				break;
			}
		}
		if (currentState != null) {
			MyLogger.writeLog(MyLogger.DEBUG, "初始状态为:" + currentState.getName());
		} else {
			MyLogger.writeLog(MyLogger.ERROR, "不存在初始状态！");
		}
	}

	protected void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	protected State getCurrentState() {
		return currentState;
	}

	protected void changeState(Rule rule) {
		currentState = null;
		for (State state : states) {
			if (state.getName().equals(rule.getNextState())) {
				currentState = state;
				break;
			}
		}
	}

	public Condition getCondition(String conditionName) {
		return conditions.get(conditionName);
	}

	public abstract void updateAllConditions();

	public void reset() {
		currentAllConditions = 0;
	}

	public void addCondition(Condition condition) {
		currentAllConditions = currentAllConditions | condition.getId();
	}

	protected int getCurrentAllConditions() {
		return currentAllConditions;
	}

}

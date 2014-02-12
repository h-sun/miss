package edu.nju.ics.miss.framework;

import java.util.List;

import edu.nju.ics.miss.bottom.util.MyLogger;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午3:01:20<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class Engine {

	private RuleManager ruleManager = new RuleManager();

	private ContextManager contextManager = null;

	private ActionManager actionManager = null;

	public Engine(ContextManager contextManager, ActionManager actionManager) {
		this.contextManager = contextManager;
		this.actionManager = actionManager;
		if (contextManager == null || actionManager == null) {
			MyLogger.writeLog(MyLogger.ERROR, "Engine构造函数参数不可为null,系统中止！");
			System.exit(-1);
		}
	}

	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		MyLogger.writeLog(MyLogger.DEBUG, "开始更新当前所有条件信息");
		contextManager.reset();
		contextManager.updateAllConditions();

		State currentState = contextManager.getCurrentState();
		MyLogger.writeLog(MyLogger.DEBUG, "当前状态为" + currentState.getName());

		int currentConditions = contextManager.getCurrentAllConditions();
		MyLogger.writeLog(MyLogger.DEBUG, "当前条件为" + currentConditions);

		Rule rule = ruleManager.activeRule(currentState, currentConditions);

		if (rule != null) {
			MyLogger.writeLog(MyLogger.DEBUG, "调用规则" + rule.getRuleName());
			executeRule(rule, currentConditions);
			contextManager.changeState(rule);
		} else {
			noRule(currentState, currentConditions);
		}
	}

	public void init() {
		contextManager.setActionManager(actionManager);
		contextManager.init();
	}

	private void executeRule(Rule rule, int currentConditions) {
		List<Action> acts = rule.getActions();
		for (int i = 0; i < acts.size(); i++) {
			Action act = acts.get(i);

			act.execute();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}

		}
	}

	public void startup() {
		MyLogger.writeLog(MyLogger.DEBUG, "系统启动");
		while (true) {
			run();
		}
	}

	public void noRule(State currentState, int currentConditions) {
		MyLogger.writeLog(MyLogger.ERROR, "无发找到相应规则(" + currentState.getName()
				+ "," + currentConditions + "),系统中止！");
		System.exit(-1);
	}
}

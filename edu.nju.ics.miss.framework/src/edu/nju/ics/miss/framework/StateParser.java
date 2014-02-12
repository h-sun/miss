package edu.nju.ics.miss.framework;

import java.io.IOException;
import java.util.List;

import edu.nju.ics.miss.bottom.io.File;
import edu.nju.ics.miss.bottom.io.FileReader;
import edu.nju.ics.miss.bottom.util.MyHashMap;
import edu.nju.ics.miss.bottom.util.MyLogger;
import edu.nju.ics.miss.bottom.util.MyStringUtils;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午5:00:45<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class StateParser {

	private FileReader reader = null;

	private ActionManager actionManager = null;

	public StateParser() {
		File ruleConfFile = new File("rules.xml");
		try {
			reader = new FileReader(ruleConfFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public StateParser(String fileName) {
		File ruleConfFile = new File(fileName);
		try {
			reader = new FileReader(ruleConfFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	private ConditionManager conditionManager = new ConditionManager();

	private MyHashMap<String, Condition> conditionMap;

	private List<State> states;

	public void parse() {
		MyLogger.writeLog(MyLogger.INFO, "开始解析规则文件");

		MyHashMap<String, State> stateMap = parseState();
		conditionMap = parseCondition();
		MyHashMap<String, Action> actionMap = parseAction();

		parseRule(stateMap, actionMap);
		reader.close();

		states = stateMap.valueSet();

		if (MyLogger.getLevel() != MyLogger.DEBUG) {
			return;
		}

		MyLogger.writeLog(MyLogger.DEBUG, "条件数目:"
				+ conditionMap.keySet().size());
		for (Condition c : conditionMap.valueSet()) {
			MyLogger.writeLog(MyLogger.DEBUG, "  条件名称:" + c.getName()
					+ ",条件ID:" + c.getId());
		}

		MyLogger.writeLog(MyLogger.DEBUG, "动作数目:" + actionMap.keySet().size());
		for (Action a : actionMap.valueSet()) {
			MyLogger.writeLog(MyLogger.DEBUG, "  动作名称:" + a.getActionName());
		}

		MyLogger.writeLog(MyLogger.DEBUG, "状态数目:" + stateMap.keySet().size());
		for (State state : stateMap.valueSet()) {
			MyLogger.writeLog(MyLogger.DEBUG, "状态名称:" + state.getName()
					+ (state.isInitial() ? "(初始状态)" : ""));
			MyLogger.writeLog(MyLogger.DEBUG, "包含规则数目:"
					+ state.getRules().size());
			for (Rule rule : state.getRules()) {
				MyLogger.writeLog(MyLogger.DEBUG,
						"  规则名称:" + rule.getRuleName());
				MyLogger.writeLog(MyLogger.DEBUG,
						"  当前状态:" + rule.getCurrentState());
				MyLogger.writeLog(MyLogger.DEBUG,
						"  下一状态:" + rule.getNextState());
				MyLogger.writeLog(MyLogger.DEBUG,
						"  触发条件:" + rule.getConditions());
				MyLogger.writeLog(MyLogger.DEBUG, "  动作数目:"
						+ rule.getActions().size());
				for (Action a : rule.getActions()) {
					MyLogger.writeLog(MyLogger.DEBUG,
							"    动作名称:" + a.getActionName());
				}
			}
		}

		MyLogger.writeLog(MyLogger.INFO, "规则文件解析完毕");
	}

	public MyHashMap<String, Condition> getAllConditions() {
		return conditionMap;
	}

	public List<State> getAllStates() {
		return states;
	}

	private MyHashMap<String, State> parseState() {
		MyHashMap<String, State> map = new MyHashMap<String, State>();
		String tagStart = "<states>";
		String tagEnd = "</states>";
		String lineStr = null;
		boolean tag = false;
		reader.reset();
		while (true) {
			lineStr = reader.readLine();
			if (lineStr == null) {
				break;
			}
			// 跳过空行及注释行
			if (lineStr.equals("") || lineStr.startsWith("<!--")) {
				continue;
			}
			if (!tag) {
				// 寻找<states>
				if (lineStr.equals(tagStart)) {
					// 寻找到<states>
					tag = true;
					continue;
				}
				continue;
			}

			if (tag && lineStr.equals(tagEnd)) {
				// <states>读取完毕
				break;
			}

			if (!lineStr.startsWith("<state ")) {
				continue;
			}

			int from = lineStr.indexOf("name");
			int start = lineStr.indexOf("\"", from + 1);
			int end = lineStr.indexOf("\"", start + 1);
			String name = lineStr.substring(start + 1, end).trim();
			State state = new State();
			state.setName(name);

			from = lineStr.indexOf("initial");
			if (from != -1) {
				start = lineStr.indexOf("\"", from + 1);
				end = lineStr.indexOf("\"", start + 1);
				String initial = lineStr.substring(start + 1, end).trim();

				if (initial.equalsIgnoreCase("true")) {
					state.setInitial(true);
				} else {
					state.setInitial(false);
				}
			}
			if (map.containsKey(state.getName())) {
				MyLogger.writeLog(MyLogger.WARN, "状态\"" + state.getName()
						+ "\"重复！");
			}
			map.put(state.getName(), state);
		}

		return map;
	}

	private MyHashMap<String, Action> parseAction() {
		MyHashMap<String, Action> map = new MyHashMap<String, Action>();
		String tagStart = "<actions>";
		String tagEnd = "</actions>";
		String lineStr = null;
		boolean tag = false;
		reader.reset();
		while (true) {
			lineStr = reader.readLine();
			if (lineStr == null) {
				break;
			}
			// 跳过空行及注释行
			if (lineStr.equals("") || lineStr.startsWith("<!--")) {
				continue;
			}
			if (!tag) {
				// 寻找<states>
				if (lineStr.equals(tagStart)) {
					// 寻找到<states>
					tag = true;
					continue;
				}
				continue;
			}

			if (tag && lineStr.equals(tagEnd)) {
				// <states>读取完毕
				break;
			}

			if (!lineStr.startsWith("<action ")) {
				continue;
			}

			int from = lineStr.indexOf("name");
			int start = lineStr.indexOf("\"", from + 1);
			int end = lineStr.indexOf("\"", start + 1);
			String name = lineStr.substring(start + 1, end).trim();

			from = lineStr.indexOf("type");
			start = lineStr.indexOf("\"", from + 1);
			end = lineStr.indexOf("\"", start + 1);
			String type = lineStr.substring(start + 1, end).trim();

			Action action = actionManager.generateAction(name, type);

			if (action == null) {
				MyLogger.writeLog(MyLogger.ERROR, "无法创建动作\"" + name + "\"！");
			}
			if (map.containsKey(action.getActionName())) {
				MyLogger.writeLog(MyLogger.WARN,
						"动作\"" + action.getActionName() + "\"重复！");
			}
			map.put(action.getActionName(), action);
		}

		return map;
	}

	private MyHashMap<String, Condition> parseCondition() {
		MyHashMap<String, Condition> map = new MyHashMap<String, Condition>();
		String tagStart = "<conditions>";
		String tagEnd = "</conditions>";
		String lineStr = null;
		boolean tag = false;
		reader.reset();
		while (true) {
			lineStr = reader.readLine();
			if (lineStr == null) {
				break;
			}
			// 跳过空行及注释行
			if (lineStr.equals("") || lineStr.startsWith("<!--")) {
				continue;
			}
			if (!tag) {
				// 寻找<states>
				if (lineStr.equals(tagStart)) {
					// 寻找到<states>
					tag = true;
					continue;
				}
				continue;
			}

			if (tag && lineStr.equals(tagEnd)) {
				// <states>读取完毕
				break;
			}

			if (!lineStr.startsWith("<condition ")) {
				continue;
			}

			int from = lineStr.indexOf("name");
			int start = lineStr.indexOf("\"", from + 1);
			int end = lineStr.indexOf("\"", start + 1);
			String name = lineStr.substring(start + 1, end).trim();
			Condition condition = map.get(name);
			if (condition != null) {
				MyLogger.writeLog(MyLogger.WARN, "条件\"" + name + "\"重复！");
				name = name.startsWith("!") ? name.substring(1) : "!" + name;
			} else {
				int id = conditionManager.generateConditionId();
				condition = new Condition(name, id);
				map.put(name, condition);
				name = name.startsWith("!") ? name.substring(1) : "!" + name;
			}

			condition = map.get(name);
			if (condition != null) {
				MyLogger.writeLog(MyLogger.WARN, "条件\"" + name + "\"重复！");
			} else {
				int id = conditionManager.generateConditionId();
				condition = new Condition(name, id);
				map.put(name, condition);
			}
		}

		return map;
	}

	private void parseRule(MyHashMap<String, State> stateMap,
			MyHashMap<String, Action> actionMap) {
		String tagStart = "<rules>";
		String tagEnd = "</rules>";
		String lineStr = null;
		boolean tag = false;
		while (true) {
			lineStr = reader.readLine();

			if (lineStr == null) {
				break;
			}

			// 跳过空行及注释行
			lineStr = lineStr.trim();
			if (lineStr.equals("")) {
				continue;
			}
			if (lineStr.startsWith("<!--")) {
				continue;
			}

			if (!tag) {
				// 寻找<rules>
				if (lineStr.equals(tagStart)) {
					// 寻找到<rules>
					tag = true;
					continue;
				}
				continue;
			}

			if (tag && lineStr.equals(tagEnd)) {
				// <rules>读取完毕
				break;
			}

			Rule rule = parseRule(lineStr, actionMap);

			State s = stateMap.get(rule.getNextState());
			if (s == null) {
				MyLogger.writeLog(MyLogger.ERROR, "规则\"" + rule.getRuleName()
						+ "\"的下一状态不存在！");
				continue;
			}
			s = stateMap.get(rule.getCurrentState());
			if (s == null) {
				MyLogger.writeLog(MyLogger.ERROR, "规则\"" + rule.getRuleName()
						+ "\"的当前状态不存在！");
				continue;
			}

			s.getRules().add(rule);
		}
	}

	private Rule parseRule(String firstLine, MyHashMap<String, Action> actionMap) {
		String lineStr = firstLine;
		Rule rule = new Rule();
		while (true) {
			if (lineStr.indexOf("</rule>") != -1) {
				break;
			}
			// 跳过空行
			if (lineStr.equals("")) {
				continue;
			}
			if (lineStr.startsWith("<!--")) {
				continue;
			}

			if (lineStr.startsWith("<rule ")) {
				// 寻找name
				int from = lineStr.indexOf("name");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String name = lineStr.substring(start + 1, end).trim();
				rule.setRuleName(name);
			} else if (lineStr.startsWith("<currstate ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end).trim();
				rule.setCurrentState(value);
			} else if (lineStr.startsWith("<nextstate ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end).trim();
				rule.setNextState(value);
			} else if (lineStr.startsWith("<condition ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end).trim();
				String cons[] = MyStringUtils.split(value, "&");
				int conditions = 0;
				for (String con : cons) {
					Condition c = conditionMap.get(con);
					if (c == null) {
						MyLogger.writeLog(MyLogger.ERROR, "条件\"" + con
								+ "\"不存在!");
						conditions = 0;
						break;
					} else {
						conditions = conditions | c.getId();
					}
				}
				rule.setConditions(conditions);
			} else if (lineStr.startsWith("<action ")) {
				int from = lineStr.indexOf("value");
				int start = lineStr.indexOf("\"", from + 1);
				int end = lineStr.indexOf("\"", start + 1);
				String value = lineStr.substring(start + 1, end).trim();

				String actions[] = MyStringUtils.split(value, "&");

				for (String s : actions) {
					Action action = actionMap.get(s);
					if (action == null) {
						MyLogger.writeLog(MyLogger.ERROR, "动作\"" + s + "\"不存在!");
					} else {
						rule.getActions().add(action);
					}
				}
			}

			lineStr = reader.readLine();
		}

		return rule;
	}

}

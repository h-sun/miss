package edu.nju.ics.miss.smartcar;

import edu.nju.ics.miss.bottom.RequestMessage;
import edu.nju.ics.miss.bottom.util.MyHashMap;
import edu.nju.ics.miss.bottom.util.MyStringUtils;
import edu.nju.ics.miss.framework.Action;
import edu.nju.ics.miss.framework.ActionManager;
import edu.nju.ics.miss.smartcar.action.BackwardAction;
import edu.nju.ics.miss.smartcar.action.ForwardAction;
import edu.nju.ics.miss.smartcar.action.StopAction;
import edu.nju.ics.miss.smartcar.action.TurnLeftAction;
import edu.nju.ics.miss.smartcar.action.TurnRightAction;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-29 下午8:55:27<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class SmartCarActionManager extends ActionManager {

	private MyHashMap<String, Action> map = new MyHashMap<String, Action>();

	@Override
	public Action generateAction(String name, String type) {
		if (name == null || type == null) {
			return null;
		}

		if (type.startsWith("forward")) {
			ForwardAction action = new ForwardAction(name);
			action.setParam(0);
			int index1 = type.indexOf("(");
			int index2 = type.indexOf(")");
			if (index2 <= index1 + 1) {
				return action;
			}
			String s = type.substring(index1 + 1, index2).trim();
			if (s.equalsIgnoreCase("right")) {
				action.setReference(RequestMessage.REFERENCE_RIGHT);
				return action;
			}
			if (s.equalsIgnoreCase("left")) {
				action.setReference(RequestMessage.REFERENCE_LEFT);
				return action;
			}

			try {
				int i = Integer.valueOf(s);
				if (i >= 0) {
					action.setParam(i);
				} else {
					action.setParam(0);
				}
			} catch (Exception e) {
				String ss[] = MyStringUtils.split(s, ",");
				if (ss.length != 2) {
					return action;
				}
				if (ss[0].equalsIgnoreCase("right")) {
					action.setReference(RequestMessage.REFERENCE_RIGHT);
				} else if (ss[0].equalsIgnoreCase("left")) {
					action.setReference(RequestMessage.REFERENCE_LEFT);
				}
				try {
					int i = Integer.valueOf(ss[1]);
					if (i >= 0) {
						action.setParam(i);
					} else {
						action.setParam(0);
					}
				} catch (Exception e2) {
				}

				return action;
			}
		}

		if (type.startsWith("backward")) {
			BackwardAction action = new BackwardAction(name);
			action.setParam(0);
			int index1 = type.indexOf("(");
			int index2 = type.indexOf(")");
			if (index2 <= index1 + 1) {
				return action;
			}
			String s = type.substring(index1 + 1, index2).trim();
			if (s.equalsIgnoreCase("right")) {
				action.setReference(RequestMessage.REFERENCE_RIGHT);
				return action;
			}
			if (s.equalsIgnoreCase("left")) {
				action.setReference(RequestMessage.REFERENCE_LEFT);
				return action;
			}

			try {
				int i = Integer.valueOf(s);
				if (i >= 0) {
					action.setParam(i);
				} else {
					action.setParam(0);
				}
			} catch (Exception e) {
				String ss[] = MyStringUtils.split(s, ",");
				if (ss.length != 2) {
					return action;
				}
				if (ss[0].equalsIgnoreCase("right")) {
					action.setReference(RequestMessage.REFERENCE_RIGHT);
				} else if (ss[0].equalsIgnoreCase("left")) {
					action.setReference(RequestMessage.REFERENCE_LEFT);
				}
				try {
					int i = Integer.valueOf(ss[1]);
					if (i >= 0) {
						action.setParam(i);
					} else {
						action.setParam(0);
					}
				} catch (Exception e2) {
				}

				return action;
			}
		}

		if (type.startsWith("turnleft")) {
			TurnLeftAction action = new TurnLeftAction(name);
			int index1 = type.indexOf("(");
			if (index1 == -1) {
				return action;
			}
			int index2 = type.indexOf(")");
			if (index2 <= index1 + 1) {
				return action;
			}
			String r = type.substring(index1 + 1, index2).trim();
			if (r.equalsIgnoreCase("right")) {
				action.setReference(RequestMessage.REFERENCE_RIGHT);
			} else if (r.equalsIgnoreCase("left")) {
				action.setReference(RequestMessage.REFERENCE_LEFT);
			}
			return action;
		}

		if (type.startsWith("turnright")) {
			TurnRightAction action = new TurnRightAction(name);
			int index1 = type.indexOf("(");
			if (index1 == -1) {
				return action;
			}
			int index2 = type.indexOf(")");
			if (index2 <= index1 + 1) {
				return action;
			}
			String r = type.substring(index1 + 1, index2).trim();
			if (r.equalsIgnoreCase("right")) {
				action.setReference(RequestMessage.REFERENCE_RIGHT);
			} else if (r.equalsIgnoreCase("left")) {
				action.setReference(RequestMessage.REFERENCE_LEFT);
			}
			return action;
		}

		if (type.startsWith("stop")) {
			return new StopAction(name);
		}

		return null;
	}

	public void addAction(Action action) {
		if (action == null) {
			return;
		}
		map.put(action.getActionName(), action);
	}

	public Action getAction(String actionName) {
		return map.get(actionName);
	}

}

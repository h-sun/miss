package edu.nju.ics.miss.framework;


/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午3:00:26<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
class RuleManager {

	protected Rule activeRule(State currentState, int currentConditions) {
		for (Rule rule : currentState.getRules()) {
			if ((rule.getConditions() & currentConditions) == rule
					.getConditions()) {
				return rule;
			}
		}

		return null;
	}

}

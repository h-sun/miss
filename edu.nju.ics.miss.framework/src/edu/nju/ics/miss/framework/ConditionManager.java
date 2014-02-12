package edu.nju.ics.miss.framework;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午3:00:13<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
class ConditionManager {

	private int index = 0;

	protected int generateConditionId() {
		int i = index;
		index++;
		int id = 1;
		while (i > 0) {
			id = id << 1;
			i--;
		}

		return id;
	}

}

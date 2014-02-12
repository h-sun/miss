package edu.nju.ics.miss.bottom.util;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-28 下午9:12:56<br>
 *
 * @author hsun
 * 
 * @since v0.1.1
 *
 */
import java.util.ArrayList;
import java.util.List;

public class MyStringUtils {

	public static String[] split(String str, String token) {
		String[] result = null;

		if (str == null || token == null) {
			return result;
		}

		List<String> list = new ArrayList<String>();
		String subStr = str.trim();
		String tmp;
		int index = subStr.indexOf(token);
		while (index >= 0) {
			if (index == 0) {
				subStr = subStr.substring(index + 1).trim();
				index = subStr.indexOf(token);
				continue;
			}
			tmp = subStr.substring(0, index).trim();

			if (!tmp.equals("")) {
				list.add(tmp);
			}
			subStr = subStr.substring(index + 1).trim();
			index = subStr.indexOf(token);
		}

		if (!subStr.equals("")) {
			list.add(subStr);
		}

		if (list.size() == 0) {
			return result;
		}

		result = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}

		return result;
	}

}

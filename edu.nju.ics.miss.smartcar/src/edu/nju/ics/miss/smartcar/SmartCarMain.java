package edu.nju.ics.miss.smartcar;

import edu.nju.ics.miss.framework.Engine;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-30 下午3:43:49<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class SmartCarMain {

	public static void main(String[] args) {
		SmartCarActionManager actionManager = new SmartCarActionManager();
		UltrasonicContextManager contextManager = new UltrasonicContextManager();
		Engine engine = new Engine(contextManager, actionManager);

		engine.init();

		engine.startup();
	}

}

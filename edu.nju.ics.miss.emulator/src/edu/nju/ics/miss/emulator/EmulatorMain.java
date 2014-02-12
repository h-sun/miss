package edu.nju.ics.miss.emulator;

import edu.nju.ics.miss.framework.Engine;

/**
 * Title: [一句话功能简述]<br>
 * <br>
 * Description: [功能详细描述]<br>
 * <br>
 * Create-time: 2013-12-29 上午9:20:05<br>
 * 
 * @author hsun
 * 
 * @since v0.1.1
 * 
 */
public class EmulatorMain {

	public static void main(String[] args) {
		EmulatorActionManager actionManager = new EmulatorActionManager();
		EmulatorContextManager contextManager = new EmulatorContextManager();
		Engine engine = new Engine(contextManager, actionManager);

		engine.init();

		engine.startup();
	}

}

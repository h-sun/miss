package edu.nju.ics.miss.bottom;

import edu.nju.ics.miss.bottom.controller.Controller;
import edu.nju.ics.miss.bottom.datatype.CarStatus;
import edu.nju.ics.miss.bottom.datatype.NICException;
import edu.nju.ics.miss.bottom.device.DeviceException;

public class MISSBottom {

	public static CarStatus queryCarStatus() throws NICException {
		return Controller.getInstance().getCurrentCarStatus();
	}

	public static void startAction(RequestMessage request)
			throws DeviceException, NICException {
		Controller.getInstance().startAction(request);
	}

}

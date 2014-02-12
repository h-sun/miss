package edu.nju.ics.miss.bottom.controller;

import edu.nju.ics.miss.bottom.RequestMessage;
import edu.nju.ics.miss.bottom.context.Context;
import edu.nju.ics.miss.bottom.datatype.ActionInfo;
import edu.nju.ics.miss.bottom.datatype.CarStatus;
import edu.nju.ics.miss.bottom.datatype.NICException;
import edu.nju.ics.miss.bottom.device.DeviceException;

public class Controller {

	private static Controller instance = null;

	private AdjusterDeamon adjustor = AdjusterDeamon.getInstance();

	private Actuator executor = Actuator.getInstance();

	private Context context = Context.getInstance();

	private Controller() throws NICException {
	}

	public static Controller getInstance() throws NICException {
		if (instance == null) {
			instance = new Controller();
		}

		return instance;
	}

	public CarStatus getCurrentCarStatus() {
		CarStatus status = new CarStatus();
		context.getSmartCar()
				.obtainUltrasonicDatas(status.getUltrasonicDatas());
		status.setCurrentAction(context.getCurrentActionType());

		status.setCompletedDistance(context.getSmartCar().getDistanceWalked());

		return status;
	}

	public void startAction(RequestMessage request) throws DeviceException {
		if (context.getCurrentActionType() != ActionInfo.ACTION_NO) {
			terminateAction();
		}

		switch (request.getActionType()) {
		case ActionInfo.ACTION_NO:
		case ActionInfo.ACTION_STOP:
			context.setCurrentActionType(ActionInfo.ACTION_NO);
			return;
		}

		try {
			context.reset(request);
		} catch (NICException e) {
			e.printStackTrace();
		}

		// LCD.drawString(context.getActionInfo().toString() + "\n", 0, 0);

		context.setCurrentActionType(context.getActionInfo().getActionType());

		switch (context.getActionInfo().getActionType()) {
		case ActionInfo.ACTION_FORWARD:
		case ActionInfo.ACTION_BACKWARD:
			adjustor.doBalance();
			executor.executeTravel(true);

			if (context.getActionInfo().getEnd() > 0) {
				adjustor.startDeamon(false);

				// LCD.drawString("......action end......\n", 0, 0);
				// context.updateDatas();
				// int dis = context.getUltrasonicDatas().getLeftDatas()[0];
				// dis += context.getUltrasonicDatas().getLeftDatas()[1];
				// dis /= 2;
				// LCD.drawString("dis=" + dis + "\n", 0, 0);
				// LCD.drawString(
				// "bias="
				// + (dis - context.getActionInfo()
				// .getReferenceDistance()) + "\n", 0, 0);

				adjustor.doBalance();

				// LCD.drawString("--------\n", 0, 0);
				// context.updateDatas();
				// dis = context.getUltrasonicDatas().getLeftDatas()[0];
				// dis += context.getUltrasonicDatas().getLeftDatas()[1];
				// dis /= 2;
				// LCD.drawString("dis=" + dis + "\n", 0, 0);
				// LCD.drawString(
				// "bias="
				// + (dis - context.getActionInfo()
				// .getReferenceDistance()) + "\n", 0, 0);

				context.setCurrentActionType(ActionInfo.ACTION_NO);
			} else {
				adjustor.startDeamon(true);
			}

			// if (context.getActionInfo().getEnd() < 0) {
			// executor.executeTravel(false);
			// adjustor.doBalance();
			// context.setCurrentActionType(ActionInfo.ACTION_NO);
			// } else {
			// executor.executeTravel(true);
			// adjustor.startDeamon();
			// }
			break;
		case ActionInfo.ACTION_TURN_RIGHT:
		case ActionInfo.ACTION_TURN_LEFT:
			executor.executeTurn();
			adjustor.doBalance4Turn();
			context.setCurrentActionType(ActionInfo.ACTION_NO);
			break;
		default:
			break;
		}
		// 执行动作

	}

	/**
	 * 终止当前动作
	 * 
	 * @throws DeviceException
	 */
	private void terminateAction() throws DeviceException {
		// LCD.drawString("...terminate action...\n", 0, 0);
		adjustor.stopDeamon();
		executor.terminateExecute();
		adjustor.doBalance();
		context.setCurrentActionType(ActionInfo.ACTION_NO);
	}

}

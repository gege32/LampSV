package hu.gehorvath.lampsv.core.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.Controller;
import hu.gehorvath.lampsv.core.Framework;
import jssc.SerialPortException;

public class CommunicationService {

	Map<Controller, Protocol> lamps = new HashMap<Controller, Protocol>();

	Logger logger = Logger.getLogger(CommunicationService.class);

	public void init() {
		for (Controller controller : Framework.getControllerProvider().getAllControllers()) {
			Protocol prot = new Protocol(controller);
			lamps.put(controller, prot);
		}

	}

	public void initLamp(Controller controller) {
		try {
			lamps.get(controller).init();
		} catch (FileNotFoundException e) {
			logger.error("File problems!" + e);
		} catch (SerialPortException e) {
			logger.error("Serial port error" + e);
		}
	}

	public boolean isInit(Controller controller) {
		return lamps.get(controller).isInit();
	}

	public void startLamp(Controller controller) {
		lamps.get(controller).start();
	}

	public void stopLamp(Controller controller) {
		lamps.get(controller).stop();
	}

	public boolean isStarted(Controller controller) {
		return lamps.get(controller).isEnabled();
	}
	
	public void loadProgram(Controller controller) {
		lamps.get(controller).updateProgram();
	}

}

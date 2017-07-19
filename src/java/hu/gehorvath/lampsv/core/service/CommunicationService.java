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
		for(Controller controller : Framework.getControllerProvider().getAllControllers()) {
			Protocol prot = new Protocol(controller);
			try {
				prot.init();
			} catch (FileNotFoundException e) {
				logger.error("File problems!" + e);
			} catch (SerialPortException e) {
				logger.error("Serial port error" + e);
			}
			lamps.put(controller, prot);
		}
		
	}
	
	public void startLamp(Controller controller) {
		lamps.get(controller).start();
	}
	
	public boolean isStarted(Controller controller) {
		return lamps.get(controller).isEnabled();
	}
	
}

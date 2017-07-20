package hu.gehorvath.lampsv.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.provider.ControllerProvider;
import hu.gehorvath.lampsv.core.provider.PresetProvider;
import hu.gehorvath.lampsv.core.provider.ProgramProvider;
import hu.gehorvath.lampsv.core.service.CommunicationService;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class Framework {
	
	static Logger logger = Logger.getLogger(Framework.class);
		
	private static CoreData coreData;
	
	private static CommunicationService commService = new CommunicationService();
		
	private static Marshaller marshaller;
	
	private static File dataFile = new File("data/data.xml");
	
	private static Lock lock = new ReentrantLock();
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class CoreData{
		private PresetProvider presetProvider;
		private ProgramProvider programProvider;
		private ControllerProvider controllerProvider;
		
		public PresetProvider getPresetProv(){
			return presetProvider;
		}
		public ProgramProvider getProgProv(){
			return programProvider;
		}
		public ControllerProvider getContrProv(){
			return controllerProvider;
		}
	}

	public void startFramework() {
		
		try {
			
			logger.info("Starting up framework!");
			JAXBContext jaxbContext = JAXBContext.newInstance(PresetProvider.class, HashMap.class, ProgramProvider.class, Program.class, Controller.class, ControllerProvider.class, CoreData.class);
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			coreData = (CoreData) jaxbUnmarshaller.unmarshal(dataFile);
			

			marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		} catch (JAXBException e) {
			logger.error("Unmarshalling failed", e);
		}
		
		commService.init();
	}
	
	public static PresetProvider getPresetProvider(){
		return coreData.getPresetProv();
	}
	
	public static ProgramProvider getProgramProvider(){
		return coreData.getProgProv();
	}
	
	public static ControllerProvider getControllerProvider(){
		return coreData.getContrProv();
	}
	
	public static CommunicationService getCommunicationService() {
		return commService;
	}
	
	//If old is null, then its an addition, if not, then a modification
	public static void savePreset(Preset newPreset, Preset oldPreset){
		
		List<Preset> presets = getPresetProvider().getAllPreset();
		
		int newID = 0;
		for(Preset preset : presets) {
			if(preset.getIntId() > newID) {
				newID = preset.getIntId();
			}
		}
		newID++;
		
		if(oldPreset == null) {
			newPreset.setPresetID(newID);
			presets.add(newPreset);
		}else {
			oldPreset.setLedTiminds(newPreset.getLEDValues());
			oldPreset.setMeasuredLux(newPreset.getLUX());
		}
		
		writeDataFile();
		logger.info("Preset saved!");
	}
	
	public static void saveProgram(Program newProgram, Program oldProgram){
		List<Program> programs = getProgramProvider().getAllPrograms();
		int newID = 0;
		for(Program prog : programs) {
			if(prog.getIntID() > newID) {
				newID = prog.getIntID();
			}
		}
		newID++;
		
		if(oldProgram == null) {
			newProgram.setId(newID);
			programs.add(newProgram);
		}else {
			oldProgram.setcharacterCode(newProgram.getCharacterCode());
			oldProgram.setDescription(newProgram.getDesc());
			oldProgram.setPresets(newProgram.getPresetList());
		}
		writeDataFile();
		logger.info("Program saved!");
	}
	
	public static void saveController(Controller newController, Controller oldController){
	
		List<Controller> controllers = getControllerProvider().getAllControllers();
		int newID = 0;
		for(Controller contr : controllers) {
			if(contr.getId() > newID) {
				newID = contr.getId();
			}
		}
		newID++;
		
		if(oldController == null) {
			newController.setId(newID);
			controllers.add(newController);
		}else {
			oldController.setControllerName(newController.getName());
			oldController.setProgram(newController.getContProgram());
			oldController.setSerialPort(newController.getSerailPort());
		}
		
		writeDataFile();
		logger.info("Controller saved!");
	}
	
	private static void writeDataFile() {
		try {
			marshaller.marshal(coreData, dataFile);
			logger.info("Data file saved!");
		} catch (JAXBException e) {
			logger.error("Save failed", e);
		}

	}
	
	public static List<String> getAvailableSerialPorts(){
		return Arrays.asList(SerialPortList.getPortNames());
	}
	
	public static void startMeasurement(Controller controller) {
		commService.startLamp(controller);
	}
	
	public static void stopMeasurement(Controller controller) {
		commService.stopLamp(controller);
	}
	
	public static void initController(Controller controller) throws FileNotFoundException, SerialPortException {
		commService.initLamp(controller);
	}
	
	public static void loadProgramToController(Controller controller) {
		commService.loadProgram(controller);
	}
	
	public static boolean isMeasurementRunning(Controller controller) {
		return commService.isStarted(controller);
	}
	
	public static boolean isInit(Controller controller) {
		return commService.isInit(controller);
	}
	
}

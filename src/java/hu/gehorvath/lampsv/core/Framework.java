package hu.gehorvath.lampsv.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.provider.ControllerProvider;
import hu.gehorvath.lampsv.core.provider.PresetProvider;
import hu.gehorvath.lampsv.core.provider.ProgramProvider;
import hu.gehorvath.lampsv.ui.MainWindow;

public class Framework {
	
	static Logger logger = Logger.getLogger(Framework.class);

	private static String presetCollectionFilename = "data/presetCollection.xml";
	private static String programCollectionFilename = "data/programCollection.xml";
	private static String controllerCollectionFilename = "data/controllerCollection.xml";
	
	private static PresetProvider presetProvider = new PresetProvider();
	private static ProgramProvider programProvider = new ProgramProvider();
	private static ControllerProvider controllerProvider = new ControllerProvider();

	public static void startFramework() {
		
		File presetFile = new File(presetCollectionFilename);
		File programFile = new File(programCollectionFilename);
		File controllerFile = new File(controllerCollectionFilename);

		try {
			
			logger.info("Starting up framework!");
			JAXBContext jaxbContext = JAXBContext.newInstance(PresetProvider.class, HashMap.class, ProgramProvider.class, Program.class, Controller.class, ControllerProvider.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			presetProvider = (PresetProvider) jaxbUnmarshaller.unmarshal(presetFile);

			programProvider = (ProgramProvider) jaxbUnmarshaller.unmarshal(programFile);
			
			controllerProvider = (ControllerProvider) jaxbUnmarshaller.unmarshal(controllerFile);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PresetProvider getPresetProvider(){
		return presetProvider;
	}
	
	public static ProgramProvider getProgramProvider(){
		return programProvider;
	}
	
	public static ControllerProvider getControllerProvider(){
		return controllerProvider;
	}

}

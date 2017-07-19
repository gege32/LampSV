package hu.gehorvath.lampsv.core;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.provider.ControllerProvider;
import hu.gehorvath.lampsv.core.provider.PresetProvider;
import hu.gehorvath.lampsv.core.provider.ProgramProvider;
import hu.gehorvath.lampsv.core.service.CommunicationService;

public class Framework {
	
	static Logger logger = Logger.getLogger(Framework.class);
		
	private static CoreData coreData;
	
	private static CommunicationService commService = new CommunicationService();
	
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
		
		File dataFile = new File("data/data.xml");

		try {
			
			logger.info("Starting up framework!");
			JAXBContext jaxbContext = JAXBContext.newInstance(PresetProvider.class, HashMap.class, ProgramProvider.class, Program.class, Controller.class, ControllerProvider.class, CoreData.class);
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			coreData = (CoreData) jaxbUnmarshaller.unmarshal(dataFile);


		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}

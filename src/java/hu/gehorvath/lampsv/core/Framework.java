package hu.gehorvath.lampsv.core;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.provider.PresetProvider;
import hu.gehorvath.lampsv.core.provider.ProgramProvider;
import hu.gehorvath.lampsv.ui.MainWindow;

public class Framework {
	
	static Logger logger = Logger.getLogger(Framework.class);

	private static String presetCollectionFilename = "data/presetCollection.xml";
	private static String programCollectionFilename = "data/programCollection.xml";
	
	private static PresetProvider presetProvider = new PresetProvider();
	private static ProgramProvider programProvider = new ProgramProvider();

	public static void startFramework() {
		
		File presetFile = new File(presetCollectionFilename);
		File programFile = new File(programCollectionFilename);

		try {
			
			logger.info("Starting up framework!");
			JAXBContext jaxbContext = JAXBContext.newInstance(PresetProvider.class, HashMap.class, ProgramProvider.class, Program.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			presetProvider = (PresetProvider) jaxbUnmarshaller.unmarshal(presetFile);

			programProvider = (ProgramProvider) jaxbUnmarshaller.unmarshal(programFile);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PresetProvider getPresetProvider(){
		return presetProvider;
	}

}

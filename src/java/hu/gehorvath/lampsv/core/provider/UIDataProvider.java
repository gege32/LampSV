package hu.gehorvath.lampsv.core.provider;

import java.util.List;
import java.util.Map;

import hu.gehorvath.lampsv.core.Controller;
import hu.gehorvath.lampsv.core.Framework;
import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.Program;


public class UIDataProvider {

	public static final List<Preset> getPresets(){
		return Framework.getPresetProvider().getAllPreset();
	}
	
	public static final List<Program> getPrograms(){
		return Framework.getProgramProvider().getAllPrograms();
	}
	
	public static final List<Controller> getControllers(){
		return Framework.getControllerProvider().getAllControllers();
	}
	
}

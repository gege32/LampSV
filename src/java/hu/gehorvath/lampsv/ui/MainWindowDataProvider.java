package hu.gehorvath.lampsv.ui;

import java.util.HashMap;
import java.util.List;

import hu.gehorvath.lampsv.core.Controller;
import hu.gehorvath.lampsv.core.Framework;
import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.Program;
import hu.gehorvath.lampsv.ui.data.ICallback;

public class MainWindowDataProvider {

	public void getPresetsAsync(ICallback callback){
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				callback.onSuccess(Framework.getPresetProvider().getAllPreset());				
			}			
		});
		thread.start();
	}
	
	public void getProgramsAsync(ICallback callback){
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				callback.onSuccess(Framework.getProgramProvider().getAllPrograms());				
			}			
		});
		thread.start();
	}
	
	public void getControllersAsync(ICallback callback){
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				callback.onSuccess(Framework.getControllerProvider().getAllControllers());				
			}			
		});
		thread.start();
	}
	
	public List<Program> getPrograms(){
		return Framework.getProgramProvider().getAllPrograms();
	}
	
	public List<Controller> getControllers(){
		return Framework.getControllerProvider().getAllControllers();
	}
	
	public List<Preset> getPresets(){
		return Framework.getPresetProvider().getAllPreset();
	}
	
	
	//If old is null, then its an addition, if not, then a modification
	public void savePreset(Preset newPeset, Preset oldPreset){
		
	}
	
	public void saveProgram(Program newProgram, Program oldProgram){
		
	}
	
	public void saveController(Controller newController, Controller oldController){
		
	}
	
}

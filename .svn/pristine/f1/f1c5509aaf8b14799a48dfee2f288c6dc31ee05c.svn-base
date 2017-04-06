package hu.gehorvath.lampsv.ui;

import hu.gehorvath.lampsv.core.Framework;
import hu.gehorvath.lampsv.ui.data.ICallback;

public class MainWindowDataProvider {

	public void getPresets(ICallback callback){
		
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				callback.onSuccess(Framework.getPresetProvider().getAllPreset());				
			}			
		});
		thread.start();
	}
	
}

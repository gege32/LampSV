package hu.gehorvath.lampsv.core.provider;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.helpers.adapters.PresetMapAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PresetProvider {
	
	@XmlJavaTypeAdapter(PresetMapAdapter.class)
	private Map<Integer, Preset> allPresets = new HashMap<>();
	
	/**
	 * Return all availabe preset data.
	 * @return all <code>Preset</code>
	 */
	public Map<Integer, Preset> getAllPreset(){
		return allPresets;
	}
	
	public Preset getPresetByID(int id){
		return null;
	}
	
}

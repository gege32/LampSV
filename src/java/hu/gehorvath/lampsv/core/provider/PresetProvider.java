package hu.gehorvath.lampsv.core.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.helpers.adapters.PresetMapAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PresetProvider {
	
	@XmlElementWrapper(name="presets")
	@XmlElement(name="preset")
	private List<Preset> allPresets = new ArrayList<Preset>();
	
	/**
	 * Return all availabe preset data.
	 * @return all <code>Preset</code>
	 */
	public List<Preset> getAllPreset(){
		return allPresets;
	}
	
	public Preset getPresetByID(int id){
		return null;
	}
	
}

package hu.gehorvath.lampsv.core;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlTransient;

public class Program {
	
	@XmlAttribute
	int characterCode;
	
	@XmlAttribute
	int id;
	
	@XmlAttribute
	String description;
	
	@XmlElement(name="preset")
	@XmlIDREF
	@XmlElementWrapper(name="presets")
	List<Preset> presetList = new LinkedList<>();
	
	public Program(){
	}
	
	public Program(int code, int id, List<Preset> presets){
		this.characterCode = code;
		this.id = id;
		this.presetList = presets;
	}
	
	
	public List<Preset> getPresetList(){
		return this.presetList;
	}
	
	public String getProgramCode(){
		return String.valueOf((char) characterCode);
	}
	
	@XmlID
	public String getID(){
		return this.id + "";
	}
	
	public String toString(){
		if(characterCode == 0x00) return "New...";
		return String.valueOf((char) characterCode);
	}
	
	public void setPresets(List<Preset> presets){
		this.presetList = presets;
	}
	
	public void setcharacterCode(int programCode){
		this.characterCode = programCode;
	}
	
	public void setDescription(String desc){
		this.description = desc;
	}
	public String getDesc(){
		return this.description;
	}
	
}

package hu.gehorvath.lampsv.core;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

public class Program {

	@XmlAttribute
	char programCode;
	
	@XmlElement(name="presets")
	@XmlIDREF
	@XmlElementWrapper(name="presets")
	List<Preset> presetList = new LinkedList<>();
	
	public Program(){}
	
	public Program(char programCode){
		this.programCode = programCode;
	}
	
	public List<Preset> getPresetList(){
		return this.presetList;
	}
}
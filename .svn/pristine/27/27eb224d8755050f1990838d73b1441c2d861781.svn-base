package hu.gehorvath.lampsv.core.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import hu.gehorvath.lampsv.core.Program;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramProvider {

	@XmlElementWrapper(name="programs")
	@XmlElement(name="program")
	private List<Program> allPrograms = new ArrayList<>(); 
	
	
	public List<Program> getAllPrograms(){
		return this.allPrograms;
	}
	
	
}

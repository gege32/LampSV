package hu.gehorvath.lampsv.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

public class Controller {
	
	@XmlAttribute
	String controllerName;
	
	@XmlAttribute
	String serialPort;
	
	
	@XmlIDREF
	Program program;
	
	public Program getProgram(){
		return program;
	}
	
	public String getSerailPort(){
		return serialPort;
	}
	
	public String getControllerName(){
		return controllerName;
	}

}

package hu.gehorvath.lampsv.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;

public class Controller {
	
	@XmlAttribute
	private String controllerName;
	
	@XmlAttribute
	private String serialPort;
	
	
	@XmlIDREF
	Program program;
	
	public Controller() {
		controllerName = "New...";
	}
	
	public Program getContProgram(){
		return program;
	}
	
	public String getSerailPort(){
		return serialPort;
	}
	
	public String getName(){
		return controllerName;
	}
	
	public void setControllerName(String name){
		this.controllerName = name;
	}
	
	public void setSerialPort(String port){
		this.serialPort = port;
	}
	
	public void setProgram(Program program){
		this.program = program;
	}
	
	public String toString() {
		return controllerName;
	}

}

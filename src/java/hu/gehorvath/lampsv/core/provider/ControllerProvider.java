package hu.gehorvath.lampsv.core.provider;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import hu.gehorvath.lampsv.core.Controller;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ControllerProvider {

	@XmlElementWrapper(name="controllers")
	@XmlElement(name="controller")
	List<Controller> controllers;
	
	public List<Controller> getAllControllers(){
		return this.controllers;
	}
	
}

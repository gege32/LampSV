package hu.gehorvath.lampsv.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;

public class Preset {
	
	@XmlAttribute
	int presetID;
	
	@XmlElementWrapper(name="values")
	@XmlElement(name="value")
	int[] ledTimings = new int[6];
	
	@XmlAttribute
	int measuredLUX;	

	public String toString(){
		if(presetID == -1) return "New...";
		return "ID: " + presetID + " - LUX: ~" + measuredLUX;
	}
	
	public Preset(int id, int[] leds, int lux){
		this.presetID = id;
		this.ledTimings = leds;
		this.measuredLUX = lux;
	}
	
	public Preset(){
		presetID = -1;
	}
	
	@XmlID
	public String getID(){
		return presetID + "";
	}
	
	public final int[] getLEDValues(){
		return ledTimings;
	}
		
	public final int getLUX(){
		return measuredLUX;
	}
	
	public final int getPresetID(){
		return presetID;
	}
	
	public void setPresetID(int id){
		this.presetID = id;
	}
	
	public void setLedTiminds(int[] ledTimings){
		this.ledTimings = ledTimings;
	}
	
	public void setMeasuredLux(int measuredLUX){
		this.measuredLUX = measuredLUX;
	}
}

package hu.gehorvath.lampsv.core;

import java.nio.charset.Charset;
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
		id = -1;
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
		if(characterCode == 0) {
			return "";
		}
		return String.valueOf((char) characterCode);
	}
	
	@XmlID
	public String getID(){
		return this.id + "";
	}
	
	public String toString(){
		if(id == -1) return "New...";
		return String.valueOf((char) characterCode) + " - " + description;
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
	
	public int getCharacterCode() {
		return characterCode;
	}
	
	public int getIntID() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public List<byte[]> getData(){
		List<byte[]> toreturn = new LinkedList<byte[]>();
		int presIndex = 0;
		for(Preset preset : presetList){
			byte[] temp = new byte[8];
			temp[0] = '$';
			temp[1] = (byte)presIndex;
			int i = 2;
			for(int value : preset.getLEDValues()){
				temp[i] = (byte)value;
				i++;
			}
			toreturn.add(temp);
			presIndex++;
		}
		
		
		return toreturn;
	}
	
	public byte[] getHeader(){
		int maxpreset = presetList.size() - 1;
		return new byte[]{'#',(byte)maxpreset, (byte) characterCode};
	}
	
}

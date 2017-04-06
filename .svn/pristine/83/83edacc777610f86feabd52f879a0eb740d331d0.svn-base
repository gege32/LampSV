package hu.gehorvath.lampsv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;

public class ProgramFileParser {
	/**
	 * file structure:
	 * A (name of the program)
	 * !sample text (comment)
	 * 0,0,0,0,0,0,0 (program, led1up, led2up, led3up, led1down, led2down, led3down)
	 * 1,20,20,20,30,30,30
	 * etc...
	 * 
	 * @param string filname
	 * @throws FileNotFoundException 
	 */
	
	BufferedReader filereader = null;
	
	String programCode = "";
	String fileName = "";
	
	int maxpreset = -1;
	
	List<String> lines = new LinkedList<String>();
	
	public void parseFile() throws IOException{
		int lineNumber = 0;
		while(true){
			String temp = filereader.readLine();
			lineNumber++;
			if(temp == null){
				break;
			}else if(temp.substring(0, 1).equals("!")){
				continue;
			}else{
				if(programCode.equals("")){
					programCode = temp;
					if(programCode.length() != 1){
						throw new SyntaxException("Syntax error in program file on line " + lineNumber + ". Program code not found!");
					}
				}
				else{
					String[] temp2 = temp.split(",");
					if(temp2.length != 7){
						throw new SyntaxException("Syntax error in program file on line " + lineNumber + ".");
					}
					for(String number : temp2){
						if(Integer.parseInt(number) < 0 || Integer.parseInt(number) > 50){
							throw new SyntaxException("Syntax error in program file on line " + lineNumber + ". Values must be 0 <= val <= 50");
						}
					}
					if(Integer.parseInt(temp2[0]) > 9){
						throw new SyntaxException("Syntax error in program file on line " + lineNumber + ". There shou be only 10 presets in one program (0-9)");
					}
					lines.add(temp);
				}
			}
		}
		if(lines.size() > 10){
			throw new SyntaxException("Syntax error in program file on line " + lineNumber + ". There shou be only 10 presets in one program (0-9)");
		}
		
	}
	
	public List<byte[]> getData(){
		List<byte[]> toreturn = new LinkedList<byte[]>();
		for(String line : lines){
			byte[] temp = new byte[8];
			String[] values = line.split(",");
			temp[0] = '$';
			int i = 1;
			for(String value : values){
				int tosend = Integer.parseInt(value);
				temp[i] = (byte)tosend;
				i++;
			}
			toreturn.add(temp);
		}
		
		
		return toreturn;
	}
	
	public byte[] getHeader(){
		maxpreset = lines.size() - 1;
		return new byte[]{'#',(byte)maxpreset, programCode.getBytes(Charset.forName("ASCII"))[0]};
	}
	
	public String toString(){
		return "ProgramCode: " + programCode + " Filename: " + fileName;
	}

	public ProgramFileParser(String string) throws FileNotFoundException {
		fileName = string;
		filereader = new BufferedReader(new FileReader(new File(string)));
	}
	
}

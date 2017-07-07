package hu.gehorvath.lampsv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.Framework;
import hu.gehorvath.lampsv.ui.MainWindow;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class LampSV {

	int lastPreset = 0;
	int lastLight = 0;

	boolean enabled = true;

	private String portName = "";
	
	private String deviceTimeText = "00:00:00";
	private long deviceStartTime = 0;

	protected SerialPort mesaureDevice = null;
	
	ProgramFileParser currentProgram = null;

	private FileOutputStream presetDataFile;
	private FileOutputStream measureDataFile;
	
	boolean measureSave;
	boolean presetSave;
	
	boolean debug;
	
	Properties prop = new Properties();
	private String presetFileName = "presetData.dat";
	private String measureFileName = "measure.dat";
	private String lastPresetChange = "";
	
	Logger logger = Logger.getLogger(LampSV.class);

	public LampSV() throws FileNotFoundException, IOException {
		prop.load(new FileReader(new File("data/lampsv.prop")));
	}

	private void start() {
		Framework.startFramework();
		MainWindow mainscreen = new MainWindow();
		mainscreen.setVisible(true);
		
		logger.addAppender(new MainWindow.LogListener());
		logger.info("Szar");
		logger.debug("Szar");
		logger.error("Szar");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("CEST"));
		
		debug = Boolean.parseBoolean((String)prop.get("debug"));
		measureSave = Boolean.parseBoolean((String)prop.get("measureSave"));
		presetSave = Boolean.parseBoolean((String)prop.get("presetSave"));
		presetFileName = (String)prop.get("presetFileName");
		measureFileName = (String)prop.get("measureFileName");
		portName = (String)prop.get("portname");
		
		if(portName.equals("")){
			throw new RuntimeException("Serial port name was not found in the configuration");
		}
		
		if(prop.get("currentProgram") != null){
			try {
				currentProgram = new ProgramFileParser((String)prop.get("currentProgram"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		try {
			String[] portnames = SerialPortList.getPortNames();
			if(portnames.length == 0){
				throw new RuntimeException("There are no serial ports connection on this machine!");
			}
			System.out.println("Choose lamp controller serial port:");
			int portnr = 0;
			for(String port : portnames){
				System.out.println(portnr + ": " + port);
				portnr++;
			}
			do{
				String line = br.readLine();
				if(!isNumeric(line)){
					System.out.println("You must give a number!");
				}
				else{
					portnr = Integer.parseInt(line);
					break;
				}
			}while(true);
			this.portName = portnames[portnr];
			mesaureDevice = new SerialPort(this.portName);
			mesaureDevice.openPort();
			mesaureDevice.setParams(SerialPort.BAUDRATE_4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_2,
					SerialPort.PARITY_NONE);
		} catch (IOException | SerialPortException ex) {
			ex.printStackTrace();
		}

		try {
			presetDataFile = new FileOutputStream(presetFileName);
			measureDataFile = new FileOutputStream(measureFileName);
		} catch (FileNotFoundException e) {
		}

		String command = "";

		byte[] incoming = new byte[5];
		byte[] messageType = new byte[5];
		String data = "";

		//main cycle
		while (enabled) {

			// write
			try {
				if (br.ready()) {
					command = br.readLine();
					if(isNumeric(command)){
						mesaureDevice.writeByte((byte)Integer.parseInt(command));
					}else if(command.contains("l")){
						String[] commandoptions = command.split(":");
						if(commandoptions.length == 2){
							currentProgram = new ProgramFileParser(commandoptions[1]);
							currentProgram.parseFile();
							writeCurrentProgram();
						}else{
							System.out.println("Wrong use of command load: l:filename.extension (e.g.: l:example.txt)");
						}
						
					}else if(command.contains("q")){
						enabled = false;
					}else if(command.contains("i")){
						System.out.println("Uptime: " + deviceTimeText);
						System.out.println("Current preset: " + lastPreset);
						System.out.println("Last preset change: " + lastPresetChange);
						if(this.currentProgram != null){
							System.out.println(currentProgram.toString());
						}
					}
				}
			} catch (IOException | SerialPortException e1) {
				e1.printStackTrace();
			}

			//read
			try {
				
				messageType = mesaureDevice.readBytes(1, 100);
				if(debug){
					System.out.println("MessageType: " + new String(messageType));
				}
				if(isNumeric(new String(messageType)))
					continue;

				if (messageType[0] == 'P') {
					incoming = mesaureDevice.readBytes(4, 100);
					if(debug){
						System.out.println("Preset Data: " + new String(incoming));
					}
					data = new String(incoming);
					lastPreset = Integer.parseInt(data);
					System.out.println("Preset: " + lastPreset);
					lastPresetChange = convertTime(System.currentTimeMillis()) + "(Uptime: " + deviceTimeText + ")";
					if (presetDataFile != null && presetSave) {
						String toWrite = convertTime(System.currentTimeMillis()) + "," + data + "\r\n";
						try {
							presetDataFile.write(toWrite.getBytes(Charset.forName("UTF-8")));
						} catch (IOException e) {
						}

					}
				} else if (messageType[0] == 'L') {
					incoming = mesaureDevice.readBytes(4, 100);
					if(debug){
						System.out.println("Measure Data: " + new String(incoming));
					}
					data = new String(incoming);
					if(Integer.parseInt(data) > lastLight + 2 || Integer.parseInt(data) < lastLight - 2){
						lastLight = Integer.parseInt(data);
						System.out.println("Light ADC: " + lastLight);
						if (measureDataFile != null && measureSave) {
							String toWrite = convertTime(System.currentTimeMillis()) + "," + data + "\r\n";
							try {
								measureDataFile.write(toWrite.getBytes(Charset.forName("UTF-8")));
							} catch (IOException e) {
							}

						}
					}
				}else if(messageType[0] == 'T'){
					if(deviceStartTime == 0){
						deviceStartTime = System.currentTimeMillis();
						deviceTimeText = sdf.format(0);
					}else{
						deviceTimeText = sdf.format(System.currentTimeMillis() - deviceStartTime);
					}
					mesaureDevice.writeBytes("T".getBytes(Charset.forName("ASCII")));
					mesaureDevice.writeBytes(deviceTimeText.getBytes(Charset.forName("ASCII")));
				}else if(messageType[0] == 'I'){
					deviceStartTime = 0;
					if(currentProgram != null){
						writeCurrentProgram();
					}
				}


			} catch (SerialPortException e) {
				e.printStackTrace();
			} catch (SerialPortTimeoutException e) {
				// nothing to do
			}

			command = "";
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			br.close();
			mesaureDevice.closePort();
			measureDataFile.close();
			presetDataFile.close();
		} catch (IOException | SerialPortException e) {

		}

	}

	public String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		return format.format(date);
	}
	
	public void writeCurrentProgram() throws SerialPortException{
		mesaureDevice.writeBytes(currentProgram.getHeader());
		for(byte[] presetToSet : currentProgram.getData()){
			mesaureDevice.writeBytes(presetToSet);
		}
	}
	
	public boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    int i = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}

	public static void main(String[] args) {

		LampSV lampsv;
		try {
			lampsv = new LampSV();
			lampsv.start();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

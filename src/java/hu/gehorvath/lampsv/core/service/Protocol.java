package hu.gehorvath.lampsv.core.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import hu.gehorvath.lampsv.core.Controller;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class Protocol {

	Logger logger = Logger.getLogger(Protocol.class);

	private boolean enabled = false;

	private SerialPort mesaureDevice;

	private String logfile;

	private FileOutputStream presetDataFile;
	private FileOutputStream measureDataFile;
	private String lastPresetChange = "";

	private SimpleDateFormat sdf;

	private Controller controller;

	private int lastLight;

	private boolean measureSave = false;

	private int lastPreset;

	private boolean presetSave = true;

	private String deviceTimeText;

	private long deviceStartTime;
	
	private boolean initialized = false;
	
	private BlockingQueue<String> messages = new LinkedBlockingQueue<String>();

	private ExecutorService newSingleThreadExecutor;

	public Protocol(Controller controller) {
		this.controller = controller;
	}

	public void init() throws SerialPortException, FileNotFoundException {
		mesaureDevice = new SerialPort(this.controller.getSerailPort());
		mesaureDevice.openPort();
		mesaureDevice.setParams(SerialPort.BAUDRATE_4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_2,
				SerialPort.PARITY_NONE);

		sdf = new SimpleDateFormat("HH:mm:ss");
		//sdf.setTimeZone(TimeZone.getTimeZone("CEST"));
		
		SimpleDateFormat logsdf = new SimpleDateFormat("MMDDHHmm");
		//logsdf.setTimeZone(TimeZone.getTimeZone("CEST"));
		try{
			File dir = new File("data\\" + controller.getName());
			if(!dir.exists()){
				dir.mkdir();
			}
			
			StringBuilder logfile = new StringBuilder();
			logfile.append("data\\");
			logfile.append(controller.getName());
			logfile.append("\\");
			logfile.append(logsdf.format(new Date(System.currentTimeMillis())) + ".dat");
			File datafile = new File(logfile.toString());
			try{
				datafile.createNewFile();
			}catch(IOException e1){
				logger.info("Log file already exists, but we dont care");
			}
			
			presetDataFile = new FileOutputStream(datafile);
		}catch(FileNotFoundException ex){
			logger.error(ex);
			throw ex;
		}
		
		
		// measureDataFile = new FileOutputStream(measureFileName);
		
		newSingleThreadExecutor = Executors.newSingleThreadExecutor();
		initialized = true;
		logger.info("Protocol initialized, controller: " + controller.getName());
	}

	private void work() {

		logger.info("Protocol starting with controller:" + controller.getName());
		byte[] incoming = new byte[5];
		byte[] messageType = new byte[5];
		String data = "";
		String command = "";
		enabled = true;
		// main cycle
		while (enabled) {

			// write
			try {
				if (!messages.isEmpty()) {
					command = messages.take();
					if (isNumeric(command)) {
						mesaureDevice.writeByte((byte) Integer.parseInt(command));
					} else if (command.contains("l")) {
						mesaureDevice.writeBytes(controller.getContProgram().getHeader());
						for (byte[] presetToSet : controller.getContProgram().getData()) {
							mesaureDevice.writeBytes(presetToSet);
						}
					}
				}
			} catch (SerialPortException e1) {
				logger.error(e1);
				enabled = false;
			} catch (InterruptedException e) {
				logger.error(e);
				enabled = false;
			}

			// read
			try {

				messageType = mesaureDevice.readBytes(1, 100);
				logger.debug("MessageType: " + new String(messageType));
				if (isNumeric(new String(messageType)))
					continue;

				if (messageType[0] == 'P') {
					incoming = mesaureDevice.readBytes(4, 100);
					logger.debug("Preset Data: " + new String(incoming));
					data = new String(incoming);
					lastPreset = Integer.parseInt(data);
					logger.debug("Preset: " + lastPreset);
					lastPresetChange = convertTime(System.currentTimeMillis()) + "(Uptime: " + deviceTimeText + ")";
					if (presetDataFile != null && presetSave) {
						String toWrite = convertTime(System.currentTimeMillis()) + "," + data + "\r\n";
						try {
							 presetDataFile.write(toWrite.getBytes(Charset.forName("UTF-8")));
						} catch (IOException e) {
							logger.error("Cant write data file!" + e);
							enabled = false;
						}

					}
				} else if (messageType[0] == 'L') {
					incoming = mesaureDevice.readBytes(4, 100);
					logger.debug("Measure Data: " + new String(incoming));
					data = new String(incoming);
					if (Integer.parseInt(data) > lastLight + 2 || Integer.parseInt(data) < lastLight - 2) {
						lastLight = Integer.parseInt(data);
						logger.debug("Light ADC: " + lastLight);
						if (measureDataFile != null && measureSave) {
							String toWrite = convertTime(System.currentTimeMillis()) + "," + data + "\r\n";
							try {
								measureDataFile.write(toWrite.getBytes(Charset.forName("UTF-8")));
							} catch (IOException e) {
								logger.error(e);
								enabled = false;
							}

						}
					}
				} else if (messageType[0] == 'T') {
					if (deviceStartTime == 0) {
						deviceStartTime = System.currentTimeMillis();
						deviceTimeText = sdf.format(0);
					} else {
						deviceTimeText = sdf.format(System.currentTimeMillis() - deviceStartTime);
					}
					mesaureDevice.writeBytes("T".getBytes(Charset.forName("ASCII")));
					mesaureDevice.writeBytes(deviceTimeText.getBytes(Charset.forName("ASCII")));
				} else if (messageType[0] == 'I') {
					deviceStartTime = 0;
					mesaureDevice.writeBytes(controller.getContProgram().getHeader());
					for (byte[] presetToSet : controller.getContProgram().getData()) {
						mesaureDevice.writeBytes(presetToSet);
					}
				}

			} catch (SerialPortException e) {
				logger.error(e);
				enabled = false;
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
		try{
			presetDataFile.close();
		}catch(IOException ex2){
			logger.info("Could not close file", ex2);	
		}
		
		logger.info("Protocol stopped with controller:" + controller.getName());
	}

	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		return format.format(date);
	}

	public void start() {
		if(!initialized) {
			throw new RuntimeException("Protocol not initialized");
		}else {
			newSingleThreadExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					work();
				}
			});
		}
	}

	public void stop() {
		this.enabled = false;
	}
	
	public void updateProgram() {
		messages.add("l");
	}
	
	public Controller getController() {
		return this.controller;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean isInit() {
		return this.initialized;
	}

}

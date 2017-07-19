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

	Properties prop = new Properties();
	
	Logger logger = Logger.getLogger(LampSV.class);

	public LampSV() throws FileNotFoundException, IOException {
		prop.load(new FileReader(new File("data/lampsv.prop")));
	}

	private void start() {
		Framework framework = new Framework();
		framework.startFramework();
		MainWindow mainscreen = new MainWindow();
		mainscreen.setVisible(true);
		
		logger.addAppender(new MainWindow.LogListener());
		
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

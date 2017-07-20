package hu.gehorvath.lampsv.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import hu.gehorvath.lampsv.core.Controller;
import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.Program;
import jssc.SerialPortException;

public class MainWindow {

	private MainWindowDataProvider mwdp = new MainWindowDataProvider();

	private JFrame frame;
	private JTextField jtfPresetID;
	private JTextField jtfLed1on;
	private JTextField jtfLed1off;
	private JTextField jtfLed2on;
	private JTextField jtfLed2off;
	private JTextField jtfLed3on;
	private JTextField jtfLed3off;
	private JTextField jtfLux;

	private static JTextArea logTextShow;

	JComboBox<Preset> jcPresets;
	JComboBox<Program> jcPrograms;
	JList<Preset> jlUnusedPresets;
	JList<Preset> jlUsedPresets;
	DefaultListModel<Preset> unusedListModel = new DefaultListModel<>();
	DefaultListModel<Preset> usedListModel = new DefaultListModel<>();

	JComboBox<Controller> jcControllers;
	JComboBox<String> jcSerialPorts;
	JComboBox<Program> jcControllerPrograms;
	JLabel jlLoggingStatus;
	JLabel jlSerialPortAvailable;
	JLabel jlLampInitializedReady;

	private JTextField tbProgramName;
	private JTextField tbProgramDesc;
	private JTextField tbControllerName;
	private JTable jtStatus;

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		initData();
	}

	public void setVisible(boolean visible) {
		this.frame.setVisible(visible);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 643, 433);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(tabbedPane, "name_972030312230151");

		JPanel presetsPanel = new JPanel();
		presetsPanel.setBackground(Color.GRAY);
		presetsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Presets", null, presetsPanel, null);

		JLabel lblSelectPresetTo = new JLabel("Select preset to modify");
		lblSelectPresetTo.setBounds(12, 13, 142, 14);

		jcPresets = new JComboBox<Preset>();
		jcPresets.setBounds(12, 33, 142, 20);

		JLabel lblPresetid = new JLabel("PresetID:");
		lblPresetid.setBounds(183, 13, 63, 14);

		jtfPresetID = new JTextField();
		jtfPresetID.setBounds(256, 10, 86, 20);
		jtfPresetID.addFocusListener(new IntegerValidator());
		jtfPresetID.setEnabled(false);
		jtfPresetID.setColumns(10);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(472, 240, 79, 23);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (((Preset) jcPresets.getSelectedItem()).getIntId() == -1) {
					Preset newPreset = ((Preset) jcPresets.getSelectedItem());
					newPreset.setLedTiminds(parseLeds());
					newPreset.setMeasuredLux(Integer.parseInt(jtfLux.getText()));
					mwdp.savePreset(newPreset, null);
					initData();
				} else {
					Preset newPreset = new Preset();
					newPreset.setLedTiminds(parseLeds());
					newPreset.setMeasuredLux(Integer.parseInt(jtfLux.getText()));
					newPreset.setPresetID(((Preset) jcPresets.getSelectedItem()).getIntId());
					mwdp.savePreset(newPreset, ((Preset) jcPresets.getSelectedItem()));
					initData();
				}
			}
		});

		JLabel lblLedOn = new JLabel("LED1 on:");
		lblLedOn.setBounds(183, 54, 63, 14);

		jtfLed1on = new JTextField();
		jtfLed1on.setBounds(256, 51, 86, 20);
		jtfLed1on.addFocusListener(new IntegerValidator());
		jtfLed1on.setColumns(10);

		jtfLed1off = new JTextField();
		jtfLed1off.setBounds(256, 89, 86, 20);
		jtfLed1off.addFocusListener(new IntegerValidator());
		jtfLed1off.setColumns(10);

		jtfLed2on = new JTextField();
		jtfLed2on.setBounds(256, 127, 86, 20);
		jtfLed2on.addFocusListener(new IntegerValidator());
		jtfLed2on.setColumns(10);

		jtfLed2off = new JTextField();
		jtfLed2off.setBounds(256, 165, 86, 20);
		jtfLed2off.addFocusListener(new IntegerValidator());
		jtfLed2off.setColumns(10);

		jtfLed3on = new JTextField();
		jtfLed3on.setBounds(256, 203, 86, 20);
		jtfLed3on.addFocusListener(new IntegerValidator());
		jtfLed3on.setColumns(10);

		jtfLed3off = new JTextField();
		jtfLed3off.setBounds(256, 241, 86, 20);
		jtfLed3off.addFocusListener(new IntegerValidator());
		jtfLed3off.setColumns(10);

		JLabel lblLedOff = new JLabel("LED1 off:");
		lblLedOff.setBounds(183, 92, 63, 14);

		JLabel lblLedOn_1 = new JLabel("LED2 on:");
		lblLedOn_1.setBounds(183, 130, 63, 14);

		JLabel lblLedOff_1 = new JLabel("LED2 off:");
		lblLedOff_1.setBounds(183, 168, 63, 14);

		JLabel lblLedOn_2 = new JLabel("LED3 on:");
		lblLedOn_2.setBounds(183, 206, 63, 14);

		JLabel lblLedOff_2 = new JLabel("LED3 off:");
		lblLedOff_2.setBounds(183, 244, 63, 14);

		jtfLux = new JTextField();
		jtfLux.setBounds(472, 10, 86, 20);
		jtfLux.addFocusListener(new IntegerValidator());
		jtfLux.setColumns(10);

		JLabel lblMeasuredLux = new JLabel("Measured LUX:");
		lblMeasuredLux.setBounds(370, 13, 92, 14);
		presetsPanel.setLayout(null);
		presetsPanel.add(jcPresets);
		presetsPanel.add(lblSelectPresetTo);
		presetsPanel.add(lblLedOn);
		presetsPanel.add(lblPresetid);
		presetsPanel.add(lblLedOff);
		presetsPanel.add(lblLedOn_1);
		presetsPanel.add(lblLedOff_1);
		presetsPanel.add(lblLedOn_2);
		presetsPanel.add(lblLedOff_2);
		presetsPanel.add(jtfLed3on);
		presetsPanel.add(jtfLed2off);
		presetsPanel.add(jtfLed2on);
		presetsPanel.add(jtfLed1off);
		presetsPanel.add(jtfLed1on);
		presetsPanel.add(jtfLed3off);
		presetsPanel.add(btnSave);
		presetsPanel.add(jtfPresetID);
		presetsPanel.add(lblMeasuredLux);
		presetsPanel.add(jtfLux);

		JPanel programsPanel = new JPanel();
		programsPanel.setBackground(Color.GRAY);
		programsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Programs", null, programsPanel, null);

		jcPrograms = new JComboBox<Program>();
		jcPrograms.setBounds(12, 33, 138, 20);
		jcPrograms.addItem(new Program());

		jlUnusedPresets = new JList<Preset>(unusedListModel);
		jlUnusedPresets.setBounds(196, 35, 138, 244);

		jlUsedPresets = new JList<Preset>(usedListModel);
		jlUsedPresets.setBounds(344, 35, 136, 244);

		JButton jbAddPresetToUsed = new JButton("Add");
		jbAddPresetToUsed.setBounds(506, 32, 72, 23);
		jbAddPresetToUsed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUnusedPresets.getSelectedValue();
				unusedListModel.removeElement(selectedPres);
				usedListModel.addElement(selectedPres);
			}
		});

		JButton jbRemovePresetFromUsed = new JButton("Del");
		jbRemovePresetFromUsed.setBounds(506, 66, 72, 23);
		jbRemovePresetFromUsed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUsedPresets.getSelectedValue();
				usedListModel.removeElement(selectedPres);
				unusedListModel.addElement(selectedPres);
			}
		});

		JButton btSaveProgram = new JButton("Save");
		btSaveProgram.setBounds(506, 256, 72, 23);
		btSaveProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (((Program) jcPrograms.getSelectedItem()).getIntID() == -1) {
					Program newProgram = ((Program) jcPrograms.getSelectedItem());
					newProgram.setcharacterCode(tbProgramName.getText().getBytes()[0]);
					newProgram.setDescription(tbProgramDesc.getText());
					List<Preset> newPresetList = new ArrayList<Preset>();
					for (int i = 0; i <= usedListModel.getSize() - 1; i++) {
						newPresetList.add(usedListModel.get(i));
					}
					newProgram.setPresets(newPresetList);
					mwdp.saveProgram(newProgram, null);
					initData();
				} else {
					Program newProgram = new Program();
					newProgram.setcharacterCode(tbProgramName.getText().getBytes()[0]);
					newProgram.setDescription(tbProgramDesc.getText());
					List<Preset> newPresetList = new ArrayList<Preset>();
					for (int i = 0; i <= usedListModel.getSize() - 1; i++) {
						newPresetList.add(usedListModel.get(i));
					}
					newProgram.setPresets(newPresetList);

					mwdp.saveProgram(newProgram, ((Program) jcPrograms.getSelectedItem()));
					initData();
				}
			}
		});

		tbProgramName = new JTextField();
		tbProgramName.setBounds(12, 145, 138, 20);
		tbProgramName.setColumns(10);
		tbProgramName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				if (e.getSource() instanceof JTextField) {
					JTextField textField = (JTextField) e.getSource();
					if (textField.getText() != null && !textField.getText().equals("")) {
						if (textField.getText().length() > 1) {
							JOptionPane.showMessageDialog(null, "Character code of program must be only one character!",
									"Format Error", JOptionPane.ERROR_MESSAGE);
							textField.setText("");
						}
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		JLabel lblName = new JLabel("Character ID (unique)");
		lblName.setBounds(12, 125, 138, 14);

		tbProgramDesc = new JTextField();
		tbProgramDesc.setBounds(12, 200, 138, 20);
		tbProgramDesc.setColumns(10);

		JLabel lblId = new JLabel("Description");
		lblId.setBounds(12, 180, 138, 14);

		JLabel lblSelectProgram = new JLabel("Select program");
		lblSelectProgram.setBounds(12, 13, 138, 14);

		JButton jbMoveUp = new JButton("Up");
		jbMoveUp.setBounds(506, 121, 72, 23);
		jbMoveUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUsedPresets.getSelectedValue();
				int currentIndex = usedListModel.indexOf(selectedPres);
				if (currentIndex >= 1) {
					usedListModel.removeElement(selectedPres);
					usedListModel.add(currentIndex - 1, selectedPres);
				}
			}
		});

		JButton jbMoveDown = new JButton("Down");
		jbMoveDown.setBounds(506, 157, 72, 23);
		jbMoveUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUsedPresets.getSelectedValue();
				int currentIndex = usedListModel.indexOf(selectedPres);
				if (currentIndex < usedListModel.size()) {
					usedListModel.removeElement(selectedPres);
					usedListModel.add(currentIndex + 1, selectedPres);
				}
			}
		});
		programsPanel.setLayout(null);
		programsPanel.add(tbProgramName);
		programsPanel.add(lblName);
		programsPanel.add(lblSelectProgram);
		programsPanel.add(jcPrograms);
		programsPanel.add(jlUnusedPresets);
		programsPanel.add(jlUsedPresets);
		programsPanel.add(btSaveProgram);
		programsPanel.add(jbRemovePresetFromUsed);
		programsPanel.add(jbAddPresetToUsed);
		programsPanel.add(jbMoveUp);
		programsPanel.add(jbMoveDown);
		programsPanel.add(tbProgramDesc);
		programsPanel.add(lblId);

		JLabel lblAllPresets = new JLabel("All presets");
		lblAllPresets.setBounds(196, 13, 138, 14);
		programsPanel.add(lblAllPresets);

		JLabel lblPresetsUsedIn = new JLabel("Presets used in program");
		lblPresetsUsedIn.setBounds(344, 13, 136, 14);
		programsPanel.add(lblPresetsUsedIn);

		JPanel controllersPanel = new JPanel();
		controllersPanel.setBackground(Color.GRAY);
		controllersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Controllers", null, controllersPanel, null);

		jcControllers = new JComboBox<Controller>();
		jcControllers.setBounds(12, 34, 122, 20);
		jcControllers.addActionListener(new SelectedControllerItemChanged());

		JLabel lblSelectController = new JLabel("Select controller");
		lblSelectController.setBounds(12, 13, 122, 14);

		JLabel lblName_1 = new JLabel("Name");
		lblName_1.setBounds(152, 13, 196, 14);

		tbControllerName = new JTextField();
		tbControllerName.setBounds(152, 34, 196, 20);
		tbControllerName.setColumns(10);

		JLabel lblSerialPort = new JLabel("Serial port");
		lblSerialPort.setBounds(152, 65, 196, 14);

		jcSerialPorts = new JComboBox<String>();
		jcSerialPorts.setBounds(152, 90, 196, 20);

		JLabel lblNewLabel = new JLabel("Program");
		lblNewLabel.setBounds(152, 121, 196, 14);

		jcControllerPrograms = new JComboBox<Program>();
		jcControllerPrograms.setBounds(152, 146, 196, 20);

		JButton btControllerSave = new JButton("Save");
		btControllerSave.setBounds(453, 303, 124, 23);
		btControllerSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (((Controller) jcControllers.getSelectedItem()).getId() == -1) {
					Controller newController = ((Controller) jcControllers.getSelectedItem());
					newController.setControllerName(tbControllerName.getText());
					newController.setProgram((Program) jcControllerPrograms.getSelectedItem());
					newController.setSerialPort((String) jcSerialPorts.getSelectedItem());
					mwdp.saveController(newController, null);
					initData();
				} else {
					Controller newController = new Controller();
					newController.setControllerName(tbControllerName.getText());
					newController.setProgram((Program) jcControllerPrograms.getSelectedItem());
					newController.setSerialPort((String) jcSerialPorts.getSelectedItem());
					mwdp.saveController(newController, ((Controller) jcControllers.getSelectedItem()));
					initData();
				}
			}
		});

		JButton btStartLogging = new JButton("Start measurement");
		btStartLogging.setBounds(446, 33, 131, 23);
		btStartLogging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mwdp.startMeasurement((Controller) jcControllers.getSelectedItem());
			}
		});

		JButton btStopLogging = new JButton("Stop measurement");
		btStopLogging.setBounds(446, 74, 131, 23);
		btStopLogging.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mwdp.stopMeasurement((Controller) jcControllers.getSelectedItem());
			}
		});

		JButton btLoadProgram = new JButton("Load program to lamp");
		btLoadProgram.setBounds(446, 115, 131, 23);
		btLoadProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mwdp.loadProgramToController((Controller) jcControllers.getSelectedItem());
			}
		});

		jlLoggingStatus = new JLabel("Measurement info: N/A");
		jlLoggingStatus.setBounds(152, 253, 238, 14);
		jlLoggingStatus.setForeground(Color.BLACK);

		jlSerialPortAvailable = new JLabel("SerialPort info: N/A");
		jlSerialPortAvailable.setBounds(152, 203, 238, 14);
		jlSerialPortAvailable.setForeground(Color.BLACK);

		JButton btnInitializeLamp = new JButton("Initialize lamp");
		btnInitializeLamp.setBounds(446, 156, 131, 23);
		btnInitializeLamp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					mwdp.initController((Controller) jcControllers.getSelectedItem());
					jlLampInitializedReady.setText("The selected controller is initialized!");
					jlLampInitializedReady.setForeground(Color.GREEN);
				} catch (SerialPortException ex1) {
					jlLampInitializedReady.setText("The selected controller is NOT initialized! (Serial Port problem)");
					jlLampInitializedReady.setForeground(Color.RED);
				} catch (FileNotFoundException ex2) {
					jlLampInitializedReady.setText("The selected controller is NOT initialized! (File problems)");
					jlLampInitializedReady.setForeground(Color.RED);
				}
			}
		});

		jlLampInitializedReady = new JLabel("Initialization info: N/A");
		jlLampInitializedReady.setBounds(152, 228, 238, 14);
		jlLampInitializedReady.setForeground(Color.BLACK);

		JButton btnRecheckPort = new JButton("Recheck port");
		btnRecheckPort.setBounds(446, 199, 131, 23);
		btnRecheckPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> availableSerialPorts = mwdp.getAvailableSerialPorts();
				if (availableSerialPorts.contains((String) jcSerialPorts.getSelectedItem())) {
					jlSerialPortAvailable.setText("The selected serial port is available!");
					jlSerialPortAvailable.setForeground(Color.GREEN);
				} else {
					jlSerialPortAvailable.setText("The selected serial port is NOT available!");
					jlSerialPortAvailable.setForeground(Color.RED);
				}
			}
		});
		controllersPanel.setLayout(null);
		controllersPanel.add(lblSelectController);
		controllersPanel.add(jcControllers);
		controllersPanel.add(lblName_1);
		controllersPanel.add(lblSerialPort);
		controllersPanel.add(lblNewLabel);
		controllersPanel.add(jlSerialPortAvailable);
		controllersPanel.add(jlLampInitializedReady);
		controllersPanel.add(jlLoggingStatus);
		controllersPanel.add(jcSerialPorts);
		controllersPanel.add(tbControllerName);
		controllersPanel.add(jcControllerPrograms);
		controllersPanel.add(btnRecheckPort);
		controllersPanel.add(btStopLogging);
		controllersPanel.add(btStartLogging);
		controllersPanel.add(btLoadProgram);
		controllersPanel.add(btnInitializeLamp);
		controllersPanel.add(btControllerSave);

		Panel statePanel = new Panel();
		statePanel.setBackground(Color.GRAY);
		tabbedPane.addTab("State", null, statePanel, null);
		statePanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 485, 350);
		statePanel.add(scrollPane);

		jtStatus = new JTable(new StatusTableModel());
		jtStatus.setDefaultRenderer(String.class, new StatusCellRenderer());
		jtStatus.setRowHeight(30);
		scrollPane.setViewportView(jtStatus);

		JPanel logPanel = new JPanel();
		logPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Log", null, logPanel, null);
		logPanel.setLayout(new BorderLayout(2, 2));

		logTextShow = new JTextArea();
		logPanel.add(logTextShow, BorderLayout.CENTER);

		jcPresets.addActionListener(new SelectedPresetItemChanged());
		jcPrograms.addActionListener(new SelectedProgramItemChanged());
	}

	private void initData() {

		// fill presets
		jcPresets.removeAllItems();
		jcPresets.addItem(new Preset());
		List<Preset> presets = mwdp.getPresets();
		for (Preset x : presets) {
			jcPresets.addItem(x);
		}

		// fill programs
		jcPrograms.removeAllItems();
		jcPrograms.addItem(new Program());
		unusedListModel.clear();
		usedListModel.clear();

		List<Program> programs = mwdp.getPrograms();
		for (Program x : programs) {
			jcPrograms.addItem((Program) x);
		}
		for (Preset pres : presets) {
			unusedListModel.addElement(pres);
		}

		// fill controllers
		List<Controller> controllers = mwdp.getControllers();
		jcControllers.removeAllItems();
		jcControllers.addItem(new Controller());

		jcControllerPrograms.removeAllItems();
		for (Controller cont : controllers) {
			jcControllers.addItem(cont);
		}
		List<String> availPorts = mwdp.getAvailableSerialPorts();

		for (String serialPort : availPorts) {
			for (Controller cont : controllers) {
				if (availPorts.contains(cont.getSerailPort())) {
					break;
				}
				jcSerialPorts.addItem(serialPort);
			}
		}
		for (Program prog : programs) {
			jcControllerPrograms.addItem(prog);
		}
	}

	private int[] parseLeds() {
		int[] leds = new int[6];
		leds[0] = Integer.parseInt(jtfLed1on.getText());
		leds[1] = Integer.parseInt(jtfLed2on.getText());
		leds[2] = Integer.parseInt(jtfLed3on.getText());

		leds[3] = Integer.parseInt(jtfLed1off.getText());
		leds[4] = Integer.parseInt(jtfLed2off.getText());
		leds[5] = Integer.parseInt(jtfLed3off.getText());

		return leds;
	}

	public static class LogListener extends AppenderSkeleton {

		@Override
		public void close() {

		}

		@Override
		public boolean requiresLayout() {
			return false;
		}

		@Override
		protected void append(LoggingEvent arg0) {
			logTextShow.append(arg0.getLevel().toString() + " - ");
			logTextShow.append(arg0.getClass().toString() + " - ");
			logTextShow.append(new SimpleDateFormat("YYYY/dd/MM HH:mm:ss").format(arg0.getTimeStamp()) + " - ");
			logTextShow.append(arg0.getMessage().toString());
			logTextShow.append("\r\n");
		}

	}

	private class SelectedPresetItemChanged implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<Preset> box = (JComboBox<Preset>) e.getSource();
			Preset selectedPreset = (Preset) box.getSelectedItem();
			if (selectedPreset != null) {
				jtfPresetID.setText(selectedPreset.getID());
				int[] leds = selectedPreset.getLEDValues();
				jtfLed1on.setText(leds[0] + "");
				jtfLed1off.setText(leds[1] + "");
				jtfLed2on.setText(leds[2] + "");
				jtfLed2off.setText(leds[3] + "");
				jtfLed3on.setText(leds[4] + "");
				jtfLed3off.setText(leds[5] + "");

				jtfLux.setText(selectedPreset.getLUX() + "");
			}
		}
	}

	private class SelectedProgramItemChanged implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JComboBox<?>) {
				JComboBox<Program> box = (JComboBox<Program>) e.getSource();
				Program selectedProgram = (Program) box.getSelectedItem();
				if (selectedProgram != null) {
					tbProgramName.setText(selectedProgram.getProgramCode());
					tbProgramDesc.setText(selectedProgram.getDesc());
					usedListModel.clear();
					unusedListModel.clear();
					List<Preset> allPresetList = mwdp.getPresets();
					List<Preset> selectedPresetList = selectedProgram.getPresetList();
					for (Preset pres : allPresetList) {
						if (selectedPresetList.contains(pres)) {
							usedListModel.addElement(pres);
						} else {
							unusedListModel.addElement(pres);
						}
					}
				}
			}
		}

	}

	private class SelectedControllerItemChanged implements ActionListener {

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof JComboBox<?>) {
				JComboBox<Controller> box = (JComboBox<Controller>) e.getSource();
				Controller selectedController = (Controller) box.getSelectedItem();
				if (selectedController != null) {
					tbControllerName.setText(selectedController.getName());

					jcSerialPorts.removeAllItems();
					jcSerialPorts.addItem(selectedController.getSerailPort());
					List<String> availPorts = mwdp.getAvailableSerialPorts();
					for (String port : availPorts) {
						jcSerialPorts.addItem(port);
					}
					jcControllerPrograms.setSelectedItem(selectedController.getContProgram());
					if (selectedController.getId() != -1) {
						if (availPorts.contains(selectedController.getSerailPort())) {
							jlSerialPortAvailable.setText("The selected serial port is available!");
							jlSerialPortAvailable.setForeground(Color.GREEN);
						} else {
							jlSerialPortAvailable.setText("The selected serial port is NOT available!");
							jlSerialPortAvailable.setForeground(Color.RED);
						}
						if (mwdp.isInit(selectedController)) {
							jlLampInitializedReady.setText("The selected controller is initialized!");
							jlLampInitializedReady.setForeground(Color.GREEN);
						} else {
							jlLampInitializedReady.setText("The selected controller is NOT initialized!");
							jlLampInitializedReady.setForeground(Color.RED);
						}
						if (mwdp.isMeasurementRunning(selectedController)) {
							jlLoggingStatus.setText("The selected controller is started!");
							jlLoggingStatus.setForeground(Color.GREEN);
						} else {
							jlLoggingStatus.setText("The selected controller is stopped!");
							jlLoggingStatus.setForeground(Color.RED);
						}
					}

				}
			}

		}

	}

	private class IntegerValidator implements FocusListener {

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource() instanceof JTextField) {
				JTextField textField = (JTextField) e.getSource();
				if (textField.getText() != null && !textField.getText().equals("")) {
					try {
						Integer.parseInt(textField.getText());
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "Intput field value can only be numbers!", "Format Error",
								JOptionPane.ERROR_MESSAGE);
						textField.setText("");
					}
				}
			}
		}

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	private class StatusTableModel extends AbstractTableModel {

		List<Controller> controllers;

		String[] columnNames = new String[] { "Controller", "Serial status", "Init status", "Measurement status" };

		public StatusTableModel() {
			super();
			this.controllers = mwdp.getControllers();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public int getRowCount() {
			return controllers.size();
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			if (arg1 == 0) {
				return controllers.get(arg0).getName();
			} else if (arg1 == 1) {
				List<String> availPorts = mwdp.getAvailableSerialPorts();
				if (availPorts.contains(controllers.get(arg0).getSerailPort())) {
					return "OK";
				} else {
					return "ERROR";
				}
			} else if (arg1 == 2) {
				if (mwdp.isInit(controllers.get(arg0))) {
					return "OK";
				} else {
					return "NO";
				}
			} else if (arg1 == 3) {
				if (mwdp.isMeasurementRunning(controllers.get(arg0))) {
					return "RUNNING";
				} else {
					return "STOPPED";
				}
			}
			return "WTF";
		}
		
		@Override
		public Class getColumnClass(int c) {
	        return String.class;
	    }

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

	}

	public class StatusCellRenderer implements TableCellRenderer{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int col) {

			JLabel label = new JLabel((String)value);
			JPanel panel = new JPanel();
			if (value.equals("OK") || value.equals("RUNNING")) {
				panel.setBackground(Color.GREEN);
			} else if(value.equals("ERROR") || value.equals("NO") || value.equals("STOPPED")){
				panel.setBackground(Color.RED);
			}
			
			panel.add(label);

			// Return the JLabel which renders the cell.
			return panel;

		}
	}
}

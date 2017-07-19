package hu.gehorvath.lampsv.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.core.Program;

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

	private JTextField tbProgramName;
	private JTextField tbProgramDesc;
	private JTextField tfControllerName;

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
		frame.setBounds(100, 100, 718, 387);
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

		jcPresets = new JComboBox<Preset>();

		JLabel lblPresetid = new JLabel("PresetID:");

		jtfPresetID = new JTextField();
		jtfPresetID.addFocusListener(new IntegerValidator());
		jtfPresetID.setEnabled(false);
		jtfPresetID.setColumns(10);

		JButton btnSave = new JButton("Save");
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

		jtfLed1on = new JTextField();
		jtfLed1on.addFocusListener(new IntegerValidator());
		jtfLed1on.setColumns(10);

		jtfLed1off = new JTextField();
		jtfLed1off.addFocusListener(new IntegerValidator());
		jtfLed1off.setColumns(10);

		jtfLed2on = new JTextField();
		jtfLed2on.addFocusListener(new IntegerValidator());
		jtfLed2on.setColumns(10);

		jtfLed2off = new JTextField();
		jtfLed2off.addFocusListener(new IntegerValidator());
		jtfLed2off.setColumns(10);

		jtfLed3on = new JTextField();
		jtfLed3on.addFocusListener(new IntegerValidator());
		jtfLed3on.setColumns(10);

		jtfLed3off = new JTextField();
		jtfLed3off.addFocusListener(new IntegerValidator());
		jtfLed3off.setColumns(10);

		JLabel lblLedOff = new JLabel("LED1 off:");

		JLabel lblLedOn_1 = new JLabel("LED2 on:");

		JLabel lblLedOff_1 = new JLabel("LED2 off:");

		JLabel lblLedOn_2 = new JLabel("LED3 on:");

		JLabel lblLedOff_2 = new JLabel("LED3 off:");

		jtfLux = new JTextField();
		jtfLux.addFocusListener(new IntegerValidator());
		jtfLux.setColumns(10);

		JLabel lblMeasuredLux = new JLabel("Measured LUX:");
		GroupLayout gl_presetsPanel = new GroupLayout(presetsPanel);
		gl_presetsPanel.setHorizontalGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_presetsPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(jcPresets, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(lblSelectPresetTo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGap(41)
						.addGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING).addComponent(lblLedOn)
								.addComponent(lblPresetid).addComponent(lblLedOff).addComponent(lblLedOn_1)
								.addComponent(lblLedOff_1).addComponent(lblLedOn_2).addComponent(lblLedOff_2))
						.addGap(27)
						.addGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(jtfLed3on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jtfLed2off, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jtfLed2on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jtfLed1off, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jtfLed1on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_presetsPanel.createSequentialGroup()
												.addComponent(jtfLed3off, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 79,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(Alignment.LEADING,
												gl_presetsPanel.createSequentialGroup()
														.addComponent(jtfPresetID, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(lblMeasuredLux).addGap(18)
														.addComponent(jtfLux, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(32, Short.MAX_VALUE)));
		gl_presetsPanel.setVerticalGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_presetsPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_presetsPanel
						.createSequentialGroup()
						.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(jtfPresetID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPresetid).addComponent(lblMeasuredLux).addComponent(jtfLux,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING).addGroup(gl_presetsPanel
								.createSequentialGroup()
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed1on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOn))
								.addGap(18)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed1off, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOff))
								.addGap(18)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed2on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOn_1))
								.addGap(18)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed2off, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOff_1))
								.addGap(18)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed3on, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOn_2))
								.addGap(18)
								.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jtfLed3off, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblLedOff_2)))
								.addGroup(gl_presetsPanel.createSequentialGroup().addComponent(btnSave)
										.addPreferredGap(ComponentPlacement.RELATED))))
						.addGroup(gl_presetsPanel.createSequentialGroup().addComponent(lblSelectPresetTo)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(jcPresets,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
				.addGap(81)));
		presetsPanel.setLayout(gl_presetsPanel);

		JPanel programsPanel = new JPanel();
		programsPanel.setBackground(Color.GRAY);
		programsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Programs", null, programsPanel, null);

		jcPrograms = new JComboBox<Program>();
		jcPrograms.addItem(new Program());

		jlUnusedPresets = new JList<Preset>(unusedListModel);

		jlUsedPresets = new JList<Preset>(usedListModel);

		JButton jbAddPresetToUsed = new JButton("Add");
		jbAddPresetToUsed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUnusedPresets.getSelectedValue();
				unusedListModel.removeElement(selectedPres);
				usedListModel.addElement(selectedPres);
			}
		});

		JButton jbRemovePresetFromUsed = new JButton("Del");
		jbRemovePresetFromUsed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Preset selectedPres = jlUsedPresets.getSelectedValue();
				usedListModel.removeElement(selectedPres);
				unusedListModel.addElement(selectedPres);
			}
		});

		JButton btSaveProgram = new JButton("Save");
		btSaveProgram.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (((Program) jcPrograms.getSelectedItem()).getCharacterCode() == 0x00) {
					Program newProgram = ((Program) jcPrograms.getSelectedItem());
					newProgram.setcharacterCode(tbProgramName.getText().getBytes()[0]);
					newProgram.setDescription(tbProgramDesc.getText());
					List<Preset> newPresetList = new ArrayList<Preset>();
					for(int i = 0; i <= usedListModel.getSize() - 1; i++) {
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
					for(int i = 0; i <= usedListModel.getSize() - 1; i++) {
						newPresetList.add(usedListModel.get(i));
					}
					newProgram.setPresets(newPresetList);
					
					mwdp.saveProgram(newProgram, ((Program) jcPrograms.getSelectedItem()));
					initData();
				}
			}
		});

		tbProgramName = new JTextField();
		tbProgramName.setColumns(10);

		JLabel lblName = new JLabel("Character ID (unique)");

		tbProgramDesc = new JTextField();
		tbProgramDesc.setColumns(10);

		JLabel lblId = new JLabel("Description");

		JLabel lblSelectProgram = new JLabel("Select program");

		JButton jbMoveUp = new JButton("Up");
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

		GroupLayout gl_programsPanel = new GroupLayout(programsPanel);
		gl_programsPanel.setHorizontalGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_programsPanel.createSequentialGroup().addContainerGap().addGroup(gl_programsPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_programsPanel.createSequentialGroup()
								.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(tbProgramName, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblName).addComponent(lblSelectProgram).addComponent(jcPrograms,
												GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
								.addGap(46)
								.addComponent(jlUnusedPresets, GroupLayout.PREFERRED_SIZE, 138,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(
										jlUsedPresets, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btSaveProgram, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(jbRemovePresetFromUsed, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jbAddPresetToUsed, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jbMoveUp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(jbMoveDown, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)))
						.addComponent(tbProgramDesc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblId)).addContainerGap(123, Short.MAX_VALUE)));
		gl_programsPanel.setVerticalGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_programsPanel.createSequentialGroup().addGroup(gl_programsPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_programsPanel.createSequentialGroup().addGap(16)
								.addGroup(gl_programsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(jlUsedPresets, GroupLayout.PREFERRED_SIZE, 244,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jlUnusedPresets, GroupLayout.PREFERRED_SIZE, 244,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_programsPanel.createSequentialGroup().addGroup(gl_programsPanel
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_programsPanel.createSequentialGroup().addGap(20)
										.addComponent(jbAddPresetToUsed).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(jbRemovePresetFromUsed))
								.addGroup(gl_programsPanel.createSequentialGroup().addContainerGap()
										.addComponent(lblSelectProgram).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(jcPrograms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_programsPanel.createSequentialGroup().addGap(51)
												.addComponent(lblName).addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tbProgramName, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGap(15).addComponent(lblId)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(tbProgramDesc,
														GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_programsPanel.createSequentialGroup().addGap(42)
												.addComponent(jbMoveUp).addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(jbMoveDown)))
								.addGap(10).addComponent(btSaveProgram)))
						.addContainerGap(66, Short.MAX_VALUE)));
		programsPanel.setLayout(gl_programsPanel);

		JPanel controllersPanel = new JPanel();
		controllersPanel.setBackground(Color.GRAY);
		controllersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Controllers", null, controllersPanel, null);

		JComboBox jcControllers = new JComboBox();

		JLabel lblSelectController = new JLabel("Select controller");

		JLabel lblName_1 = new JLabel("Name");

		tfControllerName = new JTextField();
		tfControllerName.setColumns(10);

		JLabel lblSerialPort = new JLabel("Serial port");

		JComboBox cbSerialPorts = new JComboBox();

		JLabel lblNewLabel = new JLabel("Program");

		JComboBox jcControllerPrograms = new JComboBox();
		
		JButton btControllerSave = new JButton("Save");
		
		JButton btStartLogging = new JButton("Start measurement");
		
		JButton btStopLogging = new JButton("Stop measurement");
		
		JButton btLoadProgram = new JButton("Load program to lamp");
		
		JLabel jlLoggingStatus = new JLabel("Measurement in progress");
		jlLoggingStatus.setBackground(Color.GREEN);
		jlLoggingStatus.setForeground(Color.GREEN);
		
		JLabel jlSerialPortAvailable = new JLabel("Serial port available");
		jlSerialPortAvailable.setForeground(Color.GREEN);
		
		JButton btnInitializeLamp = new JButton("Initialize lamp");
		
		JLabel lbLampInitializedReady = new JLabel("Lamp initialized, ready to start");
		lbLampInitializedReady.setForeground(Color.GREEN);
		GroupLayout gl_controllersPanel = new GroupLayout(controllersPanel);
		gl_controllersPanel.setHorizontalGroup(
			gl_controllersPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controllersPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_controllersPanel.createSequentialGroup()
							.addGroup(gl_controllersPanel.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(jcControllers, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblSelectController, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(63)
							.addGroup(gl_controllersPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(jcControllerPrograms, Alignment.LEADING, 0, 152, Short.MAX_VALUE)
								.addComponent(tfControllerName, Alignment.LEADING, 152, 152, 152)
								.addComponent(lblName_1, Alignment.LEADING)
								.addComponent(lblSerialPort, Alignment.LEADING)
								.addComponent(cbSerialPorts, Alignment.LEADING, 0, 152, Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.LEADING))
							.addGap(108)
							.addGroup(gl_controllersPanel.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btLoadProgram, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btStopLogging, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btStartLogging, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnInitializeLamp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jlSerialPortAvailable)
								.addComponent(jlLoggingStatus)
								.addComponent(lbLampInitializedReady))
							.addGap(200))
						.addGroup(gl_controllersPanel.createSequentialGroup()
							.addComponent(btControllerSave)
							.addGap(67))))
		);
		gl_controllersPanel.setVerticalGroup(
			gl_controllersPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controllersPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectController)
						.addComponent(lblName_1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jcControllers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfControllerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btStartLogging))
					.addGap(18)
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSerialPort)
						.addComponent(btStopLogging))
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_controllersPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbSerialPorts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jcControllerPrograms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_controllersPanel.createSequentialGroup()
							.addGap(16)
							.addComponent(btLoadProgram)
							.addGap(18)
							.addComponent(btnInitializeLamp)))
					.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
					.addComponent(jlSerialPortAvailable)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lbLampInitializedReady)
					.addGap(8)
					.addComponent(jlLoggingStatus)
					.addGap(29)
					.addComponent(btControllerSave)
					.addContainerGap())
		);
		controllersPanel.setLayout(gl_controllersPanel);

		Panel statePanel = new Panel();
		tabbedPane.addTab("State", null, statePanel, null);

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

		//fill presets
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
}

package hu.gehorvath.lampsv.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
import hu.gehorvath.lampsv.ui.data.ICallback;

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
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

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
		jcPresets.addItem(new Preset());

		JLabel lblPresetid = new JLabel("PresetID:");

		jtfPresetID = new JTextField();
		jtfPresetID.setEnabled(false);
		jtfPresetID.setColumns(10);

		JButton btnSave = new JButton("Save");

		JLabel lblLedOn = new JLabel("LED1 on:");

		jtfLed1on = new JTextField();
		jtfLed1on.setColumns(10);

		jtfLed1off = new JTextField();
		jtfLed1off.setColumns(10);

		jtfLed2on = new JTextField();
		jtfLed2on.setColumns(10);

		jtfLed2off = new JTextField();
		jtfLed2off.setColumns(10);

		jtfLed3on = new JTextField();
		jtfLed3on.setColumns(10);

		jtfLed3off = new JTextField();
		jtfLed3off.setColumns(10);

		JLabel lblLedOff = new JLabel("LED1 off:");

		JLabel lblLedOn_1 = new JLabel("LED2 on:");

		JLabel lblLedOff_1 = new JLabel("LED2 off:");

		JLabel lblLedOn_2 = new JLabel("LED3 on:");

		JLabel lblLedOff_2 = new JLabel("LED3 off:");

		jtfLux = new JTextField();
		jtfLux.setColumns(10);

		JLabel lblMeasuredLux = new JLabel("Measured LUX:");
		GroupLayout gl_presetsPanel = new GroupLayout(presetsPanel);
		gl_presetsPanel
				.setHorizontalGroup(
						gl_presetsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_presetsPanel.createSequentialGroup().addContainerGap()
										.addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING, false)
												.addComponent(jcPresets, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(lblSelectPresetTo, Alignment.LEADING,
														GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										.addGap(41)
										.addGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblLedOn).addComponent(lblPresetid)
												.addComponent(lblLedOff).addComponent(lblLedOn_1)
												.addComponent(lblLedOff_1).addComponent(lblLedOn_2)
												.addComponent(lblLedOff_2))
										.addGap(27)
										.addGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(jtfLed3on, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jtfLed2off, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jtfLed2on, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jtfLed1off, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(jtfLed1on, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING, false)
														.addGroup(gl_presetsPanel.createSequentialGroup()
																.addComponent(jtfLed3off, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(btnSave,
																		GroupLayout.PREFERRED_SIZE, 79,
																		GroupLayout.PREFERRED_SIZE))
														.addGroup(Alignment.LEADING, gl_presetsPanel
																.createSequentialGroup()
																.addComponent(jtfPresetID, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(18).addComponent(lblMeasuredLux).addGap(18)
																.addComponent(jtfLux, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))))
										.addContainerGap(32, Short.MAX_VALUE)));
		gl_presetsPanel.setVerticalGroup(gl_presetsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_presetsPanel.createSequentialGroup().addContainerGap().addGroup(gl_presetsPanel
						.createParallelGroup(Alignment.LEADING).addGroup(gl_presetsPanel
								.createSequentialGroup().addGroup(
										gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(jtfPresetID, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblPresetid).addComponent(lblMeasuredLux)
												.addComponent(
														jtfLux, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(18).addGroup(gl_presetsPanel.createParallelGroup(Alignment.TRAILING).addGroup(
										gl_presetsPanel
												.createSequentialGroup().addGroup(gl_presetsPanel
														.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed1on, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLedOn))
												.addGap(18)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed1off, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLedOff))
												.addGap(18)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed2on, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLedOn_1))
												.addGap(18)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed2off, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLedOff_1))
												.addGap(18)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed3on, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLedOn_2))
												.addGap(18)
												.addGroup(gl_presetsPanel.createParallelGroup(Alignment.BASELINE)
														.addComponent(jtfLed3off, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
				
				JList list = new JList();
				list.setForeground(Color.WHITE);
				
				JList list_1 = new JList();
				
				JButton button = new JButton("Add");
				
				JButton button_1 = new JButton("Del");
				
				JButton btnNewButton = new JButton("Save");
				
				textField = new JTextField();
				textField.setColumns(10);
				
				JLabel lblName = new JLabel("Name");
				
				textField_1 = new JTextField();
				textField_1.setColumns(10);
				
				JLabel lblId = new JLabel("ID");
				
				JLabel lblSelectProgram = new JLabel("Select program");
				
				JButton button_2 = new JButton("Up");
				
				JButton btnDown = new JButton("Down");
				GroupLayout gl_programsPanel = new GroupLayout(programsPanel);
				gl_programsPanel.setHorizontalGroup(
					gl_programsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_programsPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_programsPanel.createSequentialGroup()
									.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblName)
										.addComponent(lblSelectProgram)
										.addComponent(jcPrograms, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))
									.addGap(46)
									.addComponent(list, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnDown, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
								.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblId))
							.addContainerGap(123, Short.MAX_VALUE))
				);
				gl_programsPanel.setVerticalGroup(
					gl_programsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_programsPanel.createSequentialGroup()
							.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_programsPanel.createSequentialGroup()
									.addGap(16)
									.addGroup(gl_programsPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(list_1, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
										.addComponent(list, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_programsPanel.createSequentialGroup()
									.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_programsPanel.createSequentialGroup()
											.addGap(20)
											.addComponent(button)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(button_1))
										.addGroup(gl_programsPanel.createSequentialGroup()
											.addContainerGap()
											.addComponent(lblSelectProgram)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(jcPrograms, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_programsPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_programsPanel.createSequentialGroup()
											.addGap(51)
											.addComponent(lblName)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addGap(15)
											.addComponent(lblId)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_programsPanel.createSequentialGroup()
											.addGap(42)
											.addComponent(button_2)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnDown)))
									.addGap(10)
									.addComponent(btnNewButton)))
							.addContainerGap(66, Short.MAX_VALUE))
				);
				programsPanel.setLayout(gl_programsPanel);

		JPanel controllersPanel = new JPanel();
		controllersPanel.setBackground(Color.GRAY);
		controllersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Controllers", null, controllersPanel, null);
		
		JComboBox jcControllers = new JComboBox();
		
		JLabel lblSelectController = new JLabel("Select controller");
		
		JLabel lblName_1 = new JLabel("Name");
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		
		JLabel lblSerialPort = new JLabel("Serial port");
		
		JComboBox comboBox_2 = new JComboBox();
		
		JLabel lblNewLabel = new JLabel("Program");
		
		JComboBox comboBox_3 = new JComboBox();
		GroupLayout gl_controllersPanel = new GroupLayout(controllersPanel);
		gl_controllersPanel.setHorizontalGroup(
			gl_controllersPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_controllersPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(jcControllers, Alignment.LEADING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblSelectController, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(63)
					.addGroup(gl_controllersPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_controllersPanel.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_controllersPanel.createSequentialGroup()
							.addGroup(gl_controllersPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(comboBox_3, Alignment.LEADING, 0, 114, Short.MAX_VALUE)
								.addComponent(textField_2, Alignment.LEADING)
								.addComponent(lblName_1, Alignment.LEADING)
								.addComponent(lblSerialPort, Alignment.LEADING)
								.addComponent(comboBox_2, Alignment.LEADING, 0, 114, Short.MAX_VALUE))
							.addContainerGap(397, GroupLayout.PREFERRED_SIZE))))
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
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(lblSerialPort)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(143, Short.MAX_VALUE))
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
	}

	private void initData() {
		mwdp.getPresets(new GetPresetsCallback());
		mwdp.getPrograms(new GetProgramsCallback());
		jcPresets.addActionListener(new SelectedProgramItemChanged());
	}

	public class GetPresetsCallback implements ICallback {

		@Override
		public void onSuccess(Object object) {
			HashMap<Integer, Preset> presets = ((HashMap<Integer, Preset>) object);
			for (Entry x : presets.entrySet()) {
				jcPresets.addItem((Preset) x.getValue());
			}
		}

		@Override
		public void onFailed(Exception ex) {
			// TODO Auto-generated method stub
		}

	}
	
	public class GetProgramsCallback implements ICallback {

		@Override
		public void onSuccess(Object object) {
			List<Program> programs = ((List<Program>) object);
			for (Program x : programs) {
				jcPrograms.addItem((Program) x);
			}
		}

		@Override
		public void onFailed(Exception ex) {
			// TODO Auto-generated method stub
		}

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
	
	public class SelectedProgramItemChanged implements ActionListener{

		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<Preset> box = (JComboBox<Preset>)e.getSource();	
			Preset selectedPreset = (Preset)box.getSelectedItem();
			jtfPresetID.setText(selectedPreset.getPresetID() + "");
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

package hu.gehorvath.lampsv.ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import hu.gehorvath.lampsv.core.Preset;
import hu.gehorvath.lampsv.ui.data.ICallback;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

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
		frame.setBounds(100, 100, 633, 387);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frame.getContentPane().add(tabbedPane, "name_972030312230151");

		JPanel presetsPanel = new JPanel();
		presetsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Presets", null, presetsPanel, null);

		JLabel lblSelectPresetTo = new JLabel("Select preset to modify");

		jcPresets = new JComboBox<Preset>();

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
		programsPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Programs", null, programsPanel, null);

		JPanel controllersPanel = new JPanel();
		controllersPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Controllers", null, controllersPanel, null);
		
		JPanel logPanel = new JPanel();
		logPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.addTab("Log", null, logPanel, null);
		logPanel.setLayout(new BorderLayout(2, 2));
		
		logTextShow = new JTextArea();
		logPanel.add(logTextShow, BorderLayout.CENTER);
	}

	private void initData() {
		mwdp.getPresets(new GetPresetsCallback());
		jcPresets.addActionListener(new SelectedItemChanged());
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
	
	public class SelectedItemChanged implements ActionListener{

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

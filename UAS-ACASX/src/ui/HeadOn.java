package ui;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.CONFIGURATION;
import java.awt.Color;


public class HeadOn extends JPanel 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup headOnAutoPilotAlgorithmGroup = new ButtonGroup();
	private final ButtonGroup headOnCollisionAvoidanceAlgorithmGroup = new ButtonGroup();
	private JTextField txtHeadontimes;

	public HeadOn() 
	{
		setLayout(null);
		
		
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setBounds(12, 12, 290, 31);
			
			JCheckBox chckbxHeadonencounter_1 = new JCheckBox("HeadOn");
			chckbxHeadonencounter_1.setSelected(CONFIGURATION.headOnSelected==1);
			chckbxHeadonencounter_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox chckbxHeadonencounter = (JCheckBox)e.getSource();
					CONFIGURATION.headOnSelected=chckbxHeadonencounter.isSelected()?1:0;
				}
			});
			splitPane.setLeftComponent(chckbxHeadonencounter_1);
			
			JSplitPane splitPane_1 = new JSplitPane();
			splitPane_1.setResizeWeight(0.6);
			splitPane.setRightComponent(splitPane_1);
			
			JLabel lblX = new JLabel("X");
			splitPane_1.setLeftComponent(lblX);
			
			JSplitPane splitPane_2 = new JSplitPane();
			splitPane_2.setResizeWeight(0.8);
			splitPane_1.setRightComponent(splitPane_2);
			
			txtHeadontimes = new JTextField();
			txtHeadontimes.setEnabled(false);
			txtHeadontimes.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField txtHeadontimes=(JTextField) e.getSource();
					CONFIGURATION.headOnTimes=Integer.parseInt(txtHeadontimes.getText());
				}
			});
			txtHeadontimes.setText("1");
			splitPane_2.setLeftComponent(txtHeadontimes);
			txtHeadontimes.setColumns(5);
			
			JButton btnConfig = new JButton("Config");			
			btnConfig.setEnabled(false);
			btnConfig.setHorizontalAlignment(SwingConstants.RIGHT);
			btnConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try 
					{
						    //System.out.println(e.getActionCommand());
							IntruderConfig dialog = new IntruderConfig();
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setName("HeadonEncounter--IntruderConfig");
							dialog.textFieldInit("HeadOnEncounter--IntruderConfig");
							dialog.setModal(true);
							//dialog.setBounds(x, y, width, height)
							dialog.setVisible(true);
					}
						
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
					
				}
			});
			splitPane_2.setRightComponent(btnConfig);
			this.add(splitPane);
			
		}
		
		{
			JPanel sensorSelectionPanel = new JPanel();
			sensorSelectionPanel.setBorder(new TitledBorder(null, "Sensor Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sensorSelectionPanel.setBounds(12, 55, 297, 101);
			add(sensorSelectionPanel);
			sensorSelectionPanel.setLayout(null);
			
			JCheckBox chckbxPerfectSensor = new JCheckBox("Perfect");
			chckbxPerfectSensor.setBounds(8, 20, 129, 23);
			sensorSelectionPanel.add(chckbxPerfectSensor);
			chckbxPerfectSensor.setSelected((CONFIGURATION.headOnSensorSelection&0B10000) == 0B10000);
			chckbxPerfectSensor.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnSensorSelection |= 0B10000;
					}
				}
			});
			
			
			JCheckBox chckbxAdsb = new JCheckBox("ADS-B");
			chckbxAdsb.setBounds(144, 20, 129, 23);
			sensorSelectionPanel.add(chckbxAdsb);
			chckbxAdsb.setSelected((CONFIGURATION.headOnSensorSelection&0B01000) == 0B01000);
			chckbxAdsb.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnSensorSelection |= 0B01000;
					}
				}
			});
			
			JCheckBox chckbxTcas = new JCheckBox("TCAS");
			chckbxTcas.setBounds(8, 47, 129, 23);
			sensorSelectionPanel.add(chckbxTcas);
			chckbxTcas.setSelected((CONFIGURATION.headOnSensorSelection&0B00100) == 0B00100);
			chckbxTcas.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnSensorSelection |= 0B00100;
					}
				}
			});
			
			JCheckBox chckbxRadar = new JCheckBox("Radar");
			chckbxRadar.setBounds(144, 47, 129, 23);
			sensorSelectionPanel.add(chckbxRadar);
			chckbxRadar.setSelected((CONFIGURATION.headOnSensorSelection&0B00010) == 0B00010);
			chckbxRadar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnSensorSelection |= 0B00010;
					}
				}
			});
			
			JCheckBox chckbxEoir = new JCheckBox("EO/IR");
			chckbxEoir.setBounds(8, 74, 129, 23);
			sensorSelectionPanel.add(chckbxEoir);
			chckbxEoir.setSelected((CONFIGURATION.headOnSensorSelection&0B00001) == 0B00001);
			chckbxEoir.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnSensorSelection |= 0B00001;
					}
				}
			});
		}
				
		
		{
			JPanel autoPilotAlgorithmSelectionPanel = new JPanel();
			
			autoPilotAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "Auto-Pilot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			autoPilotAlgorithmSelectionPanel.setBounds(12, 168, 297, 76);
			add(autoPilotAlgorithmSelectionPanel);
			autoPilotAlgorithmSelectionPanel.setLayout(null);
			
			JLabel lblStddev = new JLabel("StdDev");
			lblStddev.setBounds(12, 25, 70, 15);
			autoPilotAlgorithmSelectionPanel.add(lblStddev);
			
			final JLabel stdDevLabel = new JLabel(""+CONFIGURATION.headOnStdDevY);
			stdDevLabel.setBounds(239, 25, 47, 15);
			autoPilotAlgorithmSelectionPanel.add(stdDevLabel);
			
			JSlider headOnStdDevSlider = new JSlider();
			headOnStdDevSlider.setBounds(70, 25, 170, 16);
			autoPilotAlgorithmSelectionPanel.add(headOnStdDevSlider);
			headOnStdDevSlider.setSnapToTicks(true);
			headOnStdDevSlider.setPaintLabels(true);		
			headOnStdDevSlider.setMaximum(15);
			headOnStdDevSlider.setMinimum(0);
			headOnStdDevSlider.setValue((int)(CONFIGURATION.headOnStdDevY));
			headOnStdDevSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.headOnStdDevY = source.getValue();
					stdDevLabel.setText(""+CONFIGURATION.headOnStdDevY);
				}
			});
			
			
			JRadioButton rdbtnWhitenoise = new JRadioButton("WhiteNoise");
			rdbtnWhitenoise.setSelected(CONFIGURATION.headOnAutoPilotAlgorithmSelection=="WhiteNoise");
			rdbtnWhitenoise.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnAutoPilotAlgorithmSelection="WhiteNoise";
					}
				}
			});
			rdbtnWhitenoise.setBounds(8, 49, 110, 23);
			headOnAutoPilotAlgorithmGroup.add(rdbtnWhitenoise);
			autoPilotAlgorithmSelectionPanel.add(rdbtnWhitenoise);
			
			
			JRadioButton rdbtnSpecific = new JRadioButton("Specific");
			rdbtnSpecific.setSelected(CONFIGURATION.headOnAutoPilotAlgorithmSelection=="Specific");
			rdbtnSpecific.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnAutoPilotAlgorithmSelection="Specific";
					}
				}
			});			
			rdbtnSpecific.setBounds(173, 48, 110, 23);
			headOnAutoPilotAlgorithmGroup.add(rdbtnSpecific);
			autoPilotAlgorithmSelectionPanel.add(rdbtnSpecific);
			

			
		}		
	
		
		
		{
			JPanel collisionAvoidanceAlgorithmSelectionPanel = new JPanel();
			collisionAvoidanceAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "CAA Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			collisionAvoidanceAlgorithmSelectionPanel.setBounds(12, 258, 297, 57);
			this.add(collisionAvoidanceAlgorithmSelectionPanel);
			collisionAvoidanceAlgorithmSelectionPanel.setLayout(null);
			
			JRadioButton rdbtnNone = new JRadioButton("None");
			rdbtnNone.setBounds(209, 22, 62, 23);
			rdbtnNone.setSelected(CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection == "None");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnNone);
			headOnCollisionAvoidanceAlgorithmGroup.add(rdbtnNone);
			rdbtnNone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection = "None";
					}
				}
			});
			
			
			JRadioButton rdbtnACASXAvoidanceAlgorithm = new JRadioButton("ACASX");
			rdbtnACASXAvoidanceAlgorithm.setBounds(8, 22, 94, 23);
			rdbtnACASXAvoidanceAlgorithm.setSelected(CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection == "ACASXAvoidanceAlgorithm");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnACASXAvoidanceAlgorithm);
			headOnCollisionAvoidanceAlgorithmGroup.add(rdbtnACASXAvoidanceAlgorithm);
			rdbtnACASXAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm";
					}
				}
			});
			
			JRadioButton rdbtnRandomAvoidanceAlgorithm = new JRadioButton("Random");
			rdbtnRandomAvoidanceAlgorithm.setEnabled(false);
			rdbtnRandomAvoidanceAlgorithm.setBounds(108, 22, 94, 23);
			rdbtnRandomAvoidanceAlgorithm.setSelected(CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection == "RandomAvoidanceAlgorithm");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnRandomAvoidanceAlgorithm);
			headOnCollisionAvoidanceAlgorithmGroup.add(rdbtnRandomAvoidanceAlgorithm);
			rdbtnRandomAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection = "RandomAvoidanceAlgorithm";
					}
				}
			});
		}
		
		
		
		{
			JLabel lblOffset = new JLabel("Offset");
			lblOffset.setBounds(24, 327, 44, 15);
			this.add(lblOffset);
			
			JLabel lblVx = new JLabel("Vx");
			lblVx.setBounds(24, 354, 37, 15);
			add(lblVx);
			
			JLabel lblVy = new JLabel("Vy");
			lblVy.setBounds(24, 381, 37, 15);
			add(lblVy);
			
			final JLabel offsetLabel = new JLabel(""+CONFIGURATION.headOnOffset);
			offsetLabel.setBounds(269, 327, 44, 15);
			add(offsetLabel);
			
			final JLabel vxLabel = new JLabel(""+CONFIGURATION.headOnVx);
			vxLabel.setBounds(269, 354, 58, 15);
			add(vxLabel);
			
			final JLabel vyLabel = new JLabel(""+CONFIGURATION.headOnVy);
			vyLabel.setBounds(269, 381, 58, 15);
			add(vyLabel);
			
			JSlider selfOffsetSlider = new JSlider();
			selfOffsetSlider.setSnapToTicks(true);
			selfOffsetSlider.setPaintLabels(true);		
			selfOffsetSlider.setMaximum(500);
			selfOffsetSlider.setMinimum(-500);
			selfOffsetSlider.setValue((int)(CONFIGURATION.headOnOffset));
			selfOffsetSlider.setBounds(68, 327, 200, 16);
			this.add(selfOffsetSlider);
			selfOffsetSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.headOnOffset = source.getValue();
					offsetLabel.setText(""+CONFIGURATION.headOnOffset);
				}
			});
			
			JSlider selfVxSlider = new JSlider();
			selfVxSlider.setSnapToTicks(true);
			selfVxSlider.setPaintLabels(true);		
			selfVxSlider.setMaximum(304);
			selfVxSlider.setMinimum(169);
			selfVxSlider.setValue((int)(CONFIGURATION.headOnVx));
			selfVxSlider.setBounds(68, 354, 200, 16);
			this.add(selfVxSlider);
			selfVxSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.headOnVx = source.getValue();
					vxLabel.setText(""+CONFIGURATION.headOnVx);
				}
			});
		
			JSlider selfVySlider = new JSlider();
			selfVySlider.setSnapToTicks(true);
			selfVySlider.setPaintLabels(true);		
			selfVySlider.setMaximum(58);
			selfVySlider.setMinimum(-67);
			selfVySlider.setValue((int)(CONFIGURATION.headOnVy));
			selfVySlider.setBounds(68, 382, 200, 16);
			this.add(selfVySlider);
			selfVySlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.headOnVy = source.getValue();
					vyLabel.setText(""+CONFIGURATION.headOnVy);

				}
			});
		
		}
		
		{			
			JPanel performancePanel = new JPanel();
			performancePanel.setBackground(Color.LIGHT_GRAY);
			performancePanel.setBounds(12, 410, 297, 188);
			add(performancePanel);
			performancePanel.setLayout(null);
			JLabel lblMaxspeed = new JLabel("MaxSpeed");
			lblMaxspeed.setBounds(12, 14, 82, 15);
			performancePanel.add(lblMaxspeed);
			
			
			JTextField maxSpeedTextField_1 = new JTextField();
			maxSpeedTextField_1.setBounds(170, 14, 114, 19);
			performancePanel.add(maxSpeedTextField_1);
			maxSpeedTextField_1.setText(String.valueOf(CONFIGURATION.headOnMaxSpeed));
			maxSpeedTextField_1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxSpeedTextField = (JTextField) e.getSource();
					CONFIGURATION.headOnMaxSpeed = new Double(maxSpeedTextField.getText());
				}
			});
			maxSpeedTextField_1.setColumns(10);
			
			
			JLabel lblMinspeed = new JLabel("MinSpeed");
			lblMinspeed.setBounds(12, 43, 70, 19);
			performancePanel.add(lblMinspeed);
			
			
			JTextField minSpeedTextField_1 = new JTextField();
			minSpeedTextField_1.setBounds(170, 45, 114, 19);
			performancePanel.add(minSpeedTextField_1);
			minSpeedTextField_1.setText(String.valueOf(CONFIGURATION.headOnMinSpeed));
			minSpeedTextField_1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField minSpeedTextField = (JTextField) e.getSource();
					CONFIGURATION.headOnMinSpeed = new Double(minSpeedTextField.getText());
				}
			});
			minSpeedTextField_1.setColumns(10);
			
			JLabel lblPrefSpeed = new JLabel("PrefSpeed");
			lblPrefSpeed.setBounds(12, 74, 105, 15);
			performancePanel.add(lblPrefSpeed);
			
			JTextField prefSpeedTextField = new JTextField();
			prefSpeedTextField.setBounds(171, 72, 114, 19);
			performancePanel.add(prefSpeedTextField);
			prefSpeedTextField.setText(String.valueOf(CONFIGURATION.headOnPrefSpeed));
			prefSpeedTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField speedTextField = (JTextField) e.getSource();
					CONFIGURATION.headOnPrefSpeed = new Double(speedTextField.getText());
				}
			});
			prefSpeedTextField.setColumns(10);
			
			
			
			
			JLabel lblMaxClimb = new JLabel("MaxClimb");
			lblMaxClimb.setBounds(12, 101, 70, 19);
			performancePanel.add(lblMaxClimb);
			
			
			JTextField maxClimbTextField_1 = new JTextField();
			maxClimbTextField_1.setBounds(170, 99, 114, 19);
			performancePanel.add(maxClimbTextField_1);
			maxClimbTextField_1.setText(String.valueOf(CONFIGURATION.headOnMaxClimb));
			maxClimbTextField_1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxClimbTextField = (JTextField) e.getSource();
					CONFIGURATION.headOnMaxClimb = new Double(maxClimbTextField.getText());
				}
			});
			maxClimbTextField_1.setColumns(10);
			
			JLabel lblMaxDescent = new JLabel("MaxDescent");
			lblMaxDescent.setBounds(12, 131, 101, 19);
			performancePanel.add(lblMaxDescent);
			
			
			JTextField maxDescentTextField_1 = new JTextField();
			maxDescentTextField_1.setBounds(170, 133, 114, 19);
			performancePanel.add(maxDescentTextField_1);
			maxDescentTextField_1.setText(String.valueOf(CONFIGURATION.headOnMaxDescent));
			maxDescentTextField_1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxDescentTextField = (JTextField) e.getSource();
					CONFIGURATION.headOnMaxDescent = new Double(maxDescentTextField.getText());
				}
			});
			maxDescentTextField_1.setColumns(10);
			
			JLabel lblMaxturning = new JLabel("MaxTurning");
			lblMaxturning.setBounds(12, 162, 82, 15);
			performancePanel.add(lblMaxturning);
			
			{
				
				JTextField maxTurningTextField_1 = new JTextField();
				maxTurningTextField_1.setBounds(171, 164, 114, 19);
				performancePanel.add(maxTurningTextField_1);
				maxTurningTextField_1.setText(String.valueOf(Math.round(Math.toDegrees(CONFIGURATION.headOnMaxTurning)*100)/100.0));
				maxTurningTextField_1.addKeyListener(new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						JTextField maxTurningTextField = (JTextField) e.getSource();
						CONFIGURATION.headOnMaxTurning = Math.toRadians(new Double(maxTurningTextField.getText()));
					}
				});
				maxTurningTextField_1.setColumns(10);
			}
		}
		
						
	}

	public HeadOn(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public HeadOn(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public HeadOn(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

}

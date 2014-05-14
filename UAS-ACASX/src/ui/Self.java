package ui;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tools.CONFIGURATION;
import javax.swing.JCheckBox;
import java.awt.Color;

public class Self extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup selfAutoPilotAlgorithmGroup = new ButtonGroup();
	private final ButtonGroup selfCollisionAvoidanceAlgorithmGroup = new ButtonGroup();
	
	private JTextField maxSpeedTextField;
	private JTextField maxAccelerationTextField;
	private JTextField maxDecelerationTextField;
	private JTextField maxTurningTextField;
	private JTextField prefSpeedTextField;
	private JTextField minSpeedTextField;
	private JTextField maxClimbTextField;
	private JTextField maxDescentTextField;

	public Self() 
	{
		setLayout(null);
		
		{
			JPanel sensorSelectionPanel = new JPanel();
			sensorSelectionPanel.setBorder(new TitledBorder(null, "Sensor Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sensorSelectionPanel.setBounds(12, 12, 297, 100);
			add(sensorSelectionPanel);
			sensorSelectionPanel.setLayout(null);
			
			JCheckBox chckbxPerfectSensor = new JCheckBox("Perfect");
			chckbxPerfectSensor.setBounds(8, 20, 129, 23);
			sensorSelectionPanel.add(chckbxPerfectSensor);
			chckbxPerfectSensor.setSelected((CONFIGURATION.selfSensorSelection&0B10000) == 0B10000);
			chckbxPerfectSensor.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.selfSensorSelection |= 0B10000;
					}
				}
			});
			
			
			JCheckBox chckbxAdsb = new JCheckBox("ADS-B");
			chckbxAdsb.setBounds(144, 20, 129, 23);
			sensorSelectionPanel.add(chckbxAdsb);
			chckbxAdsb.setSelected((CONFIGURATION.selfSensorSelection&0B01000) == 0B01000);
			chckbxAdsb.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.selfSensorSelection |= 0B01000;
					}
				}
			});
			
			JCheckBox chckbxTcas = new JCheckBox("TCAS");
			chckbxTcas.setBounds(8, 47, 129, 23);
			sensorSelectionPanel.add(chckbxTcas);
			chckbxTcas.setSelected((CONFIGURATION.selfSensorSelection&0B00100) == 0B00100);
			chckbxTcas.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.selfSensorSelection |= 0B00100;
					}
				}
			});
			
			JCheckBox chckbxRadar = new JCheckBox("Radar");
			chckbxRadar.setBounds(144, 47, 129, 23);
			sensorSelectionPanel.add(chckbxRadar);
			chckbxRadar.setSelected((CONFIGURATION.selfSensorSelection&0B00010) == 0B00010);
			chckbxRadar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.selfSensorSelection |= 0B00010;
					}
				}
			});
			
			JCheckBox chckbxEoir = new JCheckBox("EO/IR");
			chckbxEoir.setBounds(8, 74, 129, 23);
			sensorSelectionPanel.add(chckbxEoir);
			chckbxEoir.setSelected((CONFIGURATION.selfSensorSelection&0B00001) == 0B00001);
			chckbxEoir.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.selfSensorSelection |= 0B00001;
					}
				}
			});
		}
		
		
		{
			JPanel autoPilotAlgorithmSelectionPanel = new JPanel();
			
			autoPilotAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "Auto-Pilot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			autoPilotAlgorithmSelectionPanel.setBounds(12, 124, 295, 79);
			add(autoPilotAlgorithmSelectionPanel);
			autoPilotAlgorithmSelectionPanel.setLayout(null);
			
			JLabel lblStddev = new JLabel("StdDev");
			lblStddev.setBounds(12, 25, 70, 15);
			autoPilotAlgorithmSelectionPanel.add(lblStddev);	
			
			final JLabel stdDevlabel = new JLabel(""+CONFIGURATION.selfStdDevY);
			stdDevlabel.setBounds(239, 25, 44, 15);
			autoPilotAlgorithmSelectionPanel.add(stdDevlabel);			
	       
	        
			JSlider selfStdDevSlider = new JSlider();
			selfStdDevSlider.setBounds(70, 25, 170, 16);
			autoPilotAlgorithmSelectionPanel.add(selfStdDevSlider);
			selfStdDevSlider.setSnapToTicks(true);
			selfStdDevSlider.setPaintLabels(true);		
			selfStdDevSlider.setMaximum(15);
			selfStdDevSlider.setMinimum(0);
			selfStdDevSlider.setValue((int)(CONFIGURATION.selfStdDevY));		
			selfStdDevSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.selfStdDevY = source.getValue();
					stdDevlabel.setText(""+CONFIGURATION.selfStdDevY);
				}
			});
			
			

			
			JRadioButton rdbtnWhitenoise = new JRadioButton("WhiteNoise");
			rdbtnWhitenoise.setSelected(CONFIGURATION.selfAutoPilotAlgorithmSelection=="WhiteNoise");
			rdbtnWhitenoise.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.selfAutoPilotAlgorithmSelection="WhiteNoise";
					}
				}
			});			
			rdbtnWhitenoise.setBounds(8, 48, 110, 23);
			selfAutoPilotAlgorithmGroup.add(rdbtnWhitenoise);
			autoPilotAlgorithmSelectionPanel.add(rdbtnWhitenoise);
			
			JRadioButton rdbtnSpecific = new JRadioButton("Specific");
			rdbtnSpecific.setSelected(CONFIGURATION.selfAutoPilotAlgorithmSelection=="Specific");
			rdbtnSpecific.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.selfAutoPilotAlgorithmSelection="Specific";
					}
				}
			});			
			rdbtnSpecific.setBounds(173, 48, 110, 23);
			selfAutoPilotAlgorithmGroup.add(rdbtnSpecific);
			autoPilotAlgorithmSelectionPanel.add(rdbtnSpecific);
			

			
		}
		
	
		
		{
			JPanel collisionAvoidanceAlgorithmSelectionPanel = new JPanel();
			collisionAvoidanceAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "CAA Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			collisionAvoidanceAlgorithmSelectionPanel.setBounds(12, 203, 297, 59);
			this.add(collisionAvoidanceAlgorithmSelectionPanel);
			collisionAvoidanceAlgorithmSelectionPanel.setLayout(null);
			
			JRadioButton rdbtnNone = new JRadioButton("None");
			rdbtnNone.setBounds(209, 22, 62, 23);
			rdbtnNone.setSelected(CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection == "None");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnNone);
			selfCollisionAvoidanceAlgorithmGroup.add(rdbtnNone);
			rdbtnNone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection = "None";
					}
				}
			});
			
			JRadioButton rdbtnACASXAvoidanceAlgorithm = new JRadioButton("ACASX");
			rdbtnACASXAvoidanceAlgorithm.setBounds(8, 22, 94, 23);
			rdbtnACASXAvoidanceAlgorithm.setSelected(CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection == "ACASXAvoidanceAlgorithm");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnACASXAvoidanceAlgorithm);
			selfCollisionAvoidanceAlgorithmGroup.add(rdbtnACASXAvoidanceAlgorithm);
			rdbtnACASXAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm";
					}
				}
			});
			
			JRadioButton rdbtnRandomAvoidanceAlgorithm = new JRadioButton("Random");
			rdbtnRandomAvoidanceAlgorithm.setEnabled(false);
			rdbtnRandomAvoidanceAlgorithm.setBounds(108, 22, 94, 23);
			rdbtnRandomAvoidanceAlgorithm.setSelected(CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection == "RandomAvoidanceAlgorithm");
			collisionAvoidanceAlgorithmSelectionPanel.add(rdbtnRandomAvoidanceAlgorithm);
			selfCollisionAvoidanceAlgorithmGroup.add(rdbtnRandomAvoidanceAlgorithm);
			rdbtnRandomAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection = "RandomAvoidanceAlgorithm";
					}
				}
			});
		}
		
		{
			JLabel lblVx = new JLabel("Vx");
			lblVx.setBounds(22, 274, 37, 15);
			add(lblVx);
			
			JLabel lblVy = new JLabel("Vy");
			lblVy.setBounds(22, 301, 37, 15);
			add(lblVy);
			
			
			final JLabel vxLabel = new JLabel(""+CONFIGURATION.selfVx);
			vxLabel.setBounds(249, 274, 60, 15);
			add(vxLabel);
			
			final JLabel vyLabel = new JLabel(""+CONFIGURATION.selfVy);
			vyLabel.setBounds(249, 301, 60, 15);
			add(vyLabel);
			
			JSlider selfVxSlider = new JSlider();
			selfVxSlider.setSnapToTicks(true);
			selfVxSlider.setPaintLabels(true);		
			selfVxSlider.setMaximum(304);
			selfVxSlider.setMinimum(169);
			selfVxSlider.setValue((int)(CONFIGURATION.selfVx));
			selfVxSlider.setBounds(43, 274, 200, 16);
			this.add(selfVxSlider);
			selfVxSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.selfVx = source.getValue();
					vxLabel.setText(""+CONFIGURATION.selfVx);
				}
			});
			
			JSlider selfVySlider = new JSlider();
			selfVySlider.setSnapToTicks(true);
			selfVySlider.setPaintLabels(true);		
			selfVySlider.setMaximum(58);
			selfVySlider.setMinimum(-67);
			selfVySlider.setValue((int)(CONFIGURATION.selfVy));
			selfVySlider.setBounds(43, 300, 200, 16);
			this.add(selfVySlider);
			selfVySlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider) e.getSource();
					CONFIGURATION.selfVy = source.getValue();
					vyLabel.setText(""+CONFIGURATION.selfVy);

				}
			});
			
		}
		
		{
			JPanel performancePanel = new JPanel();
			performancePanel.setBackground(Color.LIGHT_GRAY);
			performancePanel.setBounds(12, 345, 297, 246);
			add(performancePanel);
			performancePanel.setLayout(null);
			
			JLabel lblMaxspeed = new JLabel("MaxSpeed");
			lblMaxspeed.setBounds(12, 12, 73, 15);
			performancePanel.add(lblMaxspeed);
			
			
			JLabel lblMinspeed = new JLabel("MinSpeed");
			lblMinspeed.setBounds(12, 37, 70, 19);
			performancePanel.add(lblMinspeed);
			
			JLabel lblPrefSpeed = new JLabel("PrefSpeed");
			lblPrefSpeed.setBounds(12, 64, 101, 15);
			performancePanel.add(lblPrefSpeed);
			
			
			JLabel lblMaxClimb = new JLabel("MaxClimb");
			lblMaxClimb.setBounds(12, 91, 70, 19);
			performancePanel.add(lblMaxClimb);
			
			JLabel lblMaxDescent = new JLabel("MaxDescent");
			lblMaxDescent.setBounds(12, 125, 101, 19);
			performancePanel.add(lblMaxDescent);
			
			JLabel lblMaxturning = new JLabel("MaxTurning");
			lblMaxturning.setBounds(12, 156, 82, 15);
			performancePanel.add(lblMaxturning);
			
			
			JLabel lblMaxacceleration = new JLabel("MaxAcceleration");
			lblMaxacceleration.setBounds(12, 183, 116, 15);
			performancePanel.add(lblMaxacceleration);
			
			JLabel lblMaxdecceleration = new JLabel("MaxDeceleration");
			lblMaxdecceleration.setBounds(12, 210, 119, 15);
			performancePanel.add(lblMaxdecceleration);
			
			
			maxSpeedTextField = new JTextField();
			maxSpeedTextField.setBounds(170, 12, 114, 19);
			performancePanel.add(maxSpeedTextField);
			maxSpeedTextField.setText(String.valueOf(CONFIGURATION.selfMaxSpeed));
			maxSpeedTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxSpeedTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxSpeed = new Double(maxSpeedTextField.getText());
				}
			});
			maxSpeedTextField.setColumns(10);
			
			
			minSpeedTextField = new JTextField();
			minSpeedTextField.setBounds(170, 43, 114, 19);
			performancePanel.add(minSpeedTextField);
			minSpeedTextField.setText(String.valueOf(CONFIGURATION.selfMinSpeed));
			minSpeedTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField minSpeedTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMinSpeed = new Double(minSpeedTextField.getText());
				}
			});
			minSpeedTextField.setColumns(10);
			
			prefSpeedTextField = new JTextField();
			prefSpeedTextField.setBounds(171, 70, 114, 19);
			performancePanel.add(prefSpeedTextField);
			prefSpeedTextField.setText(String.valueOf(CONFIGURATION.selfPrefSpeed));
			prefSpeedTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField speedTextField = (JTextField) e.getSource();
					CONFIGURATION.selfPrefSpeed = new Double(speedTextField.getText());
				}
			});
			prefSpeedTextField.setColumns(10);
			
			
			maxClimbTextField = new JTextField();
			maxClimbTextField.setBounds(170, 97, 114, 19);
			performancePanel.add(maxClimbTextField);
			maxClimbTextField.setText(String.valueOf(CONFIGURATION.selfMaxClimb));
			maxClimbTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxClimbTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxClimb = new Double(maxClimbTextField.getText());
				}
			});
			maxClimbTextField.setColumns(10);
			
			
			maxDescentTextField = new JTextField();
			maxDescentTextField.setBounds(170, 131, 114, 19);
			performancePanel.add(maxDescentTextField);
			maxDescentTextField.setText(String.valueOf(CONFIGURATION.selfMaxDescent));
			maxDescentTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxDescentTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxDescent = new Double(maxDescentTextField.getText());
				}
			});
			maxDescentTextField.setColumns(10);
			
			maxTurningTextField = new JTextField();
			maxTurningTextField.setBounds(171, 162, 114, 19);
			performancePanel.add(maxTurningTextField);
			maxTurningTextField.setText(String.valueOf(Math.round(Math.toDegrees(CONFIGURATION.selfMaxTurning)*100)/100.0));
			maxTurningTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxTurningTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxTurning = Math.toRadians(new Double(maxTurningTextField.getText()));
				}
			});
			maxTurningTextField.setColumns(10);
			
			maxAccelerationTextField = new JTextField();
			maxAccelerationTextField.setBounds(171, 189, 114, 19);
			performancePanel.add(maxAccelerationTextField);
			maxAccelerationTextField.setText(String.valueOf(CONFIGURATION.selfMaxAcceleration));
			maxAccelerationTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxAccelerationTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxAcceleration = new Double(maxAccelerationTextField.getText());
				}
			});
			maxAccelerationTextField.setColumns(10);
			
			maxDecelerationTextField = new JTextField();
			maxDecelerationTextField.setBounds(171, 216, 114, 19);
			performancePanel.add(maxDecelerationTextField);
			maxDecelerationTextField.setText(String.valueOf(CONFIGURATION.selfMaxDeceleration));
			maxDecelerationTextField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxDecelerationTextField = (JTextField) e.getSource();
					CONFIGURATION.selfMaxDeceleration = new Double(maxDecelerationTextField.getText());
				}
			});
			maxDecelerationTextField.setColumns(10);

		}

		

					
	}

	public Self(LayoutManager layout) {
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public Self(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public Self(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
}

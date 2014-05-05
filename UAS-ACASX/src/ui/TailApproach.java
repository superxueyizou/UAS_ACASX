package ui;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
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
import tools.CONFIGURATION;


public class TailApproach extends JPanel 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ButtonGroup tailApproachAutoPilotAlgorithmGroup = new ButtonGroup();
	private final ButtonGroup tailApproachCollisionAvoidanceAlgorithmGroup = new ButtonGroup();
	private JTextField txtTailApproachTimes;
	private JTextField tailApproachOffsetTextField;

	public TailApproach()
	{   
		setLayout(null);
		
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setBounds(12, 12, 290, 31);
			this.add(splitPane);
			
			JCheckBox chckbxTailApproachEncounter_1 = new JCheckBox("TailApproach");
			chckbxTailApproachEncounter_1.setSelected(CONFIGURATION.tailApproachSelected==1);
			chckbxTailApproachEncounter_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox chckbxTailApproachEncounter = (JCheckBox)e.getSource();
					CONFIGURATION.tailApproachSelected=chckbxTailApproachEncounter.isSelected()?1:0;
				}
			});
			splitPane.setLeftComponent(chckbxTailApproachEncounter_1);
			
			JSplitPane splitPane_1 = new JSplitPane();
			splitPane_1.setResizeWeight(0.6);
			splitPane.setRightComponent(splitPane_1);
			
			JLabel lblX = new JLabel("X");
			splitPane_1.setLeftComponent(lblX);
			
			JSplitPane splitPane_2 = new JSplitPane();
			splitPane_2.setResizeWeight(0.8);
			splitPane_1.setRightComponent(splitPane_2);
			
			txtTailApproachTimes = new JTextField();
			txtTailApproachTimes.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField txtTailApproachTimes=(JTextField) e.getSource();
					CONFIGURATION.tailApproachTimes=Integer.parseInt(txtTailApproachTimes.getText());
				}
			});
			txtTailApproachTimes.setText("1");
			splitPane_2.setLeftComponent(txtTailApproachTimes);
			txtTailApproachTimes.setColumns(5);
			
			JButton btnConfig = new JButton("Config");
			btnConfig.setEnabled(false);
			btnConfig.setHorizontalAlignment(SwingConstants.RIGHT);
			btnConfig.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						IntruderConfig dialog = new IntruderConfig();
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setName("TailApproachEncounter--IntruderConfig");
						dialog.textFieldInit("TailApproachEncounter--IntruderConfig");
						dialog.setModal(true);
						dialog.setVisible(true);
						

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
				}
			});
			splitPane_2.setRightComponent(btnConfig);
			this.add(splitPane);
		}
		JRadioButton rdbtnIsrightside_2 = new JRadioButton("IsRightSide");
		rdbtnIsrightside_2.setBounds(12, 52, 105, 23);
		rdbtnIsrightside_2.setSelected(CONFIGURATION.tailApproachIsRightSide==1);
		this.add(rdbtnIsrightside_2);
		rdbtnIsrightside_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(((JRadioButton)e.getSource()).isSelected())
				{
					CONFIGURATION.tailApproachIsRightSide = 1;
				}
				else
				{
					CONFIGURATION.tailApproachIsRightSide = 0;
				}
			}
		});
		
		JLabel lblOffset_1 = new JLabel("Offset");
		lblOffset_1.setBounds(176, 56, 44, 15);
		this.add(lblOffset_1);
		
		{
		
		tailApproachOffsetTextField = new JTextField();
		tailApproachOffsetTextField.setBounds(238, 54, 64, 19);
		tailApproachOffsetTextField.setText(""+CONFIGURATION.tailApproachOffset);
		this.add(tailApproachOffsetTextField);
		tailApproachOffsetTextField.setColumns(10);
		tailApproachOffsetTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField offsetTextField=(JTextField) e.getSource();
				CONFIGURATION.tailApproachOffset=Double.parseDouble(offsetTextField.getText());
			}
		});
		}
		
		{
			JPanel sensorSelectionPanel = new JPanel();
			sensorSelectionPanel.setBorder(new TitledBorder(null, "Sensor Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			sensorSelectionPanel.setBounds(12, 95, 297, 106);
			add(sensorSelectionPanel);
			sensorSelectionPanel.setLayout(null);
			
			JCheckBox chckbxPerfectSensor = new JCheckBox("Perfect");
			chckbxPerfectSensor.setBounds(8, 20, 129, 23);
			sensorSelectionPanel.add(chckbxPerfectSensor);
			chckbxPerfectSensor.setSelected((CONFIGURATION.tailApproachSensorSelection&0B10000) == 0B10000);
			chckbxPerfectSensor.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachSensorSelection |= 0B10000;
					}
				}
			});
			
			
			JCheckBox chckbxAdsb = new JCheckBox("ADS-B");
			chckbxAdsb.setBounds(144, 20, 129, 23);
			sensorSelectionPanel.add(chckbxAdsb);
			chckbxAdsb.setSelected((CONFIGURATION.tailApproachSensorSelection&0B01000) == 0B01000);
			chckbxAdsb.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachSensorSelection |= 0B01000;
					}
				}
			});
			
			JCheckBox chckbxTcas = new JCheckBox("TCAS");
			chckbxTcas.setBounds(8, 47, 129, 23);
			sensorSelectionPanel.add(chckbxTcas);
			chckbxTcas.setSelected((CONFIGURATION.tailApproachSensorSelection&0B00100) == 0B00100);
			chckbxTcas.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachSensorSelection |= 0B00100;
					}
				}
			});
			
			JCheckBox chckbxRadar = new JCheckBox("Radar");
			chckbxRadar.setBounds(144, 47, 129, 23);
			sensorSelectionPanel.add(chckbxRadar);
			chckbxRadar.setSelected((CONFIGURATION.tailApproachSensorSelection&0B00010) == 0B00010);
			chckbxRadar.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachSensorSelection |= 0B00010;
					}
				}
			});
			
			JCheckBox chckbxEoir = new JCheckBox("EO/IR");
			chckbxEoir.setBounds(8, 74, 129, 23);
			sensorSelectionPanel.add(chckbxEoir);
			chckbxEoir.setSelected((CONFIGURATION.tailApproachSensorSelection&0B00001) == 0B00001);
			chckbxEoir.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachSensorSelection |= 0B00001;
					}
				}
			});
		}
		
		{
			JPanel autoPilotAlgorithmSelectionPanel = new JPanel();
			
			autoPilotAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "Auto-Pilot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			autoPilotAlgorithmSelectionPanel.setBounds(14, 214, 295, 67);
			add(autoPilotAlgorithmSelectionPanel);
			autoPilotAlgorithmSelectionPanel.setLayout(null);
			
			JRadioButton rdbtnTotarget = new JRadioButton("ToTarget");
			rdbtnTotarget.setSelected(CONFIGURATION.tailApproachAutoPilotAlgorithmSelection=="ToTarget");
			rdbtnTotarget.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachAutoPilotAlgorithmSelection="ToTarget";
					}
				}
			});
			rdbtnTotarget.setBounds(8, 22, 94, 23);
			tailApproachAutoPilotAlgorithmGroup.add(rdbtnTotarget);
			autoPilotAlgorithmSelectionPanel.add(rdbtnTotarget);
			
			JRadioButton rdbtnDubins = new JRadioButton("Dubins");
			rdbtnDubins.setSelected(CONFIGURATION.tailApproachAutoPilotAlgorithmSelection=="Dubins");
			rdbtnDubins.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachAutoPilotAlgorithmSelection="Dubins";
					}
				}
			});
			rdbtnDubins.setBounds(107, 22, 74, 23);
			tailApproachAutoPilotAlgorithmGroup.add(rdbtnDubins);
			autoPilotAlgorithmSelectionPanel.add(rdbtnDubins);
			
			JRadioButton rdbtnWhitenoise = new JRadioButton("WhiteNoise");
			rdbtnWhitenoise.setSelected(CONFIGURATION.tailApproachAutoPilotAlgorithmSelection=="WhiteNoise");
			rdbtnWhitenoise.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachAutoPilotAlgorithmSelection="WhiteNoise";
					}
				}
			});
			rdbtnWhitenoise.setBounds(177, 22, 110, 23);
			tailApproachAutoPilotAlgorithmGroup.add(rdbtnWhitenoise);
			autoPilotAlgorithmSelectionPanel.add(rdbtnWhitenoise);
		}	
	
		
		
		{
			JPanel AvoidanceAlgorithmSelectionPanel = new JPanel();
			AvoidanceAlgorithmSelectionPanel.setBorder(new TitledBorder(null, "CAA Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			AvoidanceAlgorithmSelectionPanel.setBounds(12, 282, 297, 55);
			this.add(AvoidanceAlgorithmSelectionPanel);
			AvoidanceAlgorithmSelectionPanel.setLayout(null);
			
			JRadioButton rdbtnNone = new JRadioButton("None");
			rdbtnNone.setBounds(209, 22, 62, 23);
			rdbtnNone.setSelected(CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection == "None");
			AvoidanceAlgorithmSelectionPanel.add(rdbtnNone);
			tailApproachCollisionAvoidanceAlgorithmGroup.add(rdbtnNone);
			rdbtnNone.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection = "None";
					}
				}
			});
			
			JRadioButton rdbtnACASXAvoidanceAlgorithm = new JRadioButton("ACASX");
			rdbtnACASXAvoidanceAlgorithm.setBounds(8, 22, 94, 23);
			rdbtnACASXAvoidanceAlgorithm.setSelected(CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection == "ACASXAvoidanceAlgorithm");
			AvoidanceAlgorithmSelectionPanel.add(rdbtnACASXAvoidanceAlgorithm);
			tailApproachCollisionAvoidanceAlgorithmGroup.add(rdbtnACASXAvoidanceAlgorithm);
			rdbtnACASXAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm";
					}
				}
			});
			
			JRadioButton rdbtnRandomAvoidanceAlgorithm = new JRadioButton("Random");
			rdbtnRandomAvoidanceAlgorithm.setEnabled(false);
			rdbtnRandomAvoidanceAlgorithm.setBounds(108, 22, 94, 23);
			rdbtnRandomAvoidanceAlgorithm.setSelected(CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection == "RandomAvoidanceAlgorithm");
			AvoidanceAlgorithmSelectionPanel.add(rdbtnRandomAvoidanceAlgorithm);
			tailApproachCollisionAvoidanceAlgorithmGroup.add(rdbtnRandomAvoidanceAlgorithm);
			rdbtnRandomAvoidanceAlgorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(((JRadioButton)e.getSource()).isSelected())
					{
						CONFIGURATION.tailApproachCollisionAvoidanceAlgorithmSelection = "RandomAvoidanceAlgorithm";
					}
				}
			});
			
		}			
		
		
		JPanel performancePanel = new JPanel();
		performancePanel.setBounds(12, 349, 297, 209);
		add(performancePanel);
		performancePanel.setLayout(null);
		JLabel lblMaxspeed = new JLabel("MaxSpeed");
		lblMaxspeed.setBounds(12, 14, 82, 15);
		performancePanel.add(lblMaxspeed);
		
		
		JTextField maxSpeedTextField_1 = new JTextField();
		maxSpeedTextField_1.setBounds(170, 12, 114, 19);
		performancePanel.add(maxSpeedTextField_1);
		maxSpeedTextField_1.setText(String.valueOf(CONFIGURATION.tailApproachMaxSpeed));
		maxSpeedTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField maxSpeedTextField = (JTextField) e.getSource();
				CONFIGURATION.tailApproachMaxSpeed = new Double(maxSpeedTextField.getText());
			}
		});
		maxSpeedTextField_1.setColumns(10);
		
		
		JLabel lblMinspeed = new JLabel("MinSpeed");
		lblMinspeed.setBounds(12, 43, 70, 19);
		performancePanel.add(lblMinspeed);
		
		
		JTextField minSpeedTextField_1 = new JTextField();
		minSpeedTextField_1.setBounds(170, 43, 114, 19);
		performancePanel.add(minSpeedTextField_1);
		minSpeedTextField_1.setText(String.valueOf(CONFIGURATION.tailApproachMinSpeed));
		minSpeedTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField minSpeedTextField = (JTextField) e.getSource();
				CONFIGURATION.tailApproachMinSpeed = new Double(minSpeedTextField.getText());
			}
		});
		minSpeedTextField_1.setColumns(10);
		
		JLabel lblPrefSpeed = new JLabel("PrefSpeed");
		lblPrefSpeed.setBounds(12, 70, 103, 15);
		performancePanel.add(lblPrefSpeed);
		
		final JTextField prefSpeedTextField_1 = new JTextField();
		prefSpeedTextField_1.setBounds(171, 70, 114, 19);
		performancePanel.add(prefSpeedTextField_1);
		prefSpeedTextField_1.setText(String.valueOf(CONFIGURATION.tailApproachPrefSpeed));
		prefSpeedTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField prefSpeedTextField = (JTextField) e.getSource();
				CONFIGURATION.tailApproachPrefSpeed = new Double(prefSpeedTextField.getText());
			}
		});
		prefSpeedTextField_1.setColumns(10);
		
//			JSpinner spinner_1 = new JSpinner();
//			spinner_1.addChangeListener(new ChangeListener() 
//			{
//				public void stateChanged(ChangeEvent e)
//				{
//					JSpinner spinner = (JSpinner)e.getSource();
//					CONFIGURATION.tailApproachPrefSpeedCoef = Double.parseDouble(spinner.getValue().toString());	
//					double prefSpeed;
//					if(CONFIGURATION.tailApproachPrefSpeedCoef>0)
//					{
//						prefSpeed=CONFIGURATION.selfPrefSpeed + CONFIGURATION.tailApproachPrefSpeedCoef *(CONFIGURATION.tailApproachMaxSpeed-CONFIGURATION.selfPrefSpeed);
//					}
//					else
//					{
//						prefSpeed=CONFIGURATION.selfPrefSpeed + CONFIGURATION.tailApproachPrefSpeedCoef *(CONFIGURATION.selfPrefSpeed-CONFIGURATION.tailApproachMinSpeed);
//					}
//					
//					prefSpeedTextField.setText(String.valueOf(Math.round(prefSpeed*100)/100.0));
//				}
//			});
//			spinner_1.setModel(new SpinnerNumberModel(1, -1, 1, 0.05));
//			spinner_1.setBounds(120, 325, 57, 20);
//			add(spinner_1);
		
		
		
		JLabel lblMaxClimb = new JLabel("MaxClimb");
		lblMaxClimb.setBounds(12, 97, 70, 19);
		performancePanel.add(lblMaxClimb);
		
		
		JTextField maxClimbTextField_1 = new JTextField();
		maxClimbTextField_1.setBounds(170, 97, 114, 19);
		performancePanel.add(maxClimbTextField_1);
		maxClimbTextField_1.setText(String.valueOf(CONFIGURATION.tailApproachMaxClimb));
		maxClimbTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField maxClimbTextField = (JTextField) e.getSource();
				CONFIGURATION.tailApproachMaxClimb = new Double(maxClimbTextField.getText());
			}
		});
		maxClimbTextField_1.setColumns(10);
		
		JLabel lblMaxDescent = new JLabel("MaxDescent");
		lblMaxDescent.setBounds(12, 131, 101, 19);
		performancePanel.add(lblMaxDescent);
		
		
		JTextField maxDescentTextField_1 = new JTextField();
		maxDescentTextField_1.setBounds(170, 131, 114, 19);
		performancePanel.add(maxDescentTextField_1);
		maxDescentTextField_1.setText(String.valueOf(CONFIGURATION.tailApproachMaxDescent));
		maxDescentTextField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField maxDescentTextField = (JTextField) e.getSource();
				CONFIGURATION.tailApproachMaxDescent = new Double(maxDescentTextField.getText());
			}
		});
		maxDescentTextField_1.setColumns(10);
		
		JLabel lblMaxturning = new JLabel("MaxTurning");
		lblMaxturning.setBounds(12, 162, 82, 15);
		performancePanel.add(lblMaxturning);
		
		
		{
			
			JTextField maxTurningTextField_1 = new JTextField();
			maxTurningTextField_1.setBounds(171, 162, 114, 19);
			performancePanel.add(maxTurningTextField_1);
			maxTurningTextField_1.setText(String.valueOf(Math.round(Math.toDegrees(CONFIGURATION.tailApproachMaxTurning)*100)/100.0));
			maxTurningTextField_1.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					JTextField maxTurningTextField = (JTextField) e.getSource();
					CONFIGURATION.tailApproachMaxTurning = Math.toRadians(new Double(maxTurningTextField.getText()));
				}
			});
			maxTurningTextField_1.setColumns(10);
		}


    }

	public TailApproach(LayoutManager layout) 
	{
		super(layout);
		// TODO Auto-generated constructor stub
	}

	public TailApproach(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}

	public TailApproach(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
		// TODO Auto-generated constructor stub
	}
}

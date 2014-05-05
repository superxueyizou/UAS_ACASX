package modeling;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import modeling.env.Destination;
import modeling.env.VelocityObstaclePoint;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.HexagonalPortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import tools.CONFIGURATION;

/**
 * A class for running a simulation with a UI, run to see a simulation with a UI
 * showing it running.
 * 
 * @author Robert Lee
 */
public class SAAModelWithUI extends GUIState
{	
	protected SAAModelBuilder sBuilder; 
	
	public Display2D display;
	public JFrame displayFrame;	
	private double displayX = CONFIGURATION.worldX;
	private double displayY = CONFIGURATION.worldY;
	
	
	ContinuousPortrayal2D environmentPortrayal = new ContinuousPortrayal2D();
	NetworkPortrayal2D voPortrayal = new NetworkPortrayal2D();
  
	
   
    public SAAModelWithUI() 
    {   
        super(new SAAModel(785945568, CONFIGURATION.worldX, CONFIGURATION.worldY, true)); 	    	
    	System.out.println("COModelWithUI() is being called!"+ "it's state(model)is: "+ state.toString());
    	sBuilder = new SAAModelBuilder((SAAModel) state);
    }
  
    
    public void start()
	{
		System.out.println("COModelWithUI.start is called  "+ state);
		((SAAModel)state).reset();
		sBuilder.generateSimulation();
		
		super.start();
		setupPortrayals();	
		
	}

	/**
	 * I do not know if this method is required by MASON at all, and the simulation
	 * with UI appears to run correctly even when it is removed, however all of 
	 * the example simulations that MASON comes with include a load method in the
	 * with UI class so I have done as well even though I have not found a reason
	 * as to if it is important to have one.
	 */
	public void load(SimState state)
	{
		((SAAModel)state).reset();
		sBuilder.generateSimulation();
		super.load(state);
		setupPortrayals();
	}

	
	
	/**
	 * A method which sets up the portrayals of the different layers in the UI,
	 * this is where details of the simulation are coloured and set to different
	 * parts of the UI
	 */
	public void setupPortrayals()
	{		
		SAAModel simulation = (SAAModel) state;
		
		// tell the portrayals what to portray and how to portray them
		environmentPortrayal.setField( simulation.environment );		

		CircledPortrayal2D cp=new CircledPortrayal2D( 
											(SimplePortrayal2D)new OvalPortrayal2D(30)
											{
												/**
												 * 
												 */
												private static final long serialVersionUID = 1L;

												public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
												{
													paint = new Color(0, 156, 0);		
												    super.draw(object, graphics, info);
												}
											},CONFIGURATION.selfSafetyRadius/10,1.0,new Color(255, 20, 0),false)	
              {
				private static final long serialVersionUID = 1L;

					public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
					{
						paint = ((UAS)object).isActive? new Color(255, 0, 0):new Color(128, 128, 128);			
					    super.draw(object, graphics, info);
					}

              };
              
        OrientedPortrayal2D op = new OrientedPortrayal2D(cp,0, 290, new Color(0,0,0), OrientedPortrayal2D.SHAPE_KITE);
		
		environmentPortrayal.setPortrayalForClass(UAS.class, op);
		
		environmentPortrayal.setPortrayalForClass(Destination.class, new LabelledPortrayal2D( new OvalPortrayal2D(150)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				paint = new Color(0x0E, 0xEC, 0xF0);			
			    super.draw(object, graphics, info);
			}
		}, "T", new Color(0, 0, 0), false) 				
		
		);
		
		
		environmentPortrayal.setPortrayalForClass(Waypoint.class, new OvalPortrayal2D(100)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				int actionCode = ((Waypoint) object).getAction();
				if(actionCode==1)
				{
					//red for avoid
					paint = new Color(255, 0, 0);	
					scale =200;
					filled =true;
				}
				else if(actionCode==2)
				{
					//yellow for maintain
					paint = new Color(255, 155, 0);	
					scale =100;
					filled =true;
				}
				else if(actionCode==3)
				{
					//green for restore
					paint = new Color(0, 255, 0);	
					scale =100;
					filled =true;
				}

				else if(actionCode==11||actionCode==12)
				{
					//red for avoid
					paint = new Color(255, 0, 0);	
					scale =100;
					filled =false;
				}
				else if(actionCode==13||actionCode==14)
				{
					//red for avoid
					paint = new Color(0, 255, 0);	
					scale =100;
					filled =false;
				}
				else if(actionCode==15||actionCode==16)
				{
					//red for avoid
					paint = new Color(0, 0, 255);	
					scale =100;
					filled =false;
				}				
				else
				{
					//black for normal
					paint = new Color(0, 0, 0);
					scale = 100;
					filled =true;
				}
				
				/********************for ACASX (+30)******************************************/
				if(actionCode==31||actionCode==33 ||actionCode==35)
				{//climb					
					scale =200;
					filled =true;
					switch(actionCode)
					{
					case 31:
						paint = new Color(0, 255, 0);//CL25
						break;
					case 33:
						paint = new Color(0, 255, 255);//SCL25
						break;
					case 35:
						paint = new Color(255, 0, 0);//SCL42						
					}
				}
				else if(actionCode==32||actionCode==34 ||actionCode==36)
				{//descend					
					scale =200;
					filled =false;
					switch(actionCode)
					{
					case 32:
						paint = new Color(0, 255, 0);//DES25
						break;
					case 34:
						paint = new Color(0, 255, 255);//SDES25
						break;
					case 36:
						paint = new Color(255, 0, 0);//SDES42						
					}
				}
				else if(actionCode==30)
				{//COC
					//black for normal
					paint = new Color(0, 0, 0);
					scale = 10;
					filled =true;
				}
				/*********************************************************************/			
			    super.draw(object, graphics, info);
			}
		});
		
		environmentPortrayal.setPortrayalForClass(VelocityObstaclePoint.class, new HexagonalPortrayal2D(50)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				paint = new Color(255, 255, 255);			
			    super.draw(object, graphics, info);
			}
		});
		
		
		voPortrayal.setField( new SpatialNetwork2D(simulation.environment, simulation.voField));
		voPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());
		
		// reschedule the displayer
		display.reset();
		// redraw the display
		display.repaint();
	}
	
	

    public void init(Controller c)
    {
        super.init(c);
        // make the displayer
        display = new Display2D(displayX,displayY,this);    
        display.setScale(0.25*0.29/2.5);
        // turn off clipping
        display.setClipping(true);
//      display.setBackdrop(new Color(0,0,0));

        displayFrame = display.createFrame();
        displayFrame.setTitle("SAA Simulation");
        c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
        displayFrame.setVisible(true);
        displayFrame.setBounds(0, 0, 1518, 1164); //(new Dimension(1514,1140))
        
		//adding the different layers to the display
        display.attach(environmentPortrayal, "Environment" );
        display.attach(voPortrayal, "VelocityObstacles",false);
         
        System.out.println("COModelWithUI.init is called!");
    }
    
  
    public void quit()
    {
        super.quit();

        if (displayFrame!=null) displayFrame.dispose();
        displayFrame = null;
        display = null;
    }
    
    
    
    public static String getName() { return "UAS-SAA-Sim"; }


	public void setDisplayBound(double x, double y)
	{
		this.displayX = x;
		this.displayY = y;
	}


	public Object getSimulationInspectedObject(){return state;}
    
    public Inspector getInspector()
    {
    	Inspector i = super.getInspector();
    	i.setVolatile(true);
    	return i;
    }   

}

package modeling;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import saa.collsionavoidance.LookupTable;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
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
   
    public SAAModelWithUI() 
    {   
        super(new SAAModel(785945568, CONFIGURATION.worldX, CONFIGURATION.worldY, true)); 	    	
    	sBuilder = new SAAModelBuilder((SAAModel) state);
    	LookupTable.getInstance();
    }
  
    
    public void init(Controller c)
    {
        super.init(c);
        // make the displayer
        display = new Display2D(displayX,displayY,this);    
        display.setScale(0.1044);
//        display.setYScale(2);
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
    }


	public void start()
	{
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

		SimplePortrayal2D sp =(SimplePortrayal2D)new OvalPortrayal2D()
		{
			private static final long serialVersionUID = 1L;

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				paint = new Color(0, 0, 0);		
			    super.draw(object, graphics, info);
			}
		};
		
		OrientedPortrayal2D op = new OrientedPortrayal2D(sp,0, 150, new Color(0,0,0), OrientedPortrayal2D.SHAPE_KITE)
        {
				private static final long serialVersionUID = 1L;
				public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
				{
					paint = ((UAS)object).isActive? new Color(0, 0, 0):new Color(255, 0, 0);			
				    super.draw(object, graphics, info);
				}

        };

		environmentPortrayal.setPortrayalForClass(UAS.class, op);
		
		environmentPortrayal.setPortrayalForClass(Waypoint.class, new OvalPortrayal2D()
		{
			private static final long serialVersionUID = 1L;

			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				int actionCode = ((Waypoint) object).getAction();
				if(actionCode==31||actionCode==33 ||actionCode==35)
				{//climb					
					
					filled =true;
					switch(actionCode)
					{
					case 31:
						paint = new Color(255,0,0);//CL25
						scale =50;
						break;
					case 33:
						paint = new Color(255, 0, 0);//SCL25
						scale =80;
						break;
					case 35:
						paint = new Color(255, 0, 0);//SCL42	
						scale =120;
					}
				}
				else if(actionCode==32||actionCode==34 ||actionCode==36)
				{//descend	
					double k=1;
					filled =true;
					switch(actionCode)
					{
					case 32:
						paint = new Color(0,255,0);//DES25
						scale =50*k;
						break;
					case 34:
						paint = new Color(0, 255, 0);//SDES25
						scale =80*k;
						break;
					case 36:
						paint = new Color(0, 255, 0);//SDES42	
						scale =120*k;
					}
				}
				else if(actionCode==30)
				{//COC
					paint = new Color(0, 0, 0);
					scale = 50;
					filled =false;
				}
				else
				{
					//black for normal
					paint = new Color(0, 0, 0);
					scale = 50;
					filled =true;
				}
				/*********************************************************************/			
			    super.draw(object, graphics, info);
			}
		});		
		

		// reschedule the displayer
		display.reset();
		// redraw the display
		display.repaint();
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

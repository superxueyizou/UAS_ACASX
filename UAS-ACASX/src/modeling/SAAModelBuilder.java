package modeling;

import modeling.encountergenerator.HeadOnGenerator;
import modeling.encountergenerator.SelfGenerator;
import modeling.uas.UAS;
import sim.util.Double2D;
import tools.CONFIGURATION;
/**
 *
 * @author Robert Lee
 * This class is used to build/initiate the simulation.
 * Called for by simulationWithUI.class
 */
public class SAAModelBuilder
{
	public  SAAModel state;
	
	public static double fieldXVal = CONFIGURATION.worldX; 
	public static double fieldYVal = CONFIGURATION.worldY;

	
	public SAAModelBuilder(SAAModel simState)
	{
		state = simState;
	}
	
	public  void generateSimulation()
	{	
		double selfLocX;
		double selfLocY;

		selfLocX= fieldXVal/15;
    	selfLocY= fieldYVal/2;
    	if(state.runningWithUI)
    	{
    		double vx = CONFIGURATION.selfVx;//Math.abs(CONFIGURATION.selfVx*(1+state.random.nextGaussian()));
    		double vy = CONFIGURATION.selfVy;//Math.abs(CONFIGURATION.selfVy*(1+state.random.nextGaussian()));
        	new SelfGenerator(state,selfLocX, selfLocY, vx, vy).execute();
    	}
    	else
    	{
        	new SelfGenerator(state,selfLocX, selfLocY, CONFIGURATION.selfVx, CONFIGURATION.selfVy).execute();
    	}
	
	    if(CONFIGURATION.headOnSelected==1)
	    {  	
	    	
	    	if(state.runningWithUI)
	    	{

		    	for(int i=0; i<CONFIGURATION.headOnTimes; i++)
		    	{		    		
		    		double vx =-CONFIGURATION.headOnVx;
		    		double vy =CONFIGURATION.headOnVy;
		    		double offset = CONFIGURATION.headOnOffset;		    		
		    		
		    		Double2D location = new Double2D(selfLocX+21*(CONFIGURATION.selfVx+CONFIGURATION.headOnVx), selfLocY+offset);
		    		new HeadOnGenerator(state, location,vx, vy).execute();		    		
		    		
		    	}
	    	}
	    	else
	    	{
	    		double vx =-CONFIGURATION.headOnVx;
	    		double vy =CONFIGURATION.headOnVy;
	    		double offset = CONFIGURATION.headOnOffset;
	    			    		
	    		Double2D location = new Double2D(selfLocX+21*(CONFIGURATION.selfVx+CONFIGURATION.headOnVx), selfLocY+offset);
	    		new HeadOnGenerator(state,location,vx, vy).execute();
		    		
	    	}
	    
	    }	    
	  
	
	    for(Object o : state.uasBag)
	    {
	    	UAS uas = (UAS)o;
	    	uas.getCaa().init();	
	    }
				
//		System.out.println("Simulation stepping begins!");
//		System.out.println("====================================================================================================");
		
			
	}
	
}

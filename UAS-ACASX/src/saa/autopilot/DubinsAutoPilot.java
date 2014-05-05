package saa.autopilot;

import java.util.LinkedList;

import sim.engine.SimState;
import sim.util.Double2D;
import tools.CALCULATION;
import tools.CONFIGURATION;
import modeling.SAAModel;
import modeling.env.Destination;
import modeling.env.Waypoint;
import modeling.uas.UAS;

public class DubinsAutoPilot extends AutoPilot
{
	private static final long serialVersionUID = 1L;
	private SAAModel state; 
	private UAS hostUAS;

	public DubinsAutoPilot(SimState simstate, UAS uas) 
	{
		state = (SAAModel)simstate;
		hostUAS = uas;	
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(SimState simState) 
	{
		if(hostUAS.isActive == true)
		{	
			hostUAS.setApWp(execute());
			
		}		
	}
	
	public Waypoint execute()
	{	
		LinkedList<Waypoint> wpQueue = hostUAS.getWpQueue();
		if(wpQueue.size() != 0)
		{
			Waypoint nextWp=(Waypoint)wpQueue.peekFirst();			
			Destination nextDest= (Destination)nextWp;	
			return takeDubinsPath(hostUAS, nextDest);
		}
		else
		{
			return null;
		}
	}	
	
	/* Find the new collision avoidance waypoint for the UAS to go to */
	public Waypoint calculateWaypoint(UAS uas, double turningAngle)
	{			
		Waypoint wp = new Waypoint(state.getNewID(), uas.getDestination());
		wp.setLocation(uas.getLocation().add(uas.getVelocity().masonRotate(turningAngle)));
		return wp;
	}   

    /* 
     * This function is calculates any maneuvers that are necessary for the 
     * current plane to avoid looping. Returns a waypoint based on calculations. 
    */
   public Waypoint takeDubinsPath(UAS uas, Waypoint nextDest) 
   {
       	if (uas.getVelocity().masonAngleWithDouble2D(nextDest.getLocation().subtract(uas.getLocation())) < 2*uas.getUasPerformance().getMaxTurning())
       	{
//       	System.out.println(1);
       		return calculateWaypoint(uas, 0);
       	}
    	
    	boolean isDestOnRight = CALCULATION.rightOf(uas.getLocation(), uas.getLocation().add(uas.getVelocity()), nextDest.getLocation());
    	double angleCoef = isDestOnRight? -1.0 : 1.0;
      	/* Calculate the center of the circle of minimum turning radius on the side that the waypoint is on*/	
    	double minTurningRadius = uas.getSpeed()/uas.getUasPerformance().getMaxTurning();
    	Double2D cPlusCenter = uas.getLocation().add(uas.getVelocity().masonRotate(angleCoef*0.5*Math.PI).normalize().multiply(minTurningRadius));

    	/* If destination is inside circle, must fly opposite direction before we can reach destination*/
    	if ( minTurningRadius > cPlusCenter.distance(nextDest.getLocation())+4*CONFIGURATION.selfSafetyRadius) //-3*CONFIGURATION.selfSafetyRadius
    	{
//    		System.out.println(2);
    		return calculateWaypoint(uas, -1*angleCoef*uas.getUasPerformance().getMaxTurning());
    	}
    	else
    	{
//    		System.out.println(uas+""+isDestOnRight+3);
    		return calculateWaypoint(uas, angleCoef*uas.getUasPerformance().getMaxTurning());
    	}    	   		
    	
    }

}

package saa.autopilot;

import java.util.LinkedList;

import sim.engine.SimState;
import sim.util.Double2D;
import modeling.SAAModel;
import modeling.env.Destination;
import modeling.env.Waypoint;
import modeling.uas.UAS;

public class ToTargetAutoPilot extends AutoPilot
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SAAModel state; 
	private UAS hostUAS;

	public ToTargetAutoPilot(SimState simstate,UAS uas) 
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
			
			Double2D hostUASLocation = hostUAS.getLocation();		
			final Double2D nextWpLocation =nextDest.getLocation();
	 		final double distSqToNextWp = nextWpLocation.distanceSq(hostUASLocation);

	 		Double2D prefVelocity;
	 		double prefSpeed = hostUAS.getUasPerformance().getPrefSpeed();
	 		if (Math.pow(prefSpeed,2) > distSqToNextWp)
	 		{
	 			prefVelocity = nextWpLocation.subtract(hostUASLocation);
	 			
	 		}
	 		else
	 		{
	 			prefVelocity = nextWpLocation.subtract(hostUASLocation).normalize().multiply(prefSpeed); 
	 			
	 		}
	 		
	 		Double2D newVelocity;
			double angle = hostUAS.getVelocity().masonRotateAngleToDouble2D(prefVelocity);	
			if(Math.abs(angle) > hostUAS.getUasPerformance().getMaxTurning())
			{
				newVelocity = hostUAS.getVelocity().masonRotate(hostUAS.getUasPerformance().getMaxTurning()*angle/Math.abs(angle));
			}
			else
			{
				newVelocity = hostUAS.getVelocity().masonRotate(angle);
			}
						
			Waypoint wp= new Waypoint(state.getNewID(), hostUAS.getDestination());
			wp.setLocation(hostUASLocation.add(newVelocity));
			wp.setAction(4);
			return wp;
		}
		else
		{
			return null;
		}
	
	}

	

}

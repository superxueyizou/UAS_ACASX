/**
 * 
 */
package saa.collsionavoidance;

import modeling.SAAModel;
import modeling.env.Destination;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import sim.engine.SimState;
import sim.util.Double2D;

/**
 * @author Xueyi
 *
 */
public class CollisionAvoidanceAlgorithmAdapter extends CollisionAvoidanceAlgorithm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private SAAModel state; 
	private UAS hostUAS;

	private Destination destination;
	Double2D destinationCoor;
	
	public CollisionAvoidanceAlgorithmAdapter(SimState simstate, UAS uas) 
	{
		state = (SAAModel) simstate;
		hostUAS = uas;
		
		destination = hostUAS.getDestination();
		destinationCoor = destination.getLocation();
	}

	public void init()
	{
		
	}
	
	@Override
	public void step(SimState simState)
	{
		if(hostUAS.isActive == true)
		{	
				hostUAS.setCaaWp(execute());		
		}
	}
	
	public Waypoint execute()
	{
		
		return null;
	}
	
	

}

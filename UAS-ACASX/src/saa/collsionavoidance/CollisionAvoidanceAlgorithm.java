/**
 * 
 */
package saa.collsionavoidance;

import sim.engine.SimState;
import sim.engine.Steppable;
import modeling.env.Waypoint;


/**
 * @author Xueyi
 *
 */
public abstract class CollisionAvoidanceAlgorithm implements Steppable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	
	public CollisionAvoidanceAlgorithm() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public abstract void init();
	
	public abstract Waypoint execute();
	
	@Override
	public abstract void step(SimState simState);

}

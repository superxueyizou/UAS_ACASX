/**
 * 
 */
package saa.autopilot;

import sim.engine.SimState;
import sim.engine.Steppable;
import modeling.env.Waypoint;


/**
 * @author Xueyi
 *
 */
public abstract class AutoPilot implements Steppable
{

	/**
	 * 
	 */

	
	public AutoPilot() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public abstract void init();
	@Override
	public abstract void step(SimState simState);
	
	public abstract Waypoint execute();
	


}

package saa.autopilot;

import sim.engine.SimState;
import sim.util.Double2D;
import tools.CONFIGURATION;
import modeling.SAAModel;
import modeling.env.Waypoint;
import modeling.uas.UAS;

public class WhiteNoiseAutoPilot extends AutoPilot
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final double SDEV = 3;
	private SAAModel state; 
	private UAS hostUAS;

	public WhiteNoiseAutoPilot(SimState simstate, UAS uas) 
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
		Waypoint wp = new Waypoint(state.getNewID(), hostUAS.getDestination());
		double x = hostUAS.getLocation().x + hostUAS.getVelocity().x;
		
		double ay = SDEV * state.random.nextGaussian()*(state.random.nextBoolean()?1:-1);
		hostUAS.setVelocity(new Double2D(hostUAS.getVelocity().x, hostUAS.getVelocity().y+ay));
		double y= hostUAS.getLocation().y + hostUAS.getVelocity().y + 0.5*ay;
		wp.setLocation(new Double2D(x , y));		
		return wp;
	}


}

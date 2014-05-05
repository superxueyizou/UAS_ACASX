package modeling.observer;

import modeling.SAAModel;
import modeling.env.Constants;
import modeling.env.Obstacle;
import modeling.uas.UAS;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;


public class OscillationCalculator implements Constants,Steppable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SAAModel state;

	public OscillationCalculator()
	{
		
	}
	
	@Override
	public void step(SimState simState) 
	{
		this.state = (SAAModel)simState;
		UAS uas1;		

	    for(int i=0; i<state.uasBag.size(); i++)
		{	
	    	
			uas1= (UAS)state.uasBag.get(i);
			if(!uas1.isActive)
			{
				continue;
			}

			Double2D oldVelocity = uas1.getOldVelocity();
			Double2D newVelocity = uas1.getVelocity();
			double area =Math.abs(0.5*oldVelocity.negate().perpDot(newVelocity));
			uas1.setTempOscillation(area);
			uas1.setOscillation(uas1.getOscillation()+area);
										
		}
		
	}

}

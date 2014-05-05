package modeling.observer;

import modeling.SAAModel;
import modeling.env.Constants;
import modeling.env.Obstacle;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;


public class OscillationCounter implements Constants,Steppable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SAAModel state;

	public OscillationCounter()
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
			int oscillationNo=0;
			for(int j=0; j<uas1.getAchievedWaypoints().size()-1; j++)
			{
				int wp1Action = ((Waypoint)uas1.getAchievedWaypoints().get(j)).getAction();
				int wp2Action = ((Waypoint)uas1.getAchievedWaypoints().get(j+1)).getAction();
				if(wp1Action!=wp2Action)
				{
					oscillationNo++;
				}
			}
			uas1.setOscillationNo(oscillationNo);	
//			System.out.println(uas1+"  "+ oscillationNo);
		}
		
	}

}

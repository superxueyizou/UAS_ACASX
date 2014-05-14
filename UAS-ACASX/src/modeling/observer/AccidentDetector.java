package modeling.observer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import modeling.SAAModel;
import modeling.env.Constants;
import modeling.uas.UAS;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Double2D;
import tools.CONFIGURATION;


/**
 * 
 */

/**
 * @author xueyi
 *
 */
public class AccidentDetector implements Constants,Steppable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private File accidentLog = new File("AccidentLog.txt");
	private PrintStream ps;
	
	private SAAModel state;
	private int noAccidents=0;
	
	
	public AccidentDetector()
	{
		try{
			ps= new PrintStream(new FileOutputStream(accidentLog));
		}
		catch(FileNotFoundException e)
		{
			System.out.print("File not found!");
			return;
		}
				
	}

	/* (non-Javadoc)
	 * @see state.engine.Steppable#step(state.engine.SimState)
	 */
	@Override
	public void step(SimState simState)
	{
		if(!CONFIGURATION.accidentDetectorEnabler)
		{
			return;
		}
		this.state = (SAAModel)simState;
		
		UAS uas1;
				
        outerLoop:
	    for(int i=0; i<state.uasBag.size(); i++)
		{		    	
			uas1= (UAS)state.uasBag.get(i);
			if(!uas1.isActive)
			{
				continue;
			}
			
			/************
			 * test if there is a collision with other alive UAS
			 */
			for (int k = i+1; k<state.uasBag.size(); k++)
			{
				UAS uas2=(UAS)state.uasBag.get(k);
				if(!uas2.isActive)
				{
					continue;
				}
				if (detectCollisionWithOtherUAS(uas1, uas2))
				{
					addLog(Constants.AccidentType.CLASHWITHOTHERUAS, uas1.getID(), state.schedule.getSteps(), uas1.getLocation(), "the other UAS's ID is"+uas2.getID());
					noAccidents++;
					uas1.isActive=false;
					uas2.isActive=false;
//					i++;
					continue outerLoop;
				}
			}
							
		}

	}
	
	public void addLog(AccidentType t, int uasID, long step, Double2D coor, String str)
	{
		ps.println(t.toString() +":uas"+uasID + "; time:"+step+"steps; location: ("+coor.x+" , "+coor.y+")" + str);
	}
	
	
	private boolean detectCollisionWithOtherUAS(UAS uas1, UAS uas2)
	{
		double deltaH=Math.abs(uas1.getLocation().y-uas2.getLocation().y);
		double deltaV=Math.abs(uas1.getLocation().x-uas2.getLocation().x);
	
		return (deltaV<=500)&&(deltaH<=100);
		
	}
	

	public int getNoAccidents() {
		return noAccidents;
	}

}

package modeling.observer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import modeling.SAAModel;
import modeling.env.CircleObstacle;
import modeling.env.Constants;
import modeling.env.Obstacle;
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
	private Bag trackedUASBag=new Bag();
	private int noAccidents=0;
	
	
	public AccidentDetector()
	{
		// TODO Auto-generated constructor stub		
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
		trackedUASBag.clear();
		for (int i=0; i<state.uasBag.size(); i++)
		{  
		   UAS uas = (UAS)(state.uasBag.get(i));
		   if(uas.isActive)
		   {
			   trackedUASBag.add(uas);
		   }
		}
		
		Obstacle obstacle;
		UAS uas1;
				
        outerLoop:
	    for(int i=0; i<trackedUASBag.size(); i++)
		{	
	    	
			uas1= (UAS)trackedUASBag.get(i);
			if(!uas1.isActive)
			{
				continue;
			}

			/************
			 * test if there is a collision with circular obstacles (exactly, i.e. not including UAS)
			 */
			for(int j=0; j<state.obstacles.size(); j++)
			{
				obstacle=(Obstacle)state.obstacles.get(j);
				if(obstacle.type == Constants.EntityType.TUAS)
				{
						//System.out.println(state.obstacles.get(j)+": this obstacle is UAS, don't mind!");
						continue;
				}
				
				
				if(detectCollisionWithObstacle(uas1, obstacle))
				{
					addLog(Constants.AccidentType.CLASHWITHOBSTACLE, uas1.getID(), state.schedule.getSteps(), uas1.getLocation(), "with octacle id = "+ obstacle.getID() );
					noAccidents++;
					uas1.isActive=false;
					continue outerLoop;
				}
			}

			
			/************
			 * test if there is a collision with other alive UAS
			 */
			for (int k = i+1; k<trackedUASBag.size(); k++)
			{
				UAS uas2=(UAS)trackedUASBag.get(k);
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
	
	private boolean detectCollisionWithObstacle(UAS uAS, Obstacle obstacle)
	{
		//return false;
		if(obstacle.type == Constants.EntityType.TCIROBSTACLE)
		{
			CircleObstacle o=(CircleObstacle)obstacle;
			return uAS.getLocation().distance(o.getLocation())<= uAS.getSafetyRadius()+o.getRadius();
		}
		else
		{
			System.err.println("please decide how to judge collision with non-circular obstacle!");
			return true;
		}
		
	}

	private boolean detectCollisionWithOtherUAS(UAS uas1, UAS uas2)
	{
	
		return uas1.getLocation().distance(uas2.getLocation())<=uas1.getSafetyRadius()+uas2.getSafetyRadius();	
		
	}
	

	public int getNoAccidents() {
		return noAccidents;
	}

}

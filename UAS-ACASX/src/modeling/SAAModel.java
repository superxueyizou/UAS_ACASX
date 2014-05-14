package modeling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import modeling.env.Entity;
import modeling.observer.AccidentDetector;
import modeling.observer.OscillationCalculator;
import modeling.observer.OscillationCounter;
import modeling.observer.ProximityMeasurer;
import modeling.uas.UAS;
import saa.collsionavoidance.mdpLite.ACASXMDP;
import sim.util.*;
import sim.field.continuous.*;
import sim.engine.*;
import tools.CONFIGURATION;

public class SAAModel extends SimState
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean runningWithUI = false; 
	
	public Bag allEntities = new Bag(); // entities to load into the environment, important
//	public Bag toSchedule = new Bag(); // entities to schedule, important	
	public Bag uasBag = new Bag();

    private int newID = 0;	
    public String information="no information now";

    public Continuous2D environment;
	
    public AccidentDetector aDetector= new AccidentDetector();
    public ProximityMeasurer pMeasurer= new ProximityMeasurer();
    public OscillationCalculator oCalculator= new OscillationCalculator();
    public OscillationCounter oCounter= new OscillationCounter();


    	
	/**
	 * Constructor used for setting up a simulation from the COModelBuilder object.
	 * 
	 * @param seed for random number generator
	 * @param x the width of the simulation environment
	 * @param y the height of the simulation environment
	 * @param UI pass true if the simulation is being ran with a UI false if it is not.
	 */
	public SAAModel(long seed, double x, double y, boolean UI)
    {
		super(seed);
		environment = new Continuous2D(1.0, x, y);
		runningWithUI = UI;		
		
	}    
		
	
	public void start()
	{
		super.start();	
		environment.clear();
	
		loadEntities();
		scheduleEntities();			
	}
		

	/**
	 * A method which resets the variables for the COModel and also clears
	 * the schedule and environment of any entities, to be called between simulations.
	 * 
	 * This method resets the newID counter so should NOT be called during a run.
	 */
	public void reset()
	{
		newID = 0;
		uasBag.clear();
//		toSchedule.clear();
		allEntities.clear();
		
		environment.clear(); //clear the environment

	}
	
	public void finish()
	{
		super.finish();		
		OscillationCounter oCounter = new OscillationCounter();
		oCounter.step(this);
		
	}
	
	
	/**
	 * A method which provides a different number each time it is called, this is
	 * used to ensure that different entities are given different IDs
	 * 
	 * @return a unique ID number
	 */
	public int getNewID()
	{
		int t = newID;
		newID++;
		return t;
	}
	
	
	/**
	 * A method which adds all of the entities to the simulations environment.
	 */
	public void loadEntities()
	{
		for(int i = 0; i < allEntities.size(); i++)
		{
			environment.setObjectLocation((Entity) allEntities.get(i), ((Entity) allEntities.get(i)).getLocation());
		
		}
		
	}
	
	
	/**
	 * A method which adds all the entities marked as requiring scheduling to the
	 * schedule for the simulation
	 */
	public void scheduleEntities()
	{
		//loop across all items in toSchedule and add them all to the schedule
		int counter = 0;	
		if (CONFIGURATION.collisionAvoidanceEnabler)
		{
			for(int i = 0; i < uasBag.size(); i++, counter++)
			{
				schedule.scheduleRepeating(((UAS)uasBag.get(i)).getCaa(), counter, 1.0);
			}	
			
		}
		
		for(int i=0; i < uasBag.size(); i++,counter++)
		{
			schedule.scheduleRepeating(((UAS)uasBag.get(i)).getAp(), counter, 1.0);			
		}
		
		for(int i=0; i < uasBag.size(); i++,counter++)
		{
			schedule.scheduleRepeating((Entity) uasBag.get(i), counter, 1.0);
		}	
		schedule.scheduleRepeating(pMeasurer,counter++, 1.0);
		schedule.scheduleRepeating(oCalculator,counter++, 1.0);	
		schedule.scheduleRepeating(aDetector,counter++, 1.0);	
			
	}


	public String getInformation() {
		return information;
	}
   

}

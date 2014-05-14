package modeling.uas;

import modeling.SAAModel;
import modeling.env.Constants;
import modeling.env.Entity;
import modeling.env.Waypoint;
import saa.AutoPilot;
import saa.collsionavoidance.CollisionAvoidanceAlgorithm;
import saa.sense.SensorSet;
import sim.engine.*;
import sim.portrayal.Oriented2D;
import sim.util.*;

/**
 *
 * @author Robert Lee
 */
public class UAS extends Entity implements Oriented2D
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//parameters for subsystems	
	private SensorSet sensorSet;
	private CollisionAvoidanceAlgorithm caa;
	private AutoPilot ap;

	//parameters for UAS movement	
	private Double2D oldLocation;
	private Double2D location;
	private UASVelocity oldUASVelocity;		
	private UASVelocity UASVelocity;	
	
	private Bag achievedWaypoints;

	private UASPerformance uasPerformance;//the set performance for the uas;
	
	//parameters for UAS's sensing capability. They are the result of the sensor subsystem.
	private SenseParas senseParas;

	//parameters for navigation
	private Waypoint nextWp;
	private Waypoint apWp = null;//for auto-pilot

	
	
/*************************************************************************************************/
	//parameters for recording information about simulation
	private double tempDistanceToDanger = Double.MAX_VALUE; //records the closest distance to danger in each step
	private double distanceToDanger = Double.MAX_VALUE; //records the closest distance to danger experienced by the uas
	
	private double tempOscillation = 0; //records the oscillation in each step: area
	private double Oscillation = 0; //records the oscillation in a simulation: area

	private double numOscillation = 0; //records the oscillation times in a simulation
	
/*************************************************************************************************/

	public boolean isActive;	

	private SAAModel state;	


	public UAS(int idNo, Double2D location, UASVelocity uasVelocity, UASPerformance uasPerformance, SenseParas senseParas)
	{
		super(idNo, Constants.EntityType.TUAS);

		this.location=location;
		this.uasPerformance = uasPerformance;
		this.UASVelocity = uasVelocity; 
		this.senseParas = senseParas;
		
		this.oldUASVelocity= uasVelocity;
		this.oldLocation= location;
		
		nextWp=null;
	
		achievedWaypoints = new Bag();	

		this.isActive=true;

	}
	
	public void init(SensorSet sensorSet, AutoPilot ap, CollisionAvoidanceAlgorithm caa)
	{
		this.sensorSet=sensorSet;
		this.ap = ap;
		this.caa = caa;
		
	}
	

	@Override
	public void step(SimState simState)
	{
		state = (SAAModel) simState;		
		if(this.isActive == true)
		{				
			if(apWp != null)
			{
				nextWp=apWp;
				state.environment.setObjectLocation(nextWp, nextWp.getLocation());
		
				this.setOldLocation(this.location);
				this.setLocation(nextWp.getLocation());
				state.environment.setObjectLocation(this, this.location);
				achievedWaypoints.add(nextWp);
				
			}			
			else
			{
				System.out.println("approaching the destination (impossible)!");
			}	
			
		}		
		
		if(state!=null)
		{
			dealWithTermination();
		}
		
    }

//**************************************************************************
	
	public SensorSet getSensorSet() {
		return sensorSet;
	}

	public void setSensorSet(SensorSet sensorSet) {
		this.sensorSet = sensorSet;
	}
	
	public AutoPilot getAp() {
		return ap;
	}

	public void setAp(AutoPilot ap) {
		this.ap = ap;
	}

	public CollisionAvoidanceAlgorithm getCaa() {
		return caa;
	}

	public void setCaa(CollisionAvoidanceAlgorithm aa) {
		this.caa = aa;
	}

	public UASPerformance getStats() {
		return uasPerformance;
	}


	public void setStats(UASPerformance stats) {
		this.uasPerformance = stats;
	}


	
	public double getViewingRange() {
		return this.senseParas.getViewingRange();
	}

	public void setViewingRange(double viewingRange) {
		this.senseParas.setViewingRange(viewingRange);
	}
	
	public double getViewingAngle() {
		return this.senseParas.getViewingAngle();
	}

	public void setViewingAngle(double viewingAngle) {
		this.senseParas.setViewingAngle(viewingAngle);
	}
	
	public double getBearing() {
		return UASVelocity.getBearing();
	}
	
	public double getSpeed() {
		return UASVelocity.getSpeed();
	}

	
	public Double2D getOldVelocity() {
		return oldUASVelocity.getVelocity();
	}
	public void setOldVelocity(Double2D velocity) {
		oldUASVelocity.setVelocity(velocity);
	}

	public Double2D getVelocity() {
		return UASVelocity.getVelocity();
	}
	public void setVelocity(Double2D velocity) {
		UASVelocity.setVelocity(velocity);
	}

	public Double2D getOldLocation() {
		return oldLocation;
	}

	public void setOldLocation(Double2D oldLocation) {
		this.oldLocation = oldLocation;
	}
	
	public Double2D getLocation() {
		return location;
	}

	public void setLocation(Double2D location) {
		this.location = location;
	}

	public Bag getAchievedWaypoints() {
		return achievedWaypoints;
	}


	public UASPerformance getUasPerformance() {
		return uasPerformance;
	}

	public void setUasPerformance(UASPerformance performance) {
		this.uasPerformance = performance;
	}
	
	public double getTempDistanceToDanger() {
		return tempDistanceToDanger;
	}

	public void setTempDistanceToDanger(double tempDistanceToDanger) {
		this.tempDistanceToDanger = tempDistanceToDanger;
	}

	public double getDistanceToDanger() {
		return distanceToDanger;
	}

	public void setDistanceToDanger(double distanceToDanger) {
		this.distanceToDanger = distanceToDanger;
	}
	
	public double getTempOscillation() {
		return tempOscillation;
	}

	public void setTempOscillation(double tempOscillation) {
		this.tempOscillation = tempOscillation;
	}

	public double getOscillation() {
		return Oscillation;
	}

	public void setOscillation(double oscillation) {
		Oscillation = oscillation;
	}

	public double getNumOscillation() {
		return numOscillation;
	}

	public void setNumOscillation(double oscillationNo) {
		numOscillation = oscillationNo;
	}
	
	public Waypoint getApWp() {
		return apWp;
	}

	public void setApWp(Waypoint apWp) {
		this.apWp = apWp;
	}

	public SAAModel getState() {
		return state;
	}

	public void setState(SAAModel state) {
		this.state = state;
	}
	
	/*************
	 * method of sim.portrayal.Oriented2D, for drawing orientation marker
	 */
	public double orientation2D()
	{
		return this.UASVelocity.getVelocity().angle(); // this is different from the angle definition in other parts. reverse the sign.
	}


    public void dealWithTermination()
	{
    	int noActiveAgents =0;
    	for(Object o: state.uasBag)
    	{
    		if(((UAS)o).isActive)
    		{
    			noActiveAgents++;
    		}
    		
    	}
    	
		if(noActiveAgents < 1)
		{
			state.schedule.clear();
			state.kill();
		}
	 }

}

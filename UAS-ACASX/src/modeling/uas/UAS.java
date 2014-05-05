package modeling.uas;

import java.util.LinkedList;

import modeling.SAAModel;
import modeling.env.CircleObstacle;
import modeling.env.Constants;
import modeling.env.Destination;
import modeling.env.Waypoint;
import saa.autopilot.AutoPilot;
import saa.collsionavoidance.CollisionAvoidanceAlgorithm;
import saa.sense.SensorSet;
import sim.engine.*;
import sim.portrayal.Oriented2D;
import sim.util.*;
import tools.CONFIGURATION;

/**
 *
 * @author Robert Lee
 */
public class UAS extends CircleObstacle implements Oriented2D
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
	private UASVelocity oldUASVelocity;		
	private UASVelocity newUASVelocity;	
	
	private Bag achievedWaypoints;

	private UASPerformance uasPerformance;//the set performance for the uas;
	
	//parameters for UAS's sensing capability. They are the result of the sensor subsystem.
	private SenseParas senseParas;

	//parameters for navigation
	private Double2D source;
	private Destination destination;
	private Waypoint nextWp;
	private LinkedList<Waypoint> wpQueue;
	private Waypoint apWp = null;//for auto-pilot
	private Waypoint ssaWp = null;//for self-separation
	private Waypoint caaWp = null;//for collision avoidance
	
	
/*************************************************************************************************/
	//parameters for recording information about simulation
	private double tempDistanceToDanger = Double.MAX_VALUE; //records the closest distance to danger in each step
	private double distanceToDanger = Double.MAX_VALUE; //records the closest distance to danger experienced by the uas
	
	private double tempOscillation = 0; //records the oscillation in each step: area
	private double Oscillation = 0; //records the oscillation in a simulation: area

	private double OscillationNo = 0; //records the oscillation times in a simulation
	
/*************************************************************************************************/
	
	private double safetyRadius;
	public boolean isActive;	

	private SAAModel state;	


	public UAS(int idNo, double safetyRadius, Double2D location, Destination destination, UASVelocity uasVelocity, UASPerformance uasPerformance, SenseParas senseParas)
	{
		super(idNo,safetyRadius, Constants.EntityType.TUAS);
		
		this.safetyRadius= safetyRadius;
		this.location=location;
		this.destination = destination;
		this.uasPerformance = uasPerformance;
		this.newUASVelocity = uasVelocity; 
		this.senseParas = senseParas;
		
		this.oldUASVelocity= uasVelocity;
		this.oldLocation= location;
		
		nextWp=null;
		wpQueue=new LinkedList<Waypoint>();
		wpQueue.offer(destination);
		
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
			if (caaWp != null)
			{
				nextWp=caaWp;
				state.environment.setObjectLocation(nextWp, nextWp.getLocation());
				oldUASVelocity = newUASVelocity;
				newUASVelocity= new UASVelocity(nextWp.getLocation().subtract(location));
				
				this.setOldLocation(this.location);
				this.setLocation(nextWp.getLocation());
				state.environment.setObjectLocation(this, this.location);
				achievedWaypoints.add(nextWp);
//				System.out.println("nextWp == caaWp" );	
			}
			else if(ssaWp != null)
			{
				nextWp=ssaWp;
				state.environment.setObjectLocation(nextWp, nextWp.getLocation());
				oldUASVelocity = newUASVelocity;
				newUASVelocity= new UASVelocity(nextWp.getLocation().subtract(location));
				
				this.setOldLocation(this.location);
				this.setLocation(nextWp.getLocation());
				state.environment.setObjectLocation(this, this.location);
				achievedWaypoints.add(nextWp);
//				System.out.println("nextWp == ssaWp" );	
			}
			else if(apWp != null)
			{
				nextWp=apWp;
				state.environment.setObjectLocation(nextWp, nextWp.getLocation());
				oldUASVelocity = newUASVelocity;
				newUASVelocity= new UASVelocity(nextWp.getLocation().subtract(location));
				
				this.setOldLocation(this.location);
				this.setLocation(nextWp.getLocation());
				state.environment.setObjectLocation(this, this.location);
				achievedWaypoints.add(nextWp);
//				System.out.println("nextWp == apWp" );	
				
			}			
			else
			{
				System.out.println("approaching the destination (impossible)!");
			}

			if (wpQueue.peekFirst()!=null && this.location.distance(wpQueue.peekFirst().getLocation())<3*CONFIGURATION.selfSafetyRadius)
			{
				wpQueue.poll();
			}
			if (this.location.distance(destination.getLocation())<3*CONFIGURATION.selfSafetyRadius)
			{
				this.isActive = false;
				//System.out.println("arrived at the destination!");
			}			
			
		}		
		
		if(state!=null)
		{
			state.dealWithTermination();
		}
		
		//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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


	public Destination getDestination() {
		return destination;
	}


	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public Double2D getSource() {
		return source;
	}

	public void setSource(Double2D source) {
		this.source = source;
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
		return newUASVelocity.getBearing();
	}

	public void setBearing(double bearing) {
		this.newUASVelocity.setBearing(bearing);
	}
	
	public double getSpeed() {
		return newUASVelocity.getSpeed();
	}

	public void setSpeed(double speed) {
		this.newUASVelocity.setSpeed(speed);
	}
	
	public Double2D getOldVelocity() {
		return oldUASVelocity.getVelocity();
	}

	public Double2D getVelocity() {
		return newUASVelocity.getVelocity();
	}
	public void setVelocity(Double2D velocity) {
		newUASVelocity.setVelocity(velocity);
	}

	public Double2D getOldLocation() {
		return oldLocation;
	}

	public void setOldLocation(Double2D oldLocation) {
		this.oldLocation = oldLocation;
	}
	
	public Bag getAchievedWaypoints() {
		return achievedWaypoints;
	}
	
	public double getSafetyRadius() {
		return safetyRadius;
	}

	public void setSafetyRadius(double safetyRadius) {
		this.safetyRadius = safetyRadius;
		this.radius=safetyRadius;
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

	public double getOscillationNo() {
		return OscillationNo;
	}

	public void setOscillationNo(double oscillationNo) {
		OscillationNo = oscillationNo;
	}
	
	public Waypoint getApWp() {
		return apWp;
	}

	public void setApWp(Waypoint apWp) {
		this.apWp = apWp;
	}
	
	public Waypoint getSsaWp() {
		return ssaWp;
	}

	public void setSsaWp(Waypoint ssaWp) {
		this.ssaWp = ssaWp;
	}

	public Waypoint getCaaWp() {
		return caaWp;
	}

	public void setCaaWp(Waypoint caaWp) {
		this.caaWp = caaWp;
	}

	public LinkedList<Waypoint> getWpQueue() {
		return wpQueue;
	}

	public void setWpQueue(LinkedList<Waypoint> wpQueue) {
		this.wpQueue = wpQueue;
	}

	
	public SAAModel getState() {
		return state;
	}

	public void setState(SAAModel state) {
		this.state = state;
	}
	
	/*****************************************
	 * overide method extended from Obstacle
	 */
	public boolean pointInShape(Double2D coord)
	{
		if (location.distance(coord) <= safetyRadius)
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}
	
	
	/*************
	 * method of sim.portrayal.Oriented2D, for drawing orientation marker
	 */
	public double orientation2D()
	{
		return this.newUASVelocity.getVelocity().angle(); // this is different from the angle definition in other parts. reverse the sign.
	}



}

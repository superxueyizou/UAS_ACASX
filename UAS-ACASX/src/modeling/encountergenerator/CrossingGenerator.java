/**
 * 
 */
package modeling.encountergenerator;

import saa.autopilot.AutoPilot;
import saa.autopilot.DubinsAutoPilot;
import saa.autopilot.ToTargetAutoPilot;
import saa.autopilot.WhiteNoiseAutoPilot;
import saa.collsionavoidance.ACASX;
import saa.collsionavoidance.CollisionAvoidanceAlgorithm;
import saa.collsionavoidance.CollisionAvoidanceAlgorithmAdapter;
import saa.sense.ADS_B;
import saa.sense.EOIR;
import saa.sense.PerfectSensor;
import saa.sense.Radar;
import saa.sense.SensorSet;
import saa.sense.TCAS;
import sim.util.Double2D;
import tools.CONFIGURATION;
import modeling.SAAModel;
import modeling.env.Destination;
import modeling.uas.SenseParas;
import modeling.uas.UAS;
import modeling.uas.UASPerformance;
import modeling.uas.UASVelocity;


/**
 * @author Xueyi
 *
 */
public class CrossingGenerator extends EncounterGenerator 
{

	/**
	 * 
	 */
	private SAAModel state;
	private UAS self;
	private double sideFactor;
	private double encounterAngle;
	double intruderSpeed;
	/************
	 * 
	 * @param state
	 * @param uas
	 * @param distance
	 * @param encounterAngle It's ABSOLUTE value is supposed to belong to (0,180). 0 -- tail approach, 180 -- head on
	 * @param intruderSpeed
	 */
	public CrossingGenerator(SAAModel state, UAS uas, double encounterAngle,boolean isRightSide, double intruderSpeed) 
	{
		this.state=state;
		this.self=uas;
		this.sideFactor = isRightSide ? +1:-1; 
		this.encounterAngle = encounterAngle;
		this.intruderSpeed = intruderSpeed;
	}
	
	/*********
	 * self and intruder will encounter at the middle point of each's journey
	 */
	public void execute()
	{
		
		Double2D selfMiddle = self.getLocation().add(self.getDestination().getLocation()).multiply(0.5);
		Double2D selfVector = self.getDestination().getLocation().subtract(self.getLocation());
		Double2D intruderVector = selfVector.rotate(-sideFactor*encounterAngle).multiply(intruderSpeed/self.getSpeed());
		Double2D intruderMiddle = selfMiddle;
				
		Double2D intruderLocation = intruderMiddle.subtract(intruderVector.multiply(0.5));
		Double2D intruderDestinationLoc = intruderMiddle.add(intruderVector.multiply(0.5));
		Destination intruderDestination = new Destination(state.getNewID(), null);
		intruderDestination.setLocation(intruderDestinationLoc);
		UASVelocity intruderVelocity = new UASVelocity(intruderDestination.getLocation().subtract(intruderLocation).normalize().multiply(intruderSpeed));
		UASPerformance intruderPerformance = new UASPerformance(CONFIGURATION.crossingMaxSpeed, CONFIGURATION.crossingMinSpeed, intruderSpeed, CONFIGURATION.crossingMaxClimb, CONFIGURATION.crossingMaxDescent,CONFIGURATION.crossingMaxTurning, CONFIGURATION.crossingMaxAcceleration, CONFIGURATION.crossingMaxDeceleration);
		SenseParas intruderSenseParas = new SenseParas(CONFIGURATION.crossingViewingRange,CONFIGURATION.crossingViewingAngle);

		
		UAS intruder = new UAS(state.getNewID(),CONFIGURATION.crossingSafetyRadius,intruderLocation, intruderDestination, intruderVelocity,intruderPerformance, intruderSenseParas);
		intruder.setSource(intruderLocation);
		
		SensorSet sensorSet = new SensorSet();
		if((CONFIGURATION.crossingSensorSelection&0B10000) == 0B10000)
		{
			sensorSet.perfectSensor=new PerfectSensor();
		}
		if((CONFIGURATION.crossingSensorSelection&0B01000) == 0B01000)
		{
			sensorSet.ads_b=new ADS_B();
		}
		if((CONFIGURATION.crossingSensorSelection&0B00100) == 0B00100)
		{
			sensorSet.tcas=new TCAS();
		}
		if((CONFIGURATION.crossingSensorSelection&0B00010) == 0B00010)
		{
			sensorSet.radar=new Radar();
		}
		if((CONFIGURATION.crossingSensorSelection&0B00001) == 0B00001)
		{
			sensorSet.eoir=new EOIR();
		}
		sensorSet.synthesize();
		
		AutoPilot ap;
		switch (CONFIGURATION.crossingAutoPilotAlgorithmSelection)
		{
			case "ToTarget":
				ap= new ToTargetAutoPilot(state, intruder);
				break;
			case "Dubins":
				ap= new DubinsAutoPilot(state, intruder);
				break;
			case "WhiteNoise":
				ap= new WhiteNoiseAutoPilot(state, intruder);
				break;
			default:
				ap= new ToTargetAutoPilot(state, intruder);
		
		}

		
		CollisionAvoidanceAlgorithm caa;
		switch(CONFIGURATION.crossingCollisionAvoidanceAlgorithmSelection)
		{
			case "ACASXAvoidanceAlgorithm":
				caa= new ACASX(state, intruder);
				break;
			case "None":
				caa= new CollisionAvoidanceAlgorithmAdapter(state, intruder);
				break;
			default:
				caa= new CollisionAvoidanceAlgorithmAdapter(state, intruder);
		}

		intruder.init(sensorSet, ap, caa);
		
		
		state.uasBag.add(intruder);
		state.obstacles.add(intruder);			
		state.allEntities.add(intruderDestination);
		state.allEntities.add(intruder);
		intruder.setSchedulable(true);
		state.toSchedule.add(intruder);
	
	}

}

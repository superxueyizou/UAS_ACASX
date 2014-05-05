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
public class HeadOnGenerator extends EncounterGenerator 
{

	/**
	 * 
	 */
	private SAAModel state;
	
	private UAS self;
	
	private double offset;
	private int sideFactor;
	private double intruderSpeed;
		
	
	public HeadOnGenerator(SAAModel state, UAS uas, double offset, boolean isRightSide, double intruderSpeed) 
	{		
		this.state=state;
		this.self=uas;
		this.offset=offset;
		this.intruderSpeed = intruderSpeed;
		this.sideFactor = isRightSide? +1:-1;
		
	}
	
	public void execute()
	{
		Double2D selfMiddle = self.getLocation().add(self.getDestination().getLocation()).multiply(0.5);
		Double2D selfVector = self.getDestination().getLocation().subtract(self.getLocation());
		Double2D intruderVector = selfVector.negate().multiply(intruderSpeed/self.getSpeed());
		Double2D offsetVector = selfVector.rotate(0.5*sideFactor*Math.PI).resize(offset);
		Double2D intruderMiddle = selfMiddle.add(offsetVector);
				
		Double2D intruderLocation = intruderMiddle.subtract(intruderVector.multiply(0.5));
		Double2D intruderDestinationLoc = intruderMiddle.add(intruderVector.multiply(0.5));
		Destination intruderDestination = new Destination(state.getNewID(), null);
		intruderDestination.setLocation(intruderDestinationLoc);
		
		UASVelocity intruderVelocity = new UASVelocity(intruderVector.resize(intruderSpeed));
		UASPerformance intruderPerformance = new UASPerformance(CONFIGURATION.headOnMaxSpeed, CONFIGURATION.headOnMinSpeed, intruderSpeed, CONFIGURATION.headOnMaxClimb, CONFIGURATION.headOnMaxDescent,CONFIGURATION.headOnMaxTurning, CONFIGURATION.headOnMaxAcceleration, CONFIGURATION.headOnMaxDeceleration);
		SenseParas intruderSenseParas = new SenseParas(CONFIGURATION.headOnViewingRange,CONFIGURATION.headOnViewingAngle);

		
		UAS intruder = new UAS(state.getNewID(),CONFIGURATION.headOnSafetyRadius,intruderLocation, intruderDestination, intruderVelocity,intruderPerformance, intruderSenseParas);
		intruder.setSource(intruderLocation);
		
		SensorSet sensorSet = new SensorSet();
		if((CONFIGURATION.headOnSensorSelection&0B10000) == 0B10000)
		{
			sensorSet.perfectSensor=new PerfectSensor();
		}
		if((CONFIGURATION.headOnSensorSelection&0B01000) == 0B01000)
		{
			sensorSet.ads_b=new ADS_B();
		}
		if((CONFIGURATION.headOnSensorSelection&0B00100) == 0B00100)
		{
			sensorSet.tcas=new TCAS();
		}
		if((CONFIGURATION.headOnSensorSelection&0B00010) == 0B00010)
		{
			sensorSet.radar=new Radar();
		}
		if((CONFIGURATION.headOnSensorSelection&0B00001) == 0B00001)
		{
			sensorSet.eoir=new EOIR();
		}
		sensorSet.synthesize();
		
		AutoPilot ap;
		switch (CONFIGURATION.headOnAutoPilotAlgorithmSelection)
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
		switch(CONFIGURATION.headOnCollisionAvoidanceAlgorithmSelection)
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

		intruder.init(sensorSet,ap,caa);
		
		state.uasBag.add(intruder);
		state.obstacles.add(intruder);		
		state.allEntities.add(intruderDestination);
		state.allEntities.add(intruder);
		intruder.setSchedulable(true);
		state.toSchedule.add(intruder);
				
		
		
	}

}

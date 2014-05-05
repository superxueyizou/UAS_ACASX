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
import sim.util.Bag;
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
public class SelfGenerator
{

	/**
	 * 
	 */
	private SAAModel state;
	private double uasX;
	private double uasY;
	private double distance;
	private double angle;
		
	
	public SelfGenerator(SAAModel state,double uasX, double uasY, double distance, double angle) 
	{		
		this.state=state;
		this.uasX=uasX;
		this.uasY=uasY;
		this.distance=distance;
		this.angle = angle;
	
		
	}
	
	public UAS execute()
	{
		Double2D location = new Double2D(uasX,uasY);
		Destination d = generateDestination(uasX, uasY,distance,angle,state.obstacles);
		UASVelocity uasVelocity = new UASVelocity(d.getLocation().subtract(location).normalize().multiply(CONFIGURATION.selfPrefSpeed));
		UASPerformance uasPerformance = new UASPerformance(CONFIGURATION.selfMaxSpeed, CONFIGURATION.selfMinSpeed, CONFIGURATION.selfPrefSpeed,CONFIGURATION.selfMaxClimb, 
				CONFIGURATION.selfMaxDescent,CONFIGURATION.selfMaxTurning, CONFIGURATION.selfMaxAcceleration, CONFIGURATION.selfMaxDeceleration);
		SenseParas senseParas = new SenseParas(CONFIGURATION.selfViewingRange,CONFIGURATION.selfViewingAngle);
		
		UAS self = new UAS(state.getNewID(),CONFIGURATION.selfSafetyRadius,location, d, uasVelocity,uasPerformance, senseParas);
		self.setSource(location);
		
		SensorSet sensorSet = new SensorSet();
		if((CONFIGURATION.selfSensorSelection&0B10000) == 0B10000)
		{
			sensorSet.perfectSensor=new PerfectSensor();
		}
		if((CONFIGURATION.selfSensorSelection&0B01000) == 0B01000)
		{
			sensorSet.ads_b=new ADS_B();
		}
		if((CONFIGURATION.selfSensorSelection&0B00100) == 0B00100)
		{
			sensorSet.tcas=new TCAS();
		}
		if((CONFIGURATION.selfSensorSelection&0B00010) == 0B00010)
		{
			sensorSet.radar=new Radar();
		}
		if((CONFIGURATION.selfSensorSelection&0B00001) == 0B00001)
		{
			sensorSet.eoir=new EOIR();
		}
		sensorSet.synthesize();
		
		AutoPilot ap;
		switch (CONFIGURATION.selfAutoPilotAlgorithmSelection)
		{
			case "ToTarget":
				ap= new ToTargetAutoPilot(state, self);
				break;
			case "Dubins":
				ap= new DubinsAutoPilot(state, self);
				break;
			case "WhiteNoise":
				ap= new WhiteNoiseAutoPilot(state, self);
				break;
			default:
				ap= new ToTargetAutoPilot(state, self);
		
		}

		CollisionAvoidanceAlgorithm caa;
		switch(CONFIGURATION.selfCollisionAvoidanceAlgorithmSelection)
		{
			case "ACASXAvoidanceAlgorithm":
				caa= new ACASX(state, self);
				break;
			case "None":
				caa= new CollisionAvoidanceAlgorithmAdapter(state, self);
				break;
			default:
				caa= new CollisionAvoidanceAlgorithmAdapter(state, self);
		}	
		
		self.init(sensorSet,ap,caa);
				
		state.uasBag.add(self);
		state.obstacles.add(self);
		state.allEntities.add(self);
		self.setSchedulable(true);
		state.toSchedule.add(self);
		
		return self;
		
	}
	
	public Destination generateDestination(double uasX, double uasY, double distance, double angle, Bag obstacles)
	{
		int dID = state.getNewID();
		Destination d = new Destination(dID, null);
		double desX, desY;
		double delta=0;
		do
		{
			desX = uasX + (distance+delta)* Math.cos(angle);
			desY = uasY - (distance+delta)* Math.sin(angle);
			delta += 1.0;
		}  while (state.obstacleAtPoint(new Double2D(desX,desY), obstacles));
		
		d.setLocation(new Double2D(desX,desY));
		d.setSchedulable(false);
		state.allEntities.add(d);
		return d;
	}

}

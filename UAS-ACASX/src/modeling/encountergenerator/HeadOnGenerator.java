/**
 * 
 */
package modeling.encountergenerator;

import saa.AutoPilot;
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
	
	private Double2D location;
	private double Vx;
	private double Vy;
		
	
	public HeadOnGenerator(SAAModel state, Double2D location, double Vx, double Vy) 
	{		
		this.state=state;
		this.location = location;
		this.Vx = Vx;
		this.Vy = Vy;
		
	}
	
	public void execute()
	{	
		UASVelocity intruderVelocity = new UASVelocity(new Double2D(Vx,Vy));
		UASPerformance intruderPerformance = new UASPerformance(CONFIGURATION.headOnStdDevX, CONFIGURATION.headOnStdDevY,
				CONFIGURATION.headOnMaxSpeed, CONFIGURATION.headOnMinSpeed, Math.sqrt(Vx*Vx+Vy*Vy), CONFIGURATION.headOnMaxClimb, CONFIGURATION.headOnMaxDescent,CONFIGURATION.headOnMaxTurning, CONFIGURATION.headOnMaxAcceleration, CONFIGURATION.headOnMaxDeceleration);
		SenseParas intruderSenseParas = new SenseParas(CONFIGURATION.headOnViewingRange,CONFIGURATION.headOnViewingAngle);
		
		UAS intruder = new UAS(state.getNewID(),location, intruderVelocity,intruderPerformance, intruderSenseParas);
		
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
		
		AutoPilot ap= new AutoPilot(state, intruder,intruderPerformance, CONFIGURATION.headOnAutoPilotAlgorithmSelection,-999);
		
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
		state.allEntities.add(intruder);
		intruder.setSchedulable(true);
	
	}

}

package saa;

import saa.collsionavoidance.mdpLite.Util;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;
import modeling.SAAModel;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import modeling.uas.UASPerformance;

public class AutoPilot implements Steppable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final double SDEV;
	private SAAModel state; 
	private UAS hostUAS;
	private String type;
	private int actionCode;
	private UASPerformance uasPerformance;

	public AutoPilot(SimState simstate, UAS uas, UASPerformance uasPerformance, String type, int actionCode) 
	{
		state = (SAAModel)simstate;
		hostUAS = uas;	
		this.type=type;
		this.actionCode=actionCode;
		this.uasPerformance=uasPerformance;
		this.SDEV=this.uasPerformance.getStdDevY();
	}

	public void init()
	{
		// TODO Auto-generated method stub
		
	}


	public void step(SimState simState) 
	{
		if(hostUAS.isActive == true)
		{	
			if(actionCode>=0)
			{
				hostUAS.setApWp(executeAction(actionCode));				
			}
			else if(type=="WhiteNoise")
			{
				hostUAS.setApWp(executeWhiteNoise());
			}
			else if(type=="Specific")
			{
				double ay=SDEV * state.random.nextGaussian()*(state.random.nextBoolean()?1:-1);
				hostUAS.setApWp(executeSpecific(ay));
			}
//			else if(type=="Dubins")
//			{
//				hostUAS.setApWp(executeDubins());
//			}
//			else if (type=="ToTarget")
//			{
//				hostUAS.setApWp(executeToTarget());
//			}
			
		}		
	}



	private Waypoint executeAction(int actionCode)
	{
		Waypoint wp = new Waypoint(state.getNewID(), null);
		double a=Util.getActionA(actionCode);
		double targetV= Util.getActionV(actionCode);
		double x;
		double y;
		
		double currentV=hostUAS.getVelocity().y;
//		System.out.println(currentV);
		if(!Double.isInfinite(a)&&!Double.isNaN(a)&& (a>0&&targetV>currentV || a<0&&targetV<currentV))
		{
			x = hostUAS.getLocation().x+hostUAS.getVelocity().x;
			y = hostUAS.getLocation().y+hostUAS.getVelocity().y+0.5*a;
			hostUAS.setOldVelocity(new Double2D(hostUAS.getVelocity().x,hostUAS.getVelocity().y));
			hostUAS.setVelocity(new Double2D(hostUAS.getVelocity().x,hostUAS.getVelocity().y+a));
			
		}
		else
		{
			x = hostUAS.getLocation().x + hostUAS.getVelocity().x;				
			double ay = SDEV* state.random.nextGaussian()*(state.random.nextBoolean()?1:-1);
			y= hostUAS.getLocation().y + hostUAS.getVelocity().y + 0.5*ay;
			hostUAS.setOldVelocity(new Double2D(hostUAS.getVelocity().x,hostUAS.getVelocity().y));
			hostUAS.setVelocity(new Double2D(hostUAS.getVelocity().x, hostUAS.getVelocity().y+ay));
			
		}

		wp.setLocation(new Double2D(x,y));
		wp.setAction(actionCode+30);//30 for ACASX
		return wp;
	}
	
	public Waypoint executeWhiteNoise()
	{		
		Waypoint wp = new Waypoint(state.getNewID(), null);
		double vx=hostUAS.getVelocity().x;
		double vy=hostUAS.getVelocity().y;
		double ay = SDEV * state.random.nextGaussian()*(state.random.nextBoolean()?1:-1);
		if(vy+ay>uasPerformance.getMaxClimb())
		{
			ay=uasPerformance.getMaxClimb()-vy;
		}
		else if(vy+ay<-uasPerformance.getMaxDescent())
		{
			ay=-uasPerformance.getMaxDescent()-vy;
		}
		
		double x = hostUAS.getLocation().x + vx;				
		double y= hostUAS.getLocation().y + vy + 0.5*ay;
		hostUAS.setOldVelocity(new Double2D(vx,vy));
		hostUAS.setVelocity(new Double2D(vx, vy+ay));
		wp.setLocation(new Double2D(x , y));		
		return wp;
	}

	public Waypoint executeSpecific(double ay)
	{
		Waypoint wp = new Waypoint(state.getNewID(), null);
		double vx=hostUAS.getVelocity().x;
		double vy=hostUAS.getVelocity().y;		
		if(vy+ay>uasPerformance.getMaxClimb())
		{
			ay=uasPerformance.getMaxClimb()-vy;
		}
		else if(vy+ay<-uasPerformance.getMaxDescent())
		{
			ay=-uasPerformance.getMaxDescent()-vy;
		}
		
		double x = hostUAS.getLocation().x + vx;				
		double y= hostUAS.getLocation().y + vy + 0.5*ay;
		hostUAS.setOldVelocity(new Double2D(vx,vy));
		hostUAS.setVelocity(new Double2D(vx, vy+ay));
		wp.setLocation(new Double2D(x , y));		
		return wp;
	}
	
	public int getActionCode() {
		return actionCode;
	}
	public void setActionCode(int actionCode) {
		this.actionCode = actionCode;
	}
	
/*****************************************************************************************************************/
//	private Waypoint executeToTarget()
//	{
//		LinkedList<Waypoint> wpQueue = hostUAS.getWpQueue();
//		
//		if(wpQueue.size() != 0)
//		{
//			Waypoint nextWp=(Waypoint)wpQueue.peekFirst();
//			
//			Destination nextDest= (Destination)nextWp;	
//			
//			Double2D hostUASLocation = hostUAS.getLocation();		
//			final Double2D nextWpLocation =nextDest.getLocation();
//	 		final double distSqToNextWp = nextWpLocation.distanceSq(hostUASLocation);
//
//	 		Double2D prefVelocity;
//	 		double prefSpeed = hostUAS.getUasPerformance().getPrefSpeed();
//	 		if (Math.pow(prefSpeed,2) > distSqToNextWp)
//	 		{
//	 			prefVelocity = nextWpLocation.subtract(hostUASLocation);
//	 			
//	 		}
//	 		else
//	 		{
//	 			prefVelocity = nextWpLocation.subtract(hostUASLocation).normalize().multiply(prefSpeed); 
//	 			
//	 		}
//	 		
//	 		Double2D newVelocity;
//			double angle = hostUAS.getVelocity().masonRotateAngleToDouble2D(prefVelocity);	
//			if(Math.abs(angle) > hostUAS.getUasPerformance().getMaxTurning())
//			{
//				newVelocity = hostUAS.getVelocity().masonRotate(hostUAS.getUasPerformance().getMaxTurning()*angle/Math.abs(angle));
//			}
//			else
//			{
//				newVelocity = hostUAS.getVelocity().masonRotate(angle);
//			}
//			hostUAS.setOldVelocity(new Double2D(hostUAS.getVelocity().x,hostUAS.getVelocity().y));
//			hostUAS.setVelocity(newVelocity);					
//			Waypoint wp= new Waypoint(state.getNewID(), hostUAS.getDestination());
//			wp.setLocation(hostUASLocation.add(newVelocity));
//			wp.setAction(4);
//			return wp;
//		}
//		else
//		{
//			return null;
//		}
//	}
//	
//	private Waypoint executeDubins() 
//	{
//		LinkedList<Waypoint> wpQueue = hostUAS.getWpQueue();
//		if(wpQueue.size() != 0)
//		{
//			Waypoint nextWp=(Waypoint)wpQueue.peekFirst();			
//			Destination nextDest= (Destination)nextWp;	
//			return takeDubinsPath(hostUAS, nextDest);
//		}
//		else
//		{
//			return null;
//		}
//		
//		return null;
//	}
	
	
	
//    /* 
//     * This function is calculates any maneuvers that are necessary for the 
//     * current plane to avoid looping. Returns a waypoint based on calculations. 
//    */
//   public Waypoint takeDubinsPath(UAS uas, Waypoint nextDest) 
//   {
//       	if (uas.getVelocity().masonAngleWithDouble2D(nextDest.getLocation().subtract(uas.getLocation())) < 2*uas.getUasPerformance().getMaxTurning())
//       	{
//       		return calculateWaypoint(uas, 0);
//       	}
//    	
//    	boolean isDestOnRight = CALCULATION.rightOf(uas.getLocation(), uas.getLocation().add(uas.getVelocity()), nextDest.getLocation());
//    	double angleCoef = isDestOnRight? -1.0 : 1.0;
//      	/* Calculate the center of the circle of minimum turning radius on the side that the waypoint is on*/	
//    	double minTurningRadius = uas.getSpeed()/uas.getUasPerformance().getMaxTurning();
//    	Double2D cPlusCenter = uas.getLocation().add(uas.getVelocity().masonRotate(angleCoef*0.5*Math.PI).normalize().multiply(minTurningRadius));
//
//    	/* If destination is inside circle, must fly opposite direction before we can reach destination*/
//    	if ( minTurningRadius > cPlusCenter.distance(nextDest.getLocation())+4*CONFIGURATION.selfSafetyRadius) //-3*CONFIGURATION.selfSafetyRadius
//    	{
//    		return calculateWaypoint(uas, -1*angleCoef*uas.getUasPerformance().getMaxTurning());
//    	}
//    	else
//    	{
//    		return calculateWaypoint(uas, angleCoef*uas.getUasPerformance().getMaxTurning());
//    	}    	   		
//    	
//    }
//
//	/* Find the new collision avoidance waypoint for the UAS to go to */
//	public Waypoint calculateWaypoint(UAS uas, double turningAngle)
//	{			
//		Waypoint wp = new Waypoint(state.getNewID(), null);
//		wp.setLocation(uas.getLocation().add(uas.getVelocity().masonRotate(turningAngle)));
//		uas.setOldVelocity(new Double2D(uas.getVelocity().x,uas.getVelocity().y));
//		uas.setVelocity(uas.getVelocity().masonRotate(turningAngle));
//		return wp;
//	}   
}

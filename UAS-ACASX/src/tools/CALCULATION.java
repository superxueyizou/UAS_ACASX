/**
 * 
 */
package tools;

import modeling.env.Waypoint;
import modeling.uas.UAS;
import sim.util.Double2D;

/**
 * @author Xueyi
 *
 */
public class CALCULATION 
{
	
	public final static double epsilon = 1.0e-6;

	/**providing calculation tool by some static methods
	 * 
	 */
	public CALCULATION() 
	{
		// TODO Auto-generated constructor stub
	}	
	
/****************************************************************************************************************************************/
		
	public static double det(Double2D v1, Double2D v2)
    {
        return v1.x * v2.y - v1.y * v2.x;
    }
	
    /*
     * a first point of a line
     * b second point of a line
     * c testing point
     */
	public static boolean leftOf(Double2D a, Double2D b, Double2D c)
    {
        return (-det(a.subtract(c), b.subtract(a))>epsilon);
    }
	
	public static boolean rightOf(Double2D a, Double2D b, Double2D c)
    {
        return (det(a.subtract(c), b.subtract(a))>epsilon);
    }
	
    /* Find the new collision avoidance waypoint for the UAS to go to */
	public static Waypoint calculateWaypoint(UAS uas, double turningAngle)
	{			
		Waypoint wp = new Waypoint(uas.getState().getNewID(), uas.getDestination());
		wp.setLocation(uas.getLocation().add(uas.getVelocity().masonRotate(turningAngle)));
		return wp;
	}   

    /* 
     * This function is calculates any maneuvers that are necessary for the 
     * current plane to avoid looping. Returns a waypoint based on calculations. 
    */
   public static Waypoint takeDubinsPath(UAS uas, Waypoint nextDest) 
   {
       	if (uas.getVelocity().masonAngleWithDouble2D(nextDest.getLocation().subtract(uas.getLocation())) < 2*uas.getUasPerformance().getMaxTurning())
       	{
//       	System.out.println(1);
       		return calculateWaypoint(uas, 0);
       	}
    	
    	boolean isDestOnRight = CALCULATION.rightOf(uas.getLocation(), uas.getLocation().add(uas.getVelocity()), nextDest.getLocation());
    	double angleCoef = isDestOnRight? -1.0 : 1.0;
      	/* Calculate the center of the circle of minimum turning radius on the side that the waypoint is on*/	
    	double minTurningRadius = uas.getSpeed()/uas.getUasPerformance().getMaxTurning();
    	Double2D cPlusCenter = uas.getLocation().add(uas.getVelocity().masonRotate(angleCoef*0.5*Math.PI).normalize().multiply(minTurningRadius));

    	/* If destination is inside circle, must fly opposite direction before we can reach destination*/
    	if ( minTurningRadius > cPlusCenter.distance(nextDest.getLocation())+4*CONFIGURATION.selfSafetyRadius) //-3*CONFIGURATION.selfSafetyRadius
    	{
//    		System.out.println(2);
    		return calculateWaypoint(uas, -1*angleCoef*uas.getUasPerformance().getMaxTurning());
    	}
    	else
    	{
//    		System.out.println(uas+""+isDestOnRight+3);
    		return calculateWaypoint(uas, angleCoef*uas.getUasPerformance().getMaxTurning());
    	}    	   		
    	
    }
   
   public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

		Double2D v1= new Double2D(1,1);
		Double2D v2= new Double2D(-1,1);
		Double2D v3= new Double2D(-1,-1);
		Double2D v4= new Double2D(1,-1);
		
		Double2D v5= new Double2D(1,0);
		Double2D v6= new Double2D(0,1);
		Double2D v7= new Double2D(-1,0);
		Double2D v8= new Double2D(0,-1);
		
		Double2D v= v3 ;

		
		System.out.println(rightOf(new Double2D(),v2, v3));
		
	}

}

/**
 * 
 */
package tools;

/**
 * @author Xueyi
 *
 */
public class CONFIGURATION {

	/**
	 * 
	 */
	public CONFIGURATION() 
	{
		
	}
	
	public static double worldX = 50000; 
	public static double worldY = 30000; 

	public static boolean collisionAvoidanceEnabler=true;
	public static boolean autoPilotEnabler=true;
	public static boolean accidentDetectorEnabler=true;
	public static boolean sensorNoiseEnabler=true;
	public static boolean sensorValueUncertainty=true;
		
	public static double selfDestDist = 45000;
	public static double selfDestAngle = Math.toRadians(0);	
	
	public static double selfMaxSpeed =500;
	public static double selfMinSpeed =500;
	public static double selfMaxAcceleration = 0;
	public static double selfMaxDeceleration = 10;
	public static double selfMaxClimb = 42;
	public static double selfMaxDescent = 42;
	public static double selfMaxTurning = Math.toRadians(2.5);
	public static double selfPrefSpeed = 500;
	public static double selfViewingRange =20000;
	public static double selfViewingAngle = Math.toRadians(60);
	public static double selfSafetyRadius=200;
	public static int selfSensorSelection = 0B10000; 
	public static String selfAutoPilotAlgorithmSelection = "WhiteNoise";
	public static String selfCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm"; //"ACASXAvoidanceAlgorithm","RandomAvoidanceAlgorithm", "None"

	
	public static double headOnSelected = 1;
	public static double headOnIsRightSide = 0;
	public static double headOnOffset= 0;
	public static int headOnTimes=1;
	public static double headOnMaxSpeed =500;
	public static double headOnMinSpeed =500;
	public static double headOnMaxAcceleration = 0;
	public static double headOnMaxDeceleration = 10;
	public static double headOnMaxClimb = 42;
	public static double headOnMaxDescent = 42;
	public static double headOnMaxTurning = Math.toRadians(2.5);
	public static double headOnPrefSpeed = 500;
	public static double headOnViewingRange =20000;
	public static double headOnViewingAngle =  Math.toRadians(60);
	public static double headOnSafetyRadius=200;
	public static int headOnSensorSelection = 0B10000; 
	public static String headOnAutoPilotAlgorithmSelection = "WhiteNoise";
	public static String headOnCollisionAvoidanceAlgorithmSelection = "None"; //"ACASXAvoidanceAlgorithm", "RandomAvoidanceAlgorithm", "None"
		
	public static double crossingSelected = 0;
	public static double crossingEncounterAngle= Math.toRadians(30);
	public static double crossingIsRightSide=1;
	public static int crossingTimes=1;
	public static double crossingMaxSpeed =500;
	public static double crossingMinSpeed =500;
	public static double crossingMaxAcceleration = 0;
	public static double crossingMaxDeceleration = 10;
	public static double crossingMaxClimb = 42;
	public static double crossingMaxDescent = 42;
	public static double crossingMaxTurning = Math.toRadians(2.50);
	public static double crossingPrefSpeed = 500;
	public static double crossingViewingRange =20000;
	public static double crossingViewingAngle =  Math.toRadians(60);
	public static double crossingSafetyRadius=200;
	public static int crossingSensorSelection = 0B10000; 
	public static String crossingAutoPilotAlgorithmSelection = "WhiteNoise";
	public static String crossingCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm"; //"ACASXAvoidanceAlgorithm", "RandomAvoidanceAlgorithm", "None"
	
	public static double tailApproachSelected = 0;
	public static double tailApproachOffset = 0;
    public static double tailApproachIsRightSide =1;
	public static int tailApproachTimes=1;
	public static double tailApproachMaxSpeed =500;
	public static double tailApproachMinSpeed =500;
	public static double tailApproachMaxAcceleration = 0;
	public static double tailApproachMaxDeceleration = 10;
	public static double tailApproachMaxClimb = 42;
	public static double tailApproachMaxDescent = 42;
	public static double tailApproachMaxTurning = Math.toRadians(2.50);	
	public static double tailApproachPrefSpeed = 700;
	public static double tailApproachViewingRange =20000;
	public static double tailApproachViewingAngle =  Math.toRadians(60);
	public static double tailApproachSafetyRadius=200;
	public static int tailApproachSensorSelection = 0B10000; 
	public static String tailApproachAutoPilotAlgorithmSelection = "WhiteNoise";
	public static String tailApproachCollisionAvoidanceAlgorithmSelection = "ACASXAvoidanceAlgorithm"; //"ACASXAvoidanceAlgorithm", "RandomAvoidanceAlgorithm", "None"
	
	
}

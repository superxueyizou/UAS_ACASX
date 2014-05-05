package modeling.env;
/**
 *
 * @author Xueyi Zou
 */
public interface Constants
{
	//Entity Types
	public static enum EntityType
	{	
		TOTHER,//a placeholder - save 0 for entities which aren't mentioned elsewhere	
		TUAS,//the type constant of a uas
		TDESTINATION,//the type contant of a destination
		TWAYPOINT,//the type constant of a waypoint
		TCIROBSTACLE,//the type constant of an obstacle
		TWALL,//the type constant of a wall
		TNFZ;//the type constant of a non-fly zone
		
	}
	//Terrain types
	public static enum AirspaceType
	{
		A, //0
		B,//1
		C;//2
		
	}
	
	//Accident types
	public static enum AccidentType
	{
		CLASHWITHOBSTACLE,
		CLASHWITHWALL,
		CLASHWITHOTHERUAS;
		
	}
	
	//Movement Constants
	public static final boolean ACCELERATE = true;
	public static final boolean DECELERATE = false;
}

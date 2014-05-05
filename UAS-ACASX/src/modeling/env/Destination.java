package modeling.env;

/**
 *
 * @author Xueyi Zou
 */
public class Destination extends Waypoint
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Destination(int idNo, Waypoint next)
	{
		super(idNo, null);
	}
	
	public Waypoint getNextWaypoint() {return null;}
}

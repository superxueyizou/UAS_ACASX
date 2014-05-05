package modeling.env;

/**
 *
 * @author Robert Lee
 */
public class Source extends Waypoint
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Waypoint nextWp;

	public Source(int idNo, Waypoint next)
	{
		super(idNo, next);
		nextWp=next;
	}
	
	public Waypoint getNextWaypoint() {return null;}
	
	public Waypoint getNextWp() {
		return nextWp;
	}

	public void setNextWp(Waypoint nextWp) {
		this.nextWp = nextWp;
	}
}

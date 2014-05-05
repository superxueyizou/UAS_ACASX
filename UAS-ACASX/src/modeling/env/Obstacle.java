package modeling.env;
import sim.util.*;

/**
 *
 * @author Robert Lee
 */
public abstract class Obstacle extends Entity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Obstacle(int idNo, EntityType type)
	{
		super(idNo, type);
	}
	
	/**
	 * method which returns true or false if a provided coordinate is in the shape
	 * would have to be overwritten when implemented
	 */
	public abstract boolean pointInShape(Double2D coord);
	
	public abstract boolean inCollisionWith(Double2D coord, double safeMargin);

	/**
	 * Returns the distance from the closest part of the obstacle to the coord provided.
	 * 
	 * @param coord the coordinate the distance to be checked for
	 */
	public abstract double pointToObstacle(Double2D coord);
	
}

package modeling.uas;

import sim.util.Double2D;

public class UASVelocity 
{
	private double bearing = 0; //will be a value between -pi(inc) and pi(exc)
	private double speed = 0; //the speed the vehicle is travelling at
	private Double2D velocity = new Double2D(0,0);
	
	public UASVelocity(Double2D velocity) 
	{
		super();
		this.velocity = velocity;
		this.speed = velocity.length();
		this.bearing = velocity.masonAngle();
	}

	public UASVelocity(double vx, double vy) 
	{
		super();
		this.velocity = new Double2D(vx,vy);
		this.speed = velocity.length();
		this.bearing = velocity.masonAngle();
	}
	
	public double getSpeed() 
	{
		return speed;
	}

//	public void setSpeed(double speed) 
//	{
//		this.speed = speed;
//		this.velocity= this.velocity.normalize().multiply(speed);
//	}

	public double getBearing() 
	{
		return bearing;
	}

//	public void setBearing(double bearing) 
//	{
//		double oldBearing = this.bearing;
//		this.bearing = bearing;
//		this.velocity = this.velocity.masonRotate(this.bearing-oldBearing);
//	}

	public Double2D getVelocity() 
	{
		return velocity;
	}

	public void setVelocity(Double2D velocity) 
	{
		this.velocity = velocity;
		this.speed = this.velocity.length();
		this.bearing = velocity.masonAngle();
	}
	
	public void setVelocity(double vx, double vy) 
	{
		this.velocity = new Double2D(vx,vy);
		this.speed = this.velocity.length();
		this.bearing = velocity.masonAngle();
	}
	
	

}

package dominant;

import ec.util.MersenneTwisterFast;
import saa.collsionavoidance.mdp.ACASXMDP;
import sim.util.Double2D;
import tools.CALCULATION;

public class test {

	public test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

//		Double2D v1= new Double2D(1,1);
//		Double2D v2= new Double2D(-1,1);
//		Double2D v3= new Double2D(-1,-1);
//		Double2D v4= new Double2D(1,-1);
//		
//		Double2D v5= new Double2D(1,0);
//		Double2D v6= new Double2D(0,1);
//		Double2D v7= new Double2D(-1,0);
//		Double2D v8= new Double2D(0,-1);
//		
//		Double2D v= v4;
//		System.out.println(Math.toDegrees(v1.masonAngleWithDouble2D(v)));
//		System.out.println(Math.toDegrees(v1.masonRotateAngleToDouble2D(v)));
//		System.err.println(Math.toDegrees(v.masonLeftRotate(Math.PI/4).masonAngle()));

		

//		System.out.println(Math.toDegrees(CALCULATION.getAngle(v1,v2)));
//		System.out.println(CALCULATION.vectorLRotate(v7, Math.PI/4));
//		float a=1.0f;
//		MersenneTwisterFast r=new MersenneTwisterFast(1233455);
//		System.out.println(r.nextInt());
//		System.out.println(r.nextInt());
//		int a=(int) ( (Double.doubleToLongBits(0.0)^(Double.doubleToLongBits(0.0)>>>32)) );
//		int b=(int) ( (Double.doubleToLongBits(-0.0)^(Double.doubleToLongBits(-0.0)>>>32)) );
//		System.out.println("(1,2,3,0.5)"=="(1,2,3,0.5)");
		
//		46739110=354(400.0,8.4,-29.400000000000002,2,0)//46739153
//		46739110=357(400.0,4.2,42.0,2,0)
				
		long result=1;
		
		double h=400;
		double oVz=4.2;
		double iVz=42;
		int t=2;
		int ra=0;
				
		int a=(int)Math.round(h/ACASXMDP.hRes);
		int b=(int)Math.round(oVz/ACASXMDP.oVRes);		
		int c=(int)Math.round(iVz/ACASXMDP.iVRes);
		double e=Math.round(iVz/ACASXMDP.iVRes);
		System.out.println(e);

		int[] cArr=new int[4];
		cArr[0]=ra;
		cArr[1]=a;
		cArr[2]=b;
		cArr[3]=c;
		for(int i=0;i<cArr.length;i++)
		{
			result=23*result+cArr[i];
		}
		
		result = 43*result+t;
		int hashCode=(int)result;
		System.out.println(result);
		System.out.println(hashCode);
		
		int[] A=new int[]{1,2,3,4,5};
		int[] B=A.clone();
		double[] C = new double[5];
		B[1]=10;
		System.out.println(C[1]);
		
		
//		
	}

}

package saa.collsionavoidance.mdp360;

public class Util 
{
	
	public static double getActionV(int actionCode)
	{
		if(actionCode==-1 )//"Loop"
		{
			System.err.println("no V in Loop action!");
			return Double.NaN;
		}	
		else if(actionCode==0)//"COC"
		{
			return Double.NaN;
		}
		else if(actionCode==1 || actionCode==3) //"CL25" "SCL25"
		{
			return 25;
		}
		else if(actionCode==2 || actionCode==4)//"DES25" "SDES25"
		{
			return -25;
		}		
		else if(actionCode==5)//"SCL42"
		{
			return 42;
		}
		else if(actionCode==6)//"SDES42"
		{
			return -42;
		}
		else
		{
			System.err.println("error happens in Util.getActionV(int actionCode):an unknown aciton.");
			return Double.NaN;
		}
	}
	
	public static double getActionA(int actionCode)
	{
		if(actionCode==-1)//"Loop"
		{
			System.err.println("no A in Loop action!");
			return Double.NaN;
		}
		else if(actionCode==0)//"COC"
		{
			return 0.0;
		}
		else if(actionCode==1 )//"CL25"
		{
			return 8;
		}
		else if(actionCode==2 )//"DES25"
		{
			return -8;
		}		
		else if(actionCode==3 || actionCode==5)//"SCL42" "SCL25"
		{
			return 10.7;
		}
		else if(actionCode==4|| actionCode==6)//"SDES42" "SDES25"
		{
			return -10.7;
		}
		else
		{
			System.err.println("error happens in Util.getActionA(int actionCode):an unknown aciton.");
			return Double.NaN;
		}
	}
}
/**
 * 
 */
package saa.collsionavoidance;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import modeling.SAAModel;
import modeling.env.Waypoint;
import modeling.uas.UAS;
import saa.collsionavoidance.mdp.ACASXMDP;
import saa.collsionavoidance.mdp.ACASXState;
import saa.collsionavoidance.mdp.Util;
import sim.engine.SimState;
import sim.util.Double2D;


/**
 * @author Xueyi
 *
 */
public class ACASX extends CollisionAvoidanceAlgorithm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private SAAModel state; 
	private UAS hostUAS;
	private UAS intruder=null;
	private int ra=0;//"COC"
	
	public ACASX(SimState simstate, UAS uas) 
	{
		state = (SAAModel) simstate;
		hostUAS = uas;
	
	}
	
	
	/******************************************************************************************************************************************/
	
	public void init()
	{
		if(state.uasBag.size()!=2)
		{
			System.err.println("ACASX.java: only two UAVs are allowed in this setting");
		}
		for(int i=0; i<state.uasBag.size(); i++)
		{
			UAS uas= (UAS)state.uasBag.get(i);
			if(uas == hostUAS)
			{
				continue;
			}
			else
			{
				intruder= uas;
			}			
		
		}
	
	}	
	
	
	
	@Override
	public void step(SimState simState)
	{
		if(hostUAS.isActive == true)
		{	
			hostUAS.setCaaWp(execute());
			
		}
		 
	}
	
	
	public Waypoint execute()
	{
		double h=(intruder.getLocation().y-hostUAS.getLocation().y);
		double oVz=hostUAS.getVelocity().y;
		double iVz=intruder.getVelocity().y;
		int t=(int) Math.ceil( (intruder.getLocation().x-hostUAS.getLocation().x)/(hostUAS.getVelocity().x-intruder.getVelocity().x));
		System.out.print(t+": "+h);
		
		double hRes=ACASXMDP.hRes;
		double oVRes=ACASXMDP.oVRes;
		double iVRes=ACASXMDP.iVRes;
		if(Math.abs(h)<1000 && Math.abs(oVz)<=42 && Math.abs(iVz)<=42 && t<=40 && t>0)
		{
			Map<Integer, Double> qValuesMap = new LinkedHashMap<>();
			ArrayList<AbstractMap.SimpleEntry<Integer, Double>> actionMapValues = new ArrayList<AbstractMap.SimpleEntry<Integer, Double>>();

			int hIdxL = (int)Math.floor(h/hRes);
			int oVzIdxL = (int)Math.floor(oVz/oVRes);
			int iVzIdxL = (int)Math.floor(iVz/iVRes);
			for(int i=0;i<=1;i++)
			{
				int hIdx = (i==0? hIdxL : hIdxL+1);
				int hIdxP= hIdx< -ACASXMDP.nh? -ACASXMDP.nh: (hIdx>ACASXMDP.nh? ACASXMDP.nh : hIdx);			
				for(int j=0;j<=1;j++)
				{
					int oVzIdx = (j==0? oVzIdxL : oVzIdxL+1);
					int oVzIdxP= oVzIdx<-ACASXMDP.noV? -ACASXMDP.noV: (oVzIdx>ACASXMDP.noV? ACASXMDP.noV : oVzIdx);
					for(int k=0;k<=1;k++)
					{
						int iVzIdx = (k==0? iVzIdxL : iVzIdxL+1);
						int iVzIdxP= iVzIdx<-ACASXMDP.niV? -ACASXMDP.niV: (iVzIdx>ACASXMDP.niV? ACASXMDP.niV : iVzIdx);
						
						ACASXState approxState= new ACASXState(hIdxP, oVzIdxP, iVzIdxP, t, ra);
						double probability= (1-Math.abs(hIdx-h/hRes))*(1-Math.abs(oVzIdx-oVz/oVRes))*(1-Math.abs(iVzIdx-iVz/iVRes));

						int index=-999;
						int numActions=-999;
						index=state.indexArr.get(approxState.getOrder());
						numActions = state.indexArr.get(approxState.getOrder()+1)-index;
							
						for (int n=0;n<numActions;n++) 
						{
							double qValue= state.costArr.get(index+n);
							int actionCode= state.actionArr.get(index+n);							
							actionMapValues.add(new SimpleEntry<Integer, Double>(actionCode,probability*qValue) );
						}											}
				}
			}
						
			for(AbstractMap.SimpleEntry<Integer, Double> action_value :actionMapValues)
			{
				if(qValuesMap.containsKey(action_value.getKey()))
				{
					qValuesMap.put(action_value.getKey(), qValuesMap.get(action_value.getKey())+action_value.getValue());
				}
				else
				{
					qValuesMap.put(action_value.getKey(), action_value.getValue());
				}
			}
			
			double maxQValue=Double.NEGATIVE_INFINITY;
			int bestActionCode=-999;
			
			Set<Entry<Integer,Double>> entrySet = qValuesMap.entrySet();
			for (Entry<Integer,Double> entry : entrySet) 
			{
				if(entry.getValue()>maxQValue)
				{
					maxQValue=entry.getValue();
					bestActionCode=entry.getKey();
				}
			}
			System.out.println("  Best action code is "+bestActionCode);
			
			ra=bestActionCode;
			
			Waypoint wp = new Waypoint(state.getNewID(), hostUAS.getDestination());
			double a=Util.getActionA(bestActionCode);
			double targetV= Util.getActionV(bestActionCode);
			double x;
			double y;
			
			double currentV=hostUAS.getVelocity().y;
//			System.out.println(currentV);
			if(!Double.isInfinite(a)&&!Double.isNaN(a)&& (a>0&&targetV>currentV || a<0&&targetV<currentV))
			{
				x = hostUAS.getLocation().x+hostUAS.getVelocity().x;
				y = hostUAS.getLocation().y+hostUAS.getVelocity().y+0.5*a;
				hostUAS.setVelocity(new Double2D(hostUAS.getVelocity().x,hostUAS.getVelocity().y+a));
				
			}
			else
			{
				x = hostUAS.getLocation().x+hostUAS.getVelocity().x;
				y = hostUAS.getLocation().y+hostUAS.getVelocity().y;
			}
			wp.setLocation(new Double2D(x,y));
			wp.setAction(bestActionCode+30);//30 for ACASX
			return wp;
		
		}
			
		return null;
		
	
	}
	
}

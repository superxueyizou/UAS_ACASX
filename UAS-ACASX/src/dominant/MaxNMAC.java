/**
 * 
 */
package dominant;

import tools.CONFIGURATION;
import tools.UTILS;
import modeling.SAAModel;
import modeling.SAAModelBuilder;
import modeling.uas.UAS;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.vector.DoubleVectorIndividual;

/**
 * @author Xueyi Zou
 *
 */
public class MaxNMAC extends Problem implements SimpleProblemForm 
{

	/* (non-Javadoc)
	 * @see ec.simple.SimpleProblemForm#evaluate(ec.EvolutionState, ec.Individual, int, int)
	 */
	@Override
	public void evaluate(EvolutionState state, Individual ind, int subpopulation, int threadnum) 
	{
		// TODO Auto-generated method stub
		if (ind.evaluated) return;

        if (!(ind instanceof DoubleVectorIndividual))
            state.output.fatal("Whoa!  It's not a DoubleVectorIndividual!!!",null);        
      
        DoubleVectorIndividual ind2 = (DoubleVectorIndividual)ind;
        double distanceToDangerSum=0;
//      System.out.println(ind2.genome[0]);  
        
        double selfDestDist= ind2.genome[0];
        double selfSpeed= ind2.genome[1];
        
        double headOnSelected;
        if(ind2.genome[14]==0)
    	{
        	headOnSelected= ind2.genome[2];
    	}
        else
        {
        	headOnSelected= ind2.genome[14]==1?1:0;
        	ind2.genome[2]=headOnSelected;        	
        }
        double headOnOffset = ind2.genome[3];
        double headOnIsRightSide = ind2.genome[4];		
		double headOnSpeed = ind2.genome[5];
		
		double crossingSelected;
		if(ind2.genome[14]==0)
    	{
			crossingSelected= ind2.genome[6];
    	}
        else
        {
        	crossingSelected= ind2.genome[14]==2?1:0;
        	ind2.genome[6]=crossingSelected;        	
        }
		double crossingEncounterAngle = ind2.genome[7];
        double crossingIsRightSide = ind2.genome[8];		
		double crossingSpeed = ind2.genome[9];
		
		double tailApproachSelected;
		if(ind2.genome[14]==0)
    	{
			tailApproachSelected= ind2.genome[10];
    	}
        else
        {
        	tailApproachSelected= ind2.genome[14]==3?1:0;
        	ind2.genome[10]=tailApproachSelected;
        }  	
		double tailApproachOffset = ind2.genome[11];
		double tailApproachIsRightSide = ind2.genome[12];
		double tailApproachPrefSpeed= ind2.genome[13];
		
		long time = System.nanoTime();
		SAAModel simState= new SAAModel(785945568, CONFIGURATION.worldX, CONFIGURATION.worldY, false); 	
		
		int times =1, dividend=0;
        for(int i=0;i<times; i++)
        {
    		CONFIGURATION.selfDestDist = selfDestDist;
    		CONFIGURATION.selfPrefSpeed = selfSpeed;
    		
    		CONFIGURATION.headOnSelected = headOnSelected;
    		CONFIGURATION.headOnOffset=headOnOffset;
    		CONFIGURATION.headOnIsRightSide= headOnIsRightSide;    		
    		CONFIGURATION.headOnPrefSpeed =headOnSpeed;
    		
    		CONFIGURATION.crossingSelected = crossingSelected;
    		CONFIGURATION.crossingEncounterAngle=crossingEncounterAngle;
    		CONFIGURATION.crossingIsRightSide= crossingIsRightSide;    		
    		CONFIGURATION.crossingPrefSpeed =crossingSpeed;
    		
    		CONFIGURATION.tailApproachSelected = tailApproachSelected;
    		CONFIGURATION.tailApproachOffset= tailApproachOffset;
    		CONFIGURATION.tailApproachIsRightSide=tailApproachIsRightSide;
    		CONFIGURATION.tailApproachPrefSpeed=tailApproachPrefSpeed;  
    		
    		if((crossingSelected==1)&&
    				(selfSpeed-crossingSpeed>=18)&&(crossingIsRightSide==0)&&
    				(crossingEncounterAngle<=0.9)&&(crossingEncounterAngle>=0.25*Math.PI))
			{
    			
				 ((SimpleFitness)ind2.fitness).setFitness(   state,            
				            0,/// ...the fitness...
				            false);///... is the individual ideal?  Indicate here...

				 ind2.evaluated = true;
				return;
			}
    		
    		if((crossingSelected==1)&&
    				(crossingSpeed- selfSpeed>=18)&&(crossingIsRightSide==1)&&
    				(crossingEncounterAngle<=0.9)&&(crossingEncounterAngle>=0.25*Math.PI))
			{
    			
				 ((SimpleFitness)ind2.fitness).setFitness(   state,            
				            0,/// ...the fitness...
				            false);///... is the individual ideal?  Indicate here...

				 ind2.evaluated = true;
				return;
			}
    		
 	   		SAAModelBuilder sBuilder = new SAAModelBuilder(simState);
    		sBuilder.generateSimulation();
    				
    		for(int m=0; m<simState.uasBag.size(); m++)
    		{
    			UAS uas1 = (UAS)simState.uasBag.get(m);
    			for(int n=m+1; n<simState.uasBag.size(); n++)
    			{
    				UAS uas2 = (UAS)simState.uasBag.get(n);
    				if(uas1.getLocation().distance(uas2.getLocation())<=1000)
    				{
    	    			
						 ((SimpleFitness)ind2.fitness).setFitness(   state,            
						            0,/// ...the fitness...
						            false);///... is the individual ideal?  Indicate here...
	
						 ind2.evaluated = true;
    					return;
    				}
    				
    			}
    			
    		}
    		
//****************************************************************************************
    		
    		simState.start();	
    		do
    		{
    			if (!simState.schedule.step(simState))
    			{
    				break;
    			}
    		} while(simState.schedule.getSteps()< 403);	
    		if(simState.schedule.getSteps()> 400)
    		{
    			
    			 ((SimpleFitness)ind2.fitness).setFitness(  state,            
    			            0,/// ...the fitness...
    			            false);///... is the individual ideal?  Indicate here...

    			ind2.evaluated = true;
    			return;
    		}
    		simState.finish();
//****************************************************************************************
    		
    		for(int j=0; j<simState.uasBag.size(); j++)
    		{
    			UAS uas = (UAS)simState.uasBag.get(j);
    			System.out.println(uas.getDistanceToDanger());

    			if(uas.getDistanceToDanger()>0)
				{
    				distanceToDangerSum += uas.getDistanceToDanger();
				}
    			else
    			{
    				distanceToDangerSum += 0;
    			}
    			
    			dividend++;
    		}
    		
        }
        
		float rawFitness= (float)distanceToDangerSum/dividend;  
		float fitness = 1/Math.abs(1+rawFitness);
        
        
        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        
        ((SimpleFitness)ind2.fitness).setFitness(   state,            
										            fitness,/// ...the fitness...
										            false);///... is the individual ideal?  Indicate here...
        
        ind2.evaluated = true;
        System.out.println("individual result: selfDestDist("+selfDestDist+ "), selfSpeed("+selfSpeed+ "), isRightSide("+headOnIsRightSide+"), offset("+ headOnOffset+"), speed("+ headOnSpeed + "); fitness[[ " + fitness +" ]]" );
        System.out.println();
        
        //if(fitness >0.9)
        {
        	StringBuilder dataItem = new StringBuilder();
        	dataItem.append(state.generation+",");
        	for (int i=0; i< ind2.genome.length-1; i++)
        	{
        		dataItem.append(ind2.genome[i]+",");
        		
        	}
        	dataItem.append(fitness+",");
        	dataItem.append(simState.aDetector.getNoAccidents()+",");
        	dataItem.append(ind2.genome[ind2.genome.length-1]);
        	Simulation.simDataSet.add(dataItem.toString());
        
        }
        MyStatistics.accidents[state.generation]+= simState.aDetector.getNoAccidents();

	}

}

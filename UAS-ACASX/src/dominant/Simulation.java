/**
 * 
 */
package dominant;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import tools.CONFIGURATION;
import tools.UTILS;
import ec.*;
import ec.util.*;

/**
 * @author xueyi
 * simulation with GA as harness
 *
 */
public class Simulation
{
	protected static List<String> simDataSet = new ArrayList<>(200);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		String[] params = new String[]{"-file", "src/dominant/MaxNMAC.params"}; //MaxOscillation, MaxNMAC, MaxArea,RandMaxNMAC, RandMaxOscillation, RandMaxArea
		ParameterDatabase database = Evolve.loadParameterDatabase(params);
		EvolutionState eState= Evolve.initialize(database, 0);
		eState.startFresh();
		int result=EvolutionState.R_NOTDONE;
		
		
		String title = "generation,selfDestDist,selfSpeed,"+
				   "headOnSelected,headOnOffset,headOnIsRightSide,headOnSpeed,"+
                "crossingSelected,crossingEncounterAngle,crossingIsRightSide,crossingSpeed,"+
				   "tailApproachSelected,tailApproachOffset,tailApproachIsRightSide,tailApproachSpeed,"+
                "fitness," +"accident,"+"g14"+"\n";
		boolean isAppending = false;
		String label = database.getLabel();
		String fileName= (String) label.subSequence(label.lastIndexOf("/")+1, label.lastIndexOf("."));
		
		int i=0;
		
		while(result == EvolutionState.R_NOTDONE)
		{
			result=eState.evolve();
			System.out.println("simulation of generation "+i +" finished :)&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
						
			if(simDataSet.size()>=200)
			{  				
				UTILS.writeDataSet2CSV(fileName + "Dataset.csv", title, simDataSet,isAppending);
				isAppending =true;
				simDataSet.clear();
			}
			i++;
		}	
		
//		for(int j=0; j<MyStatistics.accidents.length; j++)
//		{
//			System.out.print(MyStatistics.accidents[j]);
//		}
				
		eState.finish(result);		
		Object[] options= new Object[]{"Recurrence","Weka","Close"};
		int confirmationResult = JOptionPane.showOptionDialog(null, "choose the next step", "What's next", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, options, 0);
		
		if (confirmationResult == 0 )
		{
			String str = UTILS.readLastLine(new File(fileName+"Statics.stat"), "utf-8").trim();
			String[] pArr= str.split(" ");
			//System.out.println(pArr[3]);
					
			CONFIGURATION.selfDestDist= Double.parseDouble(pArr[0]);
			CONFIGURATION.selfPrefSpeed=Double.parseDouble(pArr[1]);
			
			CONFIGURATION.headOnSelected= Double.parseDouble(pArr[2]);
			CONFIGURATION.headOnOffset=Double.parseDouble(pArr[3]);
			CONFIGURATION.headOnIsRightSide= Double.parseDouble(pArr[4]);			
			CONFIGURATION.headOnPrefSpeed=Double.parseDouble(pArr[5]);
			
    		CONFIGURATION.crossingSelected = Double.parseDouble(pArr[6]);
    		CONFIGURATION.crossingEncounterAngle=Double.parseDouble(pArr[7]);
    		CONFIGURATION.crossingIsRightSide= Double.parseDouble(pArr[8]);
    		CONFIGURATION.crossingPrefSpeed =Double.parseDouble(pArr[9]);
    		
    		CONFIGURATION.tailApproachSelected = Double.parseDouble(pArr[10]);
    		CONFIGURATION.tailApproachOffset= Double.parseDouble(pArr[11]);
    		CONFIGURATION.tailApproachIsRightSide=Double.parseDouble(pArr[12]);
    		CONFIGURATION.tailApproachPrefSpeed =Double.parseDouble(pArr[13]);
			
			System.out.println("\nRecurrenceWithGUI");
			SimulationWithUI.main(null);
		}	
		else if (confirmationResult == 2 )
		{
			Evolve.cleanup(eState);	
		}
		
	}
	
}

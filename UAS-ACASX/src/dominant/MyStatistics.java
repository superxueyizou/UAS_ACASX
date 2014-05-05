package dominant;

import ec.EvolutionState;
import ec.simple.SimpleShortStatistics;

public class MyStatistics extends SimpleShortStatistics 
{
	protected static int[] accidents= new int[500];

	public MyStatistics() 
	{
		// TODO Auto-generated constructor stub
		super();
	}
	
	public void postEvaluationStatistics(final EvolutionState state)
	{
		super.postEvaluationStatistics(state);
		state.output.println(accidents[state.generation]+"", statisticslog);
	}

}

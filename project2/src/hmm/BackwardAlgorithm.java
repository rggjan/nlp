package hmm;

import hmm.CachedFunction.IFunction;

import java.util.List;

public class BackwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {
	private CachedFunction<Integer, TState, Double> beta;
	private TStateCollection hmm;
	private List<Word> output;
	
	public double get(int t, TState state){
		return beta.get(t, state);
	}
	
	public BackwardAlgorithm(final TStateCollection hmm, List<Word> output){
		beta=createBeta();
		this.hmm=hmm;
		this.output=output;
	}

	private CachedFunction<Integer, TState, Double> createBeta() {
		return new CachedFunction<Integer, TState, Double>(new IFunction<Integer, TState,  Double>() {

			@Override
			public Double evaluate(Integer index, TState state) {
				if (index==output.size())
				{
					if (state==hmm.endState())
						return 1.0;
					else
						return 0.0; 
				}
				// base case
				if (index==output.size()-1){
					return 
						state.nextStateProbability(hmm.endState());
				}
				
				// recursion
				double result=0;
				for (TState nextState: hmm.getStates()){
					if (nextState==hmm.endState()||nextState==hmm.startState()) continue;
					result+=
						beta.get(index+1, nextState)
						*state.nextStateProbability(nextState)
						*nextState.wordEmittingProbability(output.get(index+1));
				}
				
				return result;
			}
		}) ;
	}
	
	// TODO: Check if this is the same for forward and backward!
	public double getFinalProbability() {
		// TODO cache that function?

		double sum = 0;

		for (TState state : hmm.getStates()) {
			if (state==hmm.endState()||state==hmm.startState()) continue;
			double value;

			value = get(0, state);
			value *= hmm.startState().nextStateProbability(state);
			value *= state.wordEmittingProbability(output.get(0));
			
			sum += value;
		}
		return sum;
	}
}

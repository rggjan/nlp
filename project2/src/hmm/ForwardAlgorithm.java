package hmm;

import hmm.CachedFunction.IFunction;

import java.util.List;

public class ForwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {

	private CachedFunction<Integer, TState, Double> alpha;
	private TStateCollection hmm;
	private List<Word> output;
	
	public double get(int t, TState state){
		return alpha.get(t, state);
	}
	
	public ForwardAlgorithm(final TStateCollection hmm, List<Word> output){
		this.hmm=hmm;
		this.output=output;
		alpha=createAlpha();
	}

	private CachedFunction<Integer, TState, Double> createAlpha() {
		return new CachedFunction<Integer, TState, Double>(new IFunction<Integer, TState,  Double>() {

			@Override
			public Double evaluate(Integer index, TState state) {
				if (index==-1){
					if (state==hmm.startState())
						return 1.0;
					else
						return 0.0;
				}
				// base case
				if (index==0){
					return 
						hmm.startState().nextStateProbability(state)
						*state.wordEmittingProbability(output.get(0));
				}
				
				// recursion
				double result=0;
				for (TState prevState: hmm.getStates()){
					if (prevState==hmm.endState()||prevState==hmm.startState()) continue;
					result+=
						alpha.get(index-1, prevState)
						*prevState.nextStateProbability(state)
						*state.wordEmittingProbability(output.get(index));
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
		
			value = get(output.size() - 1, state);
			value *= state.nextStateProbability(hmm.endState());
			
			sum += value;
		}
		return sum;
	}
}

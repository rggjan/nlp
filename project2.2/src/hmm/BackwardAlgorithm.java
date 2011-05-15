package hmm;

import hmm.CachedFunction.IFunction;

import java.util.List;

public class BackwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {
	private final CachedFunction<Integer, TState, BigDouble> beta;
	private final TStateCollection hmm;
	private final List<Word> output;
	
	public BigDouble get(int t, TState state){
		return beta.get(t, state);
	}
	
	public BackwardAlgorithm(final TStateCollection hmm, List<Word> output){
		beta=createBeta();
		this.hmm=hmm;
		this.output=output;
	}

	private CachedFunction<Integer, TState, BigDouble> createBeta() {
		return new CachedFunction<Integer, TState, BigDouble>(new IFunction<Integer, TState,  BigDouble>() {

			@Override
			public BigDouble evaluate(Integer index, TState state) {
				if (index==output.size())
				{
					if (state==hmm.endState())
						return BigDouble.ONE;
					else
						return BigDouble.ZERO; 
				}
				// base case
				if (index==output.size()-1){
					return 
						beta.get(index+1, hmm.endState())
						.multiply(state.nextStateProbability(hmm.endState()));
				}
				
				// recursion
				BigDouble result=BigDouble.ZERO;
				for (TState nextState: hmm.getStates()){
					if (nextState==hmm.endState()||nextState==hmm.startState()) continue;
					result=result.add(
						beta.get(index+1, nextState)
						.multiply(state.nextStateProbability(nextState))
						.multiply(nextState.wordEmittingProbability(output.get(index+1))));
				}
				
				return result;
			}
		}) ;
	}
	
	// TODO: Check if this is the same for forward and backward!
	public BigDouble getFinalProbability() {
		// TODO cache that function?

		BigDouble sum = BigDouble.ZERO;

		for (TState state : hmm.getStates()) {
			if (state==hmm.endState()||state==hmm.startState()) continue;
			BigDouble value;

			value = 
				get(0, state)
				.multiply(hmm.startState().nextStateProbability(state))
				.multiply(state.wordEmittingProbability(output.get(0)));
			
			sum=sum.add(value);
		}
		return sum;
	}
}

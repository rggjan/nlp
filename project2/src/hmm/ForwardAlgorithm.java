package hmm;

import hmm.CachedFunction.IFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class ForwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {
	private static MathContext mc=new MathContext(10,RoundingMode.HALF_EVEN);
	private CachedFunction<Integer, TState, BigDouble> alpha;
	private TStateCollection hmm;
	private List<Word> output;
	
	public BigDouble get(int t, TState state){
		return alpha.get(t, state);
	}
	
	public ForwardAlgorithm(final TStateCollection hmm, List<Word> output){
		this.hmm=hmm;
		this.output=output;
		alpha=createAlpha();
	}

	private CachedFunction<Integer, TState, BigDouble> createAlpha() {
		return new CachedFunction<Integer, TState, BigDouble>(new IFunction<Integer, TState,  BigDouble>() {

			@Override
			public BigDouble evaluate(Integer index, TState state) {
				if (index==-1){
					if (state==hmm.startState())
						return BigDouble.ONE;
					else
						return BigDouble.ZERO;
				}

				// recursion
				BigDouble result=BigDouble.ZERO;
				for (TState prevState: hmm.getStates()){
					if (prevState==hmm.endState()) continue;
					result=result.add(
						alpha.get(index-1, prevState)
						.multiply(prevState.nextStateProbability(state))
						.multiply(state.wordEmittingProbability(output.get(index))));
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
				get(output.size() - 1, state)
				.multiply(state.nextStateProbability(hmm.endState()));
			
			sum=sum.add(value);
		}
		return sum;
	}
}

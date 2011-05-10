package hmm;

import hmm.CachedFunction.IFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class ForwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {
	private static MathContext mc=new MathContext(10,RoundingMode.HALF_EVEN);
	private CachedFunction<Integer, TState, BigDecimal> alpha;
	private TStateCollection hmm;
	private List<Word> output;
	
	public BigDecimal get(int t, TState state){
		return alpha.get(t, state);
	}
	
	public ForwardAlgorithm(final TStateCollection hmm, List<Word> output){
		this.hmm=hmm;
		this.output=output;
		alpha=createAlpha();
	}

	private CachedFunction<Integer, TState, BigDecimal> createAlpha() {
		return new CachedFunction<Integer, TState, BigDecimal>(new IFunction<Integer, TState,  BigDecimal>() {

			@Override
			public BigDecimal evaluate(Integer index, TState state) {
				if (index==-1){
					if (state==hmm.startState())
						return BigDecimal.ONE;
					else
						return BigDecimal.ZERO;
				}
				// base case
				if (index==0){
					return 
						hmm.startState().nextStateProbability(state)
						.multiply(state.wordEmittingProbability(output.get(0)),mc);
				}
				
				// recursion
				BigDecimal result=BigDecimal.ZERO;
				for (TState prevState: hmm.getStates()){
					if (prevState==hmm.endState()||prevState==hmm.startState()) continue;
					result=result.add(
						alpha.get(index-1, prevState)
						.multiply(prevState.nextStateProbability(state),mc)
						.multiply(state.wordEmittingProbability(output.get(index)),mc),mc);
				}
				
				return result;
			}
		}) ;
	}
	
	// TODO: Check if this is the same for forward and backward!
	public BigDecimal getFinalProbability() {
		// TODO cache that function?

		BigDecimal sum = BigDecimal.ZERO;

		for (TState state : hmm.getStates()) {
			if (state==hmm.endState()||state==hmm.startState()) continue;
			BigDecimal value;
		
			value = 
				get(output.size() - 1, state)
				.multiply(state.nextStateProbability(hmm.endState()),mc);
			
			sum=sum.add(value);
		}
		return sum;
	}
}

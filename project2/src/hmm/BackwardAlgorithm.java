package hmm;

import hmm.CachedFunction.IFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

public class BackwardAlgorithm<TStateCollection extends StateCollection<TState>,TState extends State> {
	private static MathContext mc=new MathContext(10,RoundingMode.HALF_EVEN);
	private CachedFunction<Integer, TState, BigDecimal> beta;
	private TStateCollection hmm;
	private List<Word> output;
	
	public BigDecimal get(int t, TState state){
		return beta.get(t, state);
	}
	
	public BackwardAlgorithm(final TStateCollection hmm, List<Word> output){
		beta=createBeta();
		this.hmm=hmm;
		this.output=output;
	}

	private CachedFunction<Integer, TState, BigDecimal> createBeta() {
		return new CachedFunction<Integer, TState, BigDecimal>(new IFunction<Integer, TState,  BigDecimal>() {

			@Override
			public BigDecimal evaluate(Integer index, TState state) {
				if (index==output.size())
				{
					if (state==hmm.endState())
						return BigDecimal.ONE;
					else
						return BigDecimal.ZERO; 
				}
				// base case
				if (index==output.size()-1){
					return 
						state.nextStateProbability(hmm.endState());
				}
				
				// recursion
				BigDecimal result=BigDecimal.ZERO;
				for (TState nextState: hmm.getStates()){
					if (nextState==hmm.endState()||nextState==hmm.startState()) continue;
					result=result.add(
						beta.get(index+1, nextState)
						.multiply(state.nextStateProbability(nextState))
						.multiply(nextState.wordEmittingProbability(output.get(index+1))),mc);
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
				get(0, state)
				.multiply(hmm.startState().nextStateProbability(state))
				.multiply(state.wordEmittingProbability(output.get(0)),mc);
			
			sum=sum.add(value);
		}
		return sum;
	}
}

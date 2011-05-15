package hmm;

import java.util.*;

public class Viterbi<TStateCollection extends StateCollection<TState>,TState extends State> {
	
	private CachedFunction<Integer,TState,BigDouble> v;
	private CachedFunction<Integer,TState,TState> d;
	
	// list of the best states found so far
	private List<TState> states=new LinkedList<TState>();
	private BigDouble probability;
	
	private Viterbi(final TStateCollection hmm, final List<String> output) {
		// create cached method for v
		v=createV(hmm, output);
		// create cached method for d
		d=createD(hmm, output);
		
		probability=BigDouble.valueOf(-1);
		
		TState bestLast=null;
		for (TState prevState:  hmm.getStates()){
			if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
			
			BigDouble f=	
				v.get(output.size()-1, prevState)
				.multiply(prevState.nextStateProbability(hmm.endState()));
			if (f.compareTo(probability)>0) {
				probability=f;
				bestLast=prevState;
			}
		}
		
		fillStates(output.size()-1,bestLast);
	}


	private void fillStates(int t,TState bestLast) {
		if (t>0){
			fillStates(t-1, d.get(t, bestLast));
		}
		states.add(bestLast);
	}


	private CachedFunction<Integer, TState, TState> createD(final TStateCollection hmm,
			final List<String> output) {
		return new CachedFunction<Integer, TState, TState>(new CachedFunction.IFunction<Integer, TState, TState>() {

			@Override
			public TState evaluate(Integer t, TState state) {
				if (t==0){
					return hmm.startState();
				}
				BigDouble result=BigDouble.valueOf(-1);
				TState bestPrev=null;
				for (TState prevState:  hmm.getStates()){
					if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
					BigDouble f=
						v.get(t-1, prevState)
						.multiply(prevState.nextStateProbability(state))
						.multiply(state.wordEmittingProbability(hmm.getWord(output.get(t))));
					if (f.compareTo(result)>0) {
						result=f;
						bestPrev=prevState;
					}
				}
				return bestPrev;
			}
		});
	}


	private CachedFunction<Integer, TState, BigDouble> createV(final TStateCollection hmm,
			final List<String> output) {
		return new CachedFunction<Integer, TState, BigDouble>(new CachedFunction.IFunction<Integer, TState, BigDouble>() {

			@Override
			public BigDouble evaluate(Integer t, TState state) {
				if (t==0){
					return 
						hmm.startState().nextStateProbability(state)
						.multiply(state.wordEmittingProbability(hmm.getWord(output.get(t))));
				}
				BigDouble result=BigDouble.ZERO;
				for (TState prevState:  hmm.getStates()){
					if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
					BigDouble f=
						v.get(t-1, prevState)
						.multiply(prevState.nextStateProbability(state))
						.multiply(state.wordEmittingProbability(hmm.getWord(output.get(t))));
					if (f.compareTo(result)>0) result=f;
				}
				return result;
			}
		});
	}

	
	public static <TStateCollection extends StateCollection<TState>,TState extends State>
		Viterbi<TStateCollection,TState> viterbi(TStateCollection hmm, List<String> output){
		return new Viterbi<TStateCollection,TState>(hmm,output);
	}


	public List<TState> getStates() {
		return states;
	}

	public BigDouble getProbability() {
		return probability;
	}
}

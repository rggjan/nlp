package hmm;

import java.util.*;

public class Viterbi<TStateCollection extends StateCollection<TState>,TState extends State> {
	
	private CachedFunction<Integer,TState,Double> v;
	private CachedFunction<Integer,TState,TState> d;
	
	// list of the best states found so far
	private List<TState> states=new LinkedList<TState>();
	private double probability;
	
	private Viterbi(final TStateCollection hmm, final List<String> output) {
		// create cached method for v
		v=createV(hmm, output);
		// create cached method for d
		d=createD(hmm, output);
		
		probability=-1;
		
		TState bestLast=null;
		for (TState prevState:  hmm.getStates()){
			if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
			
			double f=	
				v.get(output.size()-1, prevState)
				*prevState.nextStateProbability(hmm.endState());
			if (f>probability) {
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
				double result=-1;
				TState bestPrev=null;
				for (TState prevState:  hmm.getStates()){
					if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
					double f=
						v.get(t-1, prevState)
						*prevState.nextStateProbability(state)
						*state.wordEmittingProbability(hmm.getWord(output.get(t)));
					if (f>result) {
						result=f;
						bestPrev=prevState;
					}
				}
				return bestPrev;
			}
		});
	}


	private CachedFunction<Integer, TState, Double> createV(final TStateCollection hmm,
			final List<String> output) {
		return new CachedFunction<Integer, TState, Double>(new CachedFunction.IFunction<Integer, TState, Double>() {

			@Override
			public Double evaluate(Integer t, TState state) {
				if (t==0){
					return 
						hmm.startState().nextStateProbability(state)
						*state.wordEmittingProbability(hmm.getWord(output.get(t)));
				}
				double result=0;
				for (TState prevState:  hmm.getStates()){
					if (prevState==hmm.startState() || prevState==hmm.endState()) continue;
					double f=
						v.get(t-1, prevState)
						*prevState.nextStateProbability(state)
						*state.wordEmittingProbability(hmm.getWord(output.get(t)));
					if (f>result) result=f;
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

	public double getProbability() {
		return probability;
	}
}

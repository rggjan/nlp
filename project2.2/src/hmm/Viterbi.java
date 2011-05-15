package hmm;

import java.util.*;

public class Viterbi {
	
	private Cache<Integer,State,Double> v;
	private Cache<Integer,State,State> d;
	
	private List<State> states=new LinkedList<State>();
	private double probability;
	
	private Viterbi(final StateCollection hmm, final List<String> output) {
		// create cached method for v
		v=createV(hmm, output);
		// create cached method for d
		d=createD(hmm, output);
		
		probability=-1;
		
		State bestLast=null;
		for (State prevState:  hmm.states.values()){
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


	private void fillStates(int t,State state) {
		if (t>0){
			fillStates(t-1, d.get(t, state));
		}
		states.add(state);
	}


	private Cache<Integer, State, State> createD(final StateCollection hmm,
			final List<String> output) {
		return new Cache<Integer, State, State>(new Cache.CachedFunction<Integer, State, State>() {

			@Override
			public State evaluate(Integer t, State state) {
				if (t==0){
					return hmm.startState();
				}
				double result=-1;
				State bestPrev=null;
				for (State prevState:  hmm.states.values()){
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


	private Cache<Integer, State, Double> createV(final StateCollection hmm,
			final List<String> output) {
		return new Cache<Integer, State, Double>(new Cache.CachedFunction<Integer, State, Double>() {

			@Override
			public Double evaluate(Integer t, State state) {
				if (t==0){
					return 
						hmm.startState().nextStateProbability(state)
						*state.wordEmittingProbability(hmm.getWord(output.get(t)));
				}
				double result=0;
				for (State prevState:  hmm.states.values()){
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

	
	public static Viterbi viterbi(StateCollection hmm, List<String> output){
		return new Viterbi(hmm,output);
	}


	public List<State> getStates() {
		return states;
	}

	public double getProbability() {
		return probability;
	}
}

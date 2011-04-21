package hmm;

import java.util.*;

public class Viterbi {
	
	private Cache<Integer,String,Double> v;
	private Cache<Integer,String,State> d;
	
	private List<String> states=new LinkedList<String>();
	private double probability;
	
	private Viterbi(final StateCollection hmm, final List<String> output) {
		// create cached method for v
		v=createV(hmm, output);
		// create cached method for d
		d=createD(hmm, output);
		
		probability=-1;
		
		State bestLast=null;
		for (State prevState:  hmm.states.values()){
			if (prevState.name==null || prevState.name.equals("")) continue;
			double f=	v.get(output.size()-1, prevState.name)
				*prevState.nextTagProbability(null);
			if (f>probability) {
				probability=f;
				bestLast=prevState;
			}
		}
		
		fillStates(output.size()-1,bestLast);
	}


	private void fillStates(int t,State state) {
		if (t>0){
			fillStates(t-1, d.get(t, state.name));
		}
		states.add(state.name);
	}


	private Cache<Integer, String, State> createD(final StateCollection hmm,
			final List<String> output) {
		return new Cache<Integer, String, State>(new Cache.CachedFunction<Integer, String, State>() {

			@Override
			public State evaluate(Integer t, String state) {
				if (t==0){
					return hmm.states.get("");
				}
				double result=-1;
				State bestPrev=null;
				for (State prevState:  hmm.states.values()){
					if (prevState.name==null || prevState.name.equals("")) continue;
					double f=
						v.get(t-1, prevState.name)
						*prevState.nextTagProbability(state)
						*hmm.states.get(state).wordEmittingProbability(output.get(t));
					if (f>result) {
						result=f;
						bestPrev=prevState;
					}
				}
				return bestPrev;
			}
		});
	}


	private Cache<Integer, String, Double> createV(final StateCollection hmm,
			final List<String> output) {
		return new Cache<Integer, String, Double>(new Cache.CachedFunction<Integer, String, Double>() {

			@Override
			public Double evaluate(Integer t, String state) {
				if (t==0){
					return 
						hmm.states.get("").nextTagProbability(state)
						*hmm.states.get(state).wordEmittingProbability(output.get(t));
				}
				double result=0;
				for (State prevState:  hmm.states.values()){
					if (prevState.name==null || prevState.name.equals("")) continue;
					double f=
						v.get(t-1, prevState.name)
						*prevState.nextTagProbability(state)
						*hmm.states.get(state).wordEmittingProbability(output.get(t));
					if (f>result) result=f;
				}
				return result;
			}
		});
	}

	
	public static Viterbi viterbi(StateCollection hmm, List<String> output){
		return new Viterbi(hmm,output);
	}


	public List<String> getStates() {
		return states;
	}

	public double getProbability() {
		return probability;
	}
}

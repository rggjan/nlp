package hmm;

import java.util.HashMap;

/**
 * represents a state of a HMM
 * @author ruedi
 *
 */
public class State {
	private final HashMap<Word, Double> emittedWordCounts = new HashMap<Word, Double>();
	private final HashMap<State, Double> nextStateCounts = new HashMap<State, Double>();
	StateCollection stateCollection;
	String name;

	public State(String name_, StateCollection _stateCollection) {
		name = name_;
		stateCollection=_stateCollection;
	}

	public void addWordEmissionObservation(Word word) {
		// Laplace, renormalization
		if (!emittedWordCounts.containsKey(word)) {
			emittedWordCounts.put(word, 1.0);
		}
		else{
			emittedWordCounts.put(word, emittedWordCounts.get(word) + 1);
		}
	}

	public void setWordEmissionObservations(Word word,
			double numberOfObservations) {
		emittedWordCounts.put(word, numberOfObservations);
	}

	// Add next State, null is final State
	public void addStateTransitionObservation(State nextState) {
		// Laplace, renormalization
		if (!nextStateCounts.containsKey(nextState)) {
			nextStateCounts.put(nextState, 1.0);
		}
		else{
			nextStateCounts.put(nextState, nextStateCounts.get(nextState) + 1);
		}
	}

	public void setStateTransitionObservation(State nextState, double epQiQj) {
		nextStateCounts.put(nextState, epQiQj);
	}

	@Override
	public String toString() {
		return "State(" + name + ")";
	}

	/**
	 * count how many times a given word has been emitted
	 * @param word
	 * @return
	 */
	public int wordCount(Word word){
		int result=1;
		if (emittedWordCounts.containsKey(word))
			result+=emittedWordCounts.get(word);
		return result;
	}

	private int totalWordCountCache=-1;
	/**
	 * caclulate the count of all emitted words
	 * @return
	 */
	public int totalWordCount(){
		if (stateCollection.isFrozen() && totalWordCountCache!=-1) return totalWordCountCache;
		int result=0;
		// iterate over all words ever seen, to get numbers useful for a true
		// probability
		for (Word w: stateCollection.words.values()){
			result+=wordCount(w);
		}

		if (stateCollection.isFrozen()) totalWordCountCache=result;
		return result;
	}

	public int nextStateCount(State state){
		int result=1;
		if (nextStateCounts.containsKey(state)){
			result+=nextStateCounts.get(state);
		}
		return result;
	}

	private int totalNextStateCountCache=-1;
	public int totalNextStateCount(){
		if (stateCollection.isFrozen()&&totalNextStateCountCache!=-1) return totalNextStateCountCache;

		int result=0;
		// iterate over all states, to get numbers useful for a true
		// probability
		for (State s: stateCollection.states.values()){
			result+=nextStateCount(s);
		}

		if (stateCollection.isFrozen()) totalNextStateCountCache=result;
		return result;
	}

	public double wordEmittingProbability(Word word) {
		return (double) wordCount(word)/(double)totalWordCount();
	}

	public double nextStateProbability(State state) {
		return (double) nextStateCount(state)/(double)totalNextStateCount();
	}
}

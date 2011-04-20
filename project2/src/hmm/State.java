package hmm;

import java.util.HashMap;

/**
 * represents a state of a HMM
 * @author ruedi
 *
 */
public class State {
	private HashMap<String, Integer> emittedWordCounts;
	private HashMap<String, Integer> nextStateCounts;
	
	private int numWords = 0;
	private int numStates;
	
	String name;

	public State(String name_) {
		emittedWordCounts = new HashMap<String, Integer>();
		nextStateCounts = new HashMap<String, Integer>();
		
		name = name_;
	}

	public void addWordEmissionObservation(String word) {
		// Laplace, renormalization
		if (!emittedWordCounts.containsKey(word)) {
			emittedWordCounts.put(word, 1);
			numWords++;
		}
		
		emittedWordCounts.put(word, emittedWordCounts.get(word) + 1);
		numWords++;
	}

	// Add next State, null is final State
	public void addStateTransitionObservation(String nextState) {
		// Laplace, renormalization
		if (!nextStateCounts.containsKey(nextState)) {
			nextStateCounts.put(nextState, 1);
			numStates++;
		}

		nextStateCounts.put(nextState, nextStateCounts.get(nextState) + 1);
		numStates++;
	}
	
	public String toString() {
		return "State(" + name + ")"; 
	}

	public double wordEmittingProbability(String word) {
		double wordCount;
		if (emittedWordCounts.containsKey(word))
			wordCount = emittedWordCounts.get(word);
		else
			wordCount = 1.0;
		
		//System.out.println(name + " => " + word + ", " + result + "/" + numWords);
		return wordCount / numWords;
	}

	public double nextStateProbability(String state) {
		double result;
		if (nextStateCounts.containsKey(state))
			result = nextStateCounts.get(state);
		else
			result = 1.0;
		
		//System.out.println(name + " => " + State + ", " + result + "/" + numStates);
		return result / numStates;
	}
}

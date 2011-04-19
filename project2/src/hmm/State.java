package hmm;

import java.util.HashMap;

/**
 * represents a state of a HMM
 * @author ruedi
 *
 */
public class State {
	private HashMap<String, Integer> emittedWordCounts;
	private HashMap<String, Integer> nextTagCounts;
	
	private int numWords = 0;
	private int numTags;
	
	String name;

	public State(String name_) {
		emittedWordCounts = new HashMap<String, Integer>();
		nextTagCounts = new HashMap<String, Integer>();
		
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

	// Add next tag, null is final tag
	public void addStateTransitionObservation(String nextState) {
		// Laplace, renormalization
		if (!nextTagCounts.containsKey(nextState)) {
			nextTagCounts.put(nextState, 1);
			numTags++;
		}

		nextTagCounts.put(nextState, nextTagCounts.get(nextState) + 1);
		numTags++;
	}
	
	public String toString() {
		return "Tag(" + name + ")"; 
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

	public double nextTagProbability(String tag) {
		double result;
		if (nextTagCounts.containsKey(tag))
			result = nextTagCounts.get(tag);
		else
			result = 1.0;
		
		//System.out.println(name + " => " + tag + ", " + result + "/" + numTags);
		return result / numTags;
	}
}

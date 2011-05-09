package hmm;

import java.util.HashMap;

/**
 * represents a state of a HMM
 * @author ruedi
 *
 */
public class CountedState extends State<CountedState>{
	private final HashMap<Word, Integer> emittedWordCounts = new HashMap<Word, Integer>();
	private final HashMap<CountedState, Integer> nextStateCounts = new HashMap<CountedState, Integer>();
	StateCollection<CountedState> stateCollection;
	public CountedState(String name_, StateCollection _stateCollection) {
		name = name_;
		stateCollection=_stateCollection;
	}

	public void addWordEmissionObservation(Word word) {
		if (!emittedWordCounts.containsKey(word)) {
			// the word has been seen once
			emittedWordCounts.put(word, 1);
		}
		else{
			// increase the number of times the word has been seen
			emittedWordCounts.put(word, emittedWordCounts.get(word) + 1);
		}
	}

	// Add next State, null is final State
	public void addStateTransitionObservation(CountedState nextState) {
		if (!nextStateCounts.containsKey(nextState)) {
			// the state has been seen once
			nextStateCounts.put(nextState, 1);
		}
		else{
			// inclrease the nuber of times the state has been seen
			nextStateCounts.put(nextState, nextStateCounts.get(nextState) + 1);
		}
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
		// laplacian normalization (each word has been seen at least once)
		int result=1;
		
		// add the number of words acutally seen
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

	public int nextStateCount(CountedState state){
		// laplacian normalization, each state has been seen at least once
		int result=1;
		
		// add the true state count
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
		for (CountedState s: stateCollection.states.values()){
			result+=nextStateCount(s);
		}

		if (stateCollection.isFrozen()) totalNextStateCountCache=result;
		return result;
	}

	@Override
	public double wordEmittingProbability(Word word) {
		return (double) wordCount(word)/(double)totalWordCount();
	}

	@Override
	public double nextStateProbability(CountedState state) {
		return (double) nextStateCount(state)/(double)totalNextStateCount();
	}
}

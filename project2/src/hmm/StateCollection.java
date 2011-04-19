package hmm;

import java.util.ArrayList;
import java.util.HashMap;

public class StateCollection {
	
	HashMap<String, State> states;
	
	public State startTag() {
		return states.get("");
	}
	
	public StateCollection() {
		states = new HashMap<String, State>();
	}

	public void addStateTansitionObservation(String word, String stateString, String previousStateString) {
		State previousState;
		State state;
		
		// load previous state
		if (!states.containsKey(previousStateString)) {
			previousState = new State(previousStateString);
			states.put(previousStateString, previousState);
		} else {
			previousState = states.get(previousStateString);
		}
		
		// load current state
		if (!states.containsKey(stateString)) {
			state = new State(stateString);
			states.put(stateString, state);
		} else {
			state = states.get(stateString);
		}
		
		state.addWordEmissionObservation(word);
		previousState.addStateTransitionObservation(state.name);
	}

	public void addFinalStateTransitionObservation(String previousState) {
		states.get(previousState).addStateTransitionObservation(null);
	}

	/**
	 * Calculate the probability of a sentence and a tag sequence
	 * @param sentence word/tag pairs which make up the sentence
	 * @return
	 */
	public double calculateProbabilityofSentenceWithTags(ArrayList<String> sentence) {
		double probability = 1;
		String old_tag = "";
		
		for (String wordPair : sentence) {
			String[] splitting = wordPair.split("/");
			String word = splitting[0];
			String tag = splitting[1];

			// Multiply with tag-to-tag probability
			probability *= states.get(old_tag).nextTagProbability(tag);
			// Multiply with tag-to-word probability
			probability *= states.get(tag).wordEmittingProbability(word);
			
			old_tag = tag;
		}
		
		// Multiply with final-tag probability
		probability *= states.get(old_tag).nextTagProbability(null);
		return probability;
	}
}

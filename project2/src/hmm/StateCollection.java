package hmm;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class StateCollection {
	
	public HashMap<String, State> states;
	
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
	public double calculateProbabilityofSentenceWithStates(
			ArrayList<String> sentence) {
		double probability = 1;
		String old_tag = "";
		
		for (String wordPair : sentence) {
			String[] splitting = wordPair.split("/");
			String word = splitting[0];
			String tag = splitting[1];

			// Multiply with tag-to-tag probability
			probability *= states.get(old_tag).nextStateProbability(tag);
			// Multiply with tag-to-word probability
			probability *= states.get(tag).wordEmittingProbability(word);
			
			old_tag = tag;
		}
		
		// Multiply with final-tag probability
		probability *= states.get(old_tag).nextStateProbability(null);
		return probability;
	}
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(String.format("\t"));
		for (State column: states.values()){
			builder.append(String.format("%s\t",column.getDisplayName()));
		}
		builder.append(String.format("\n"));
		
		for (State row: states.values()){
			builder.append(String.format("%s\t",row.getDisplayName()));
			for (State column: states.values()){
				builder.append(String.format("%.2f\t",row.nextStateProbability(column.getDisplayName())));
			}
			builder.append(String.format("\n"));
		}
		return builder.toString();
	}
}

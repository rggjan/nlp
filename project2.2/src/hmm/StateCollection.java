package hmm;

import java.util.ArrayList;
import java.util.HashMap;

public class StateCollection {
	
	private boolean isFrozen;
	public HashMap<String, State> states=new HashMap<String, State>();
	public HashMap<String, Word> words=new HashMap<String, Word>();
	
	private State getStateTraining(String s){
		if (s==null || s.equals(""))
			throw new Error("invalid state name");
		
		// return existing state if available
		if (states.containsKey(s)){
			return states.get(s);
		}
		
		// create new state
		State result=new State(s,this);
		states.put(s,result);
		return result;
	}
	
	public State getState(String s){
		// return existing state if available
		if (states.containsKey(s)){
			return states.get(s);
		}
		
		return unknownState();
	}
	
	private Word getWordTraining(String s){
		// return existing word if available
		if (words.containsKey(s)){
			return words.get(s);
		}
		
		// create new word
		Word result=new Word(s);
		words.put(s,result);
		return result;
	}
	
	
	public Word getWord(String s){
		// return existing word if available
		if (words.containsKey(s)){
			return words.get(s);
		}
		
		return unknownWord();
	}
	
	public State startState() {
		return getStateTraining("<start>");
	}
	
	public State endState() {
		return getStateTraining("<end>");
	}
	
	public State unknownState() {
		return getStateTraining("<unk>");
	}
	
	public Word unknownWord(){
		return getWordTraining("<unk>");
	}
	
	public StateCollection() {
		// crate start and end state
		startState();
		endState();
		unknownState();
	}

	public void addStateTansitionObservation(String wordString, String stateString, String previousStateString) {
		if (isFrozen()) throw new Error();
		State previousState;
		State state;
		Word word;
		
		// load states
		previousState = getStateTraining(previousStateString);
		state = getStateTraining(stateString);
		
		// load word
		word=getWordTraining(wordString);
		
		state.addWordEmissionObservation(word);
		previousState.addStateTransitionObservation(state);
	}

	public void addFinalStateTransitionObservation(String previousState) {
		if (isFrozen()) throw new Error();
		getStateTraining(previousState).addStateTransitionObservation(endState());
	}

	/**
	 * Calculate the probability of a sentence and a tag sequence
	 * @param sentence word/tag pairs which make up the sentence
	 * @return
	 */
	public double calculateProbabilityofSentenceWithStates(ArrayList<String> sentence) {
		double probability = 1;
		State lastState = startState();
		
		for (String wordPair : sentence) {
			String[] splitting = wordPair.split("/");
			String wordString = splitting[0];
			String stateString = splitting[1];

			Word word=getWord(wordString);
			State state=getState(stateString);
			
			// Multiply with tag-to-tag probability
			probability *= lastState.nextStateProbability(state);
			// Multiply with tag-to-word probability
			probability *= state.wordEmittingProbability(word);
			
			lastState = state;
		}
		
		// Multiply with final-tag probability
		probability *= lastState.nextStateProbability(unknownState());
		return probability;
	}
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		
		// print transition matrix
		builder.append(String.format("\t"));
		for (State column: states.values()){
			builder.append(String.format("%s\t",column.name));
		}
		builder.append(String.format("\n"));
		
		for (State row: states.values()){
			builder.append(String.format("%s\t",row.name));
			for (State column: states.values()){
				builder.append(String.format("%.2f\t",row.nextStateProbability(column)));
			}
			builder.append(String.format("\n"));
		}
		builder.append(String.format("\n"));
		
		// print emission matrix
		builder.append(String.format("\t"));
		for (Word column: words.values()){
			builder.append(String.format("%s\t",column.name));
		}
		builder.append(String.format("\n"));
		
		for (State row: states.values()){
			builder.append(String.format("%s\t",row.name));
			for (Word column: words.values()){
				builder.append(String.format("%.2f\t",row.wordEmittingProbability(column)));
			}
			builder.append(String.format("\n"));
		}
		
		return builder.toString();
	}

	public void freeze() {
		this.isFrozen = true;
	}

	public boolean isFrozen() {
		return isFrozen;
	}
}

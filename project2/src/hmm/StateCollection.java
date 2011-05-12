package hmm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class StateCollection<T extends State> {
	private boolean isFrozen;
	public HashMap<String, T> states = new HashMap<String, T>();
	public HashMap<String, Word> words = new HashMap<String, Word>();

	public StateCollection(){
		// crate start and end state
		startState();
		endState();
		unknownState();
	}

	public Collection<T> getStates() {
		return states.values();
	}

	protected abstract T createState(String s);

	public T getStateTraining(String s) {
		if (s==null || s.equals(""))
			throw new Error("invalid state name");

		// return existing state if available
		if (states.containsKey(s)){
			return states.get(s);
		}

		// create new state
		T result=createState(s);
		states.put(s,result);
		return result;
	}

	public T getState(String s) {
		// return existing state if available
		if (states.containsKey(s)){
			return states.get(s);
		}

		return unknownState();
	}

	public Word getWordTraining(String s) {
		// return existing word if available
		if (words.containsKey(s)){
			return words.get(s);
		}

		// create new word
		Word result=new Word(s);
		words.put(s,result);
		return result;
	}

	public Word getWord(String s) {
		// return existing word if available
		if (words.containsKey(s)){
			return words.get(s);
		}

		return unknownWord();
	}

	public T startState() {
		return getStateTraining("<start>");
	}

	public T endState() {
		return getStateTraining("<end>");
	}

	public T unknownState() {
		return getStateTraining("<unk>");
	}

	public Word unknownWord() {
		return getWordTraining("<unk>");
	}

	/**
	 * Calculate the probability of a sentence and a tag sequence
	 * @param sentence word/tag pairs which make up the sentence
	 * @return
	 */
	public BigDouble calculateProbabilityofSentenceWithStates(ArrayList<String> sentence) {
		BigDouble probability = BigDouble.ONE;
		T lastState = startState();

		for (String wordPair : sentence) {
			String[] splitting = wordPair.split("/");
			String wordString = splitting[0];
			String stateString = splitting[1];

			Word word=getWord(wordString);
			T state=getState(stateString);
	
			// Multiply with tag-to-tag probability
			probability=probability.multiply(lastState.nextStateProbability(state));
			// Multiply with tag-to-word probability
			probability=probability.multiply(state.wordEmittingProbability(word));

			lastState = state;
		}

		// Multiply with final-tag probability
		probability=probability.multiply(lastState.nextStateProbability(unknownState()));
		return probability;
	}

	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();

		// print transition matrix

		// print top row
		builder.append(String.format("\t"));
		for (T column: states.values()){
			builder.append(String.format("%s\t",column.name));
		}
		builder.append(String.format("\n"));

		for (T row: states.values()){
			// print row name
			builder.append(String.format("%s\t",row.name));
			
			// output all values
			for (T column: states.values()){
				builder.append(String.format("%.2f\t",row.nextStateProbability(column).doubleValue()));
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
	
		for (T row: states.values()){
			builder.append(String.format("%s\t",row.name));
			for (Word column: words.values()){
				builder.append(String.format("%.2f\t",row.wordEmittingProbability(column).doubleValue()));
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

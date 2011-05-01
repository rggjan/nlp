package hmm;

import java.util.List;

public class ForwardAlgorithm {
	private StateCollection stateCollection;
	private List<String> outputSencence;
	
	private ForwardAlgorithm(final StateCollection hmm, final List<String> output) {
		stateCollection = hmm;
		outputSencence = output;
	}
	
	// Get the probability for a certain element at a certain index
	public double getV(int index, State qj) {
		assert(index >= 1);
		
		if (index == 1) {
			double probability;
			probability = stateCollection.startState().nextStateProbability(qj);
			probability *= qj.wordEmittingProbability(new Word(outputSencence.get(0)));
			return probability;			
		}
		
		double max = 0;
		//double argmax = 1;
		
		for (State qi : stateCollection.states.values()) {
			double value;
			value = getV(index - 1, qj);
			value *= qi.nextStateProbability(qj);
			value *= qj.wordEmittingProbability(new Word(outputSencence.get(index - 1)));
			if (value > max) {
				max = value;
			}
		}
		
		return max;
	}
}

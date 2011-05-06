package hmm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForwardBackwardAlgorithm {
	private final StateCollection stateCollection;
	private final List<String> outputSencence;
	private final ArrayList< HashMap<State, Double> > cache;
	private final Boolean isForwardAlgorithm;
	
	public ForwardBackwardAlgorithm(final StateCollection hmm, final List<String> output, Boolean isForward) {
		stateCollection = hmm;
		outputSencence = output;
		
		cache = new ArrayList< HashMap<State, Double> >();
		isForwardAlgorithm = isForward;
	}

	private void addToCache(int index, State qj, double probability) {
		HashMap<State, Double> stateCache; 
		
		stateCache = cache.get(index);
		if (stateCache == null) {
			stateCache = new HashMap<State, Double>();
			cache.set(index, stateCache);
		}
			
		stateCache.put(qj, probability);
	}
	
	private double checkCache(int index, State qj) {
		HashMap<State, Double> stateCache; 
		
		stateCache = cache.get(index);
		if (stateCache != null)
			return stateCache.get(qj);
		
		return -1;
	}
	
	// Get the probability for a certain element at a certain index
	public double getAlphaBeta(int index, State qj) {
		assert(index >= 1);
		assert(index <= outputSencence.size()); 
		
		double cachedValue = checkCache(index, qj); 
		if (cachedValue != -1)
			return cachedValue;
			
		if (isForwardAlgorithm) {
			if (index == 1) {
				double probability;
				probability = stateCollection.startState().nextStateProbability(qj);
				probability *= qj.wordEmittingProbability(new Word(outputSencence.get(0)));
				// TODO probably should not create "new" word
				addToCache(index, qj, probability);
				return probability;			
			}
		} else {
			if (index == outputSencence.size()) {
				double probability;
				probability = qj.nextStateProbability(stateCollection.endState());
				addToCache(index, qj, probability);
				return probability;			
			}
		}
		
		double sum = 0;
		
		for (State qi : stateCollection.states.values()) {
			double value;
			if (isForwardAlgorithm) {
				value = getAlphaBeta(index - 1, qj);
				value *= qi.nextStateProbability(qj);
				value *= qj.wordEmittingProbability(new Word(outputSencence.get(index - 1)));
				// TODO probably should not create "new" word
			} else {
				value = getAlphaBeta(index + 1, qi);
				value *= qj.nextStateProbability(qi);
				value *= qi.wordEmittingProbability(new Word(outputSencence.get(index - 1)));
				// TODO probably should not create "new" word
			}
			sum += value;
		}
		
		addToCache(index, qj, sum);
		return sum;
	}
}

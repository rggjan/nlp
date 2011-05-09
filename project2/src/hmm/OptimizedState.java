package hmm;

import java.util.HashMap;
import java.util.Map.Entry;

public class OptimizedState extends State<OptimizedState>{

	private HashMap<OptimizedState, Double> nextStateProbabilityMap=new HashMap<OptimizedState, Double>();
	private HashMap<Word, Double> wordEmissionProbablilityMap=new HashMap<Word, Double>();
	
	public OptimizedState(String s) {
		name=s;
	}

	public void setNextStateProbability(OptimizedState state, double d){
		nextStateProbabilityMap.put(state, d);
	}
	
	public void setWordEmissionProbability(Word word, double d){
		wordEmissionProbablilityMap.put(word,d);
	}
	
	@Override
	public double nextStateProbability(OptimizedState state) {
		if (nextStateProbabilityMap.containsKey(state)){
			return nextStateProbabilityMap.get(state);
		}
		return 0;
	}

	@Override
	public double wordEmittingProbability(Word word) {
		if (wordEmissionProbablilityMap.containsKey(word)){
			return wordEmissionProbablilityMap.get(word);
		}
		
		return 0;
	}

	public void normalize() {
		double sum=0;
		
		// normalize transition probabilities
		for (Double d: nextStateProbabilityMap.values()){
			sum+=d;
		}
		
		if (sum==0) sum=1;
		
		for (Entry<OptimizedState, Double> e: nextStateProbabilityMap.entrySet()){
			e.setValue(e.getValue()/sum);
		}
		
		// normalize word emission probabilities
		sum=0;
		for (Double d: wordEmissionProbablilityMap.values())
			sum+=d;
		
		if (sum==0) sum=1;
		
		for (Entry<Word, Double> e: wordEmissionProbablilityMap.entrySet()){
			e.setValue(e.getValue()/sum);
		}
		
	}
}

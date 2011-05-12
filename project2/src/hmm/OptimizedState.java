package hmm;

import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map.Entry;

public class OptimizedState extends State<OptimizedState>{

	private MathContext mc=new MathContext(200,RoundingMode.HALF_EVEN);
	
	private HashMap<OptimizedState, BigDouble> nextStateProbabilityMap=new HashMap<OptimizedState, BigDouble>();
	private HashMap<Word, BigDouble> wordEmissionProbablilityMap=new HashMap<Word, BigDouble>();
	
	public OptimizedState(String s) {
		name=s;
	}

	public void setNextStateProbability(OptimizedState state, BigDouble d){
		nextStateProbabilityMap.put(state, d);
	}
	
	public void setWordEmissionProbability(Word word, BigDouble d){
		wordEmissionProbablilityMap.put(word,d);
	}
	
	@Override
	public BigDouble nextStateProbability(OptimizedState state) {
		if (nextStateProbabilityMap.containsKey(state)){
			return nextStateProbabilityMap.get(state);
		}
		return BigDouble.ZERO;
	}

	@Override
	public BigDouble wordEmittingProbability(Word word) {
		if (wordEmissionProbablilityMap.containsKey(word)){
			return wordEmissionProbablilityMap.get(word);
		}
		
		return BigDouble.ZERO;
	}

	public void normalize() {
		BigDouble sum=BigDouble.ZERO;
		
		// normalize transition probabilities
		for (BigDouble d: nextStateProbabilityMap.values()){
			sum=sum.add(d);
		}
		
		if (sum.compareTo(BigDouble.ZERO)==0) sum=BigDouble.ONE;
		
		for (Entry<OptimizedState, BigDouble> e: nextStateProbabilityMap.entrySet()){
			e.setValue(e.getValue().divide(sum));
		}
		
		// normalize word emission probabilities
		sum=BigDouble.ZERO;
		for (BigDouble d: wordEmissionProbablilityMap.values())
			sum=sum.add(d);
		
		if (sum.compareTo(BigDouble.ZERO)==0) sum=BigDouble.ONE;
		
		for (Entry<Word, BigDouble> e: wordEmissionProbablilityMap.entrySet()){
			e.setValue(e.getValue().divide(sum));
		}
		
	}
}

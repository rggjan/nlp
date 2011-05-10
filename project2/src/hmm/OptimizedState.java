package hmm;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map.Entry;

public class OptimizedState extends State<OptimizedState>{

	private MathContext mc=new MathContext(200,RoundingMode.HALF_EVEN);
	
	private HashMap<OptimizedState, BigDecimal> nextStateProbabilityMap=new HashMap<OptimizedState, BigDecimal>();
	private HashMap<Word, BigDecimal> wordEmissionProbablilityMap=new HashMap<Word, BigDecimal>();
	
	public OptimizedState(String s) {
		name=s;
	}

	public void setNextStateProbability(OptimizedState state, BigDecimal d){
		nextStateProbabilityMap.put(state, d);
	}
	
	public void setWordEmissionProbability(Word word, BigDecimal d){
		wordEmissionProbablilityMap.put(word,d);
	}
	
	@Override
	public BigDecimal nextStateProbability(OptimizedState state) {
		if (nextStateProbabilityMap.containsKey(state)){
			return nextStateProbabilityMap.get(state);
		}
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal wordEmittingProbability(Word word) {
		if (wordEmissionProbablilityMap.containsKey(word)){
			return wordEmissionProbablilityMap.get(word);
		}
		
		return BigDecimal.ZERO;
	}

	public void normalize() {
		BigDecimal sum=BigDecimal.ZERO;
		
		// normalize transition probabilities
		for (BigDecimal d: nextStateProbabilityMap.values()){
			sum=sum.add(d);
		}
		
		if (sum.compareTo(BigDecimal.ZERO)==0) sum=BigDecimal.ONE;
		
		for (Entry<OptimizedState, BigDecimal> e: nextStateProbabilityMap.entrySet()){
			e.setValue(e.getValue().divide(sum,mc));
		}
		
		// normalize word emission probabilities
		sum=BigDecimal.ZERO;
		for (BigDecimal d: wordEmissionProbablilityMap.values())
			sum=sum.add(d);
		
		if (sum.compareTo(BigDecimal.ZERO)==0) sum=BigDecimal.ONE;
		
		for (Entry<Word, BigDecimal> e: wordEmissionProbablilityMap.entrySet()){
			e.setValue(e.getValue().divide(sum,mc));
		}
		
	}
}

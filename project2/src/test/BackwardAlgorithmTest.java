package test;

import static junit.framework.Assert.assertEquals;
import hmm.BackwardAlgorithm;
import hmm.BigDouble;
import hmm.ForwardAlgorithm;
import hmm.OptimizedState;
import hmm.OptimizedStateCollection;
import hmm.Word;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BackwardAlgorithmTest {
	@Test
	public void TestSimple(){
		OptimizedStateCollection hmm=new OptimizedStateCollection();
		OptimizedState foo=hmm.getStateTraining("foo");
		foo.setNextStateProbability(hmm.endState(), BigDouble.valueOf(1));
		hmm.startState().setNextStateProbability(foo, BigDouble.valueOf(1));
		
		Word bar=hmm.getWordTraining("bar");
		foo.setWordEmissionProbability(bar, BigDouble.valueOf(1));
		
		List<Word> sentence=new ArrayList<Word>();
		sentence.add(bar);
		
		BackwardAlgorithm<OptimizedStateCollection, OptimizedState> beta
			=new BackwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,beta.get(1,hmm.endState()).doubleValue());
		assertEquals(1.0,beta.get(0,foo).doubleValue());
		assertEquals(1.0,beta.getFinalProbability().doubleValue());
	}
	
	@Test
	public void TestMedium(){
		OptimizedStateCollection hmm=new OptimizedStateCollection();
		OptimizedState state1=hmm.getStateTraining("state1");
		OptimizedState state2=hmm.getStateTraining("state2");
		
		state1.setNextStateProbability(hmm.endState(), BigDouble.valueOf(0.1));
		state1.setNextStateProbability(state2, BigDouble.valueOf(0.9));
		state2.setNextStateProbability(hmm.endState(), BigDouble.valueOf(1));
		
		hmm.startState().setNextStateProbability(state1, BigDouble.valueOf(1));
		
		Word word1=hmm.getWordTraining("word1");
		Word word2=hmm.getWordTraining("word2");
		
		state1.setWordEmissionProbability(word1, BigDouble.valueOf(0.5));
		state1.setWordEmissionProbability(word2, BigDouble.valueOf(0.5));
		state2.setWordEmissionProbability(word1, BigDouble.valueOf(1));
		
		List<Word> sentence=new ArrayList<Word>();
		sentence.add(word1);
		
		BackwardAlgorithm<OptimizedStateCollection, OptimizedState> beta
			=new BackwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,beta.get(1,hmm.endState()).doubleValue());
		assertEquals(1.0,beta.get(0,state1).doubleValue());
		assertEquals(0.5,beta.getFinalProbability().doubleValue());
		
		sentence.add(word1);
		beta=new BackwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,beta.get(2,hmm.endState()).doubleValue());
		assertEquals(1.0,beta.get(1,state2).doubleValue());
		assertEquals(1.0,beta.get(0,state1).doubleValue());
		
		assertEquals(0.5,beta.getFinalProbability().doubleValue());
	}
}

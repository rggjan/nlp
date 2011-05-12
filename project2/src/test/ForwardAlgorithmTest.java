package test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

import hmm.BackwardAlgorithm;
import hmm.BigDouble;
import hmm.ForwardAlgorithm;
import hmm.OptimizedState;
import hmm.OptimizedStateCollection;
import hmm.Word;

import org.junit.Test;

public class ForwardAlgorithmTest {

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
		
		ForwardAlgorithm<OptimizedStateCollection, OptimizedState> alpha
			=new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,alpha.get(-1,hmm.startState()).doubleValue(),1e-5);
		assertEquals(1.0,alpha.get(0,foo).doubleValue());
		assertEquals(1.0,alpha.getFinalProbability().doubleValue());
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
		
		ForwardAlgorithm<OptimizedStateCollection, OptimizedState> alpha
			=new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,alpha.get(-1,hmm.startState()).doubleValue());
		assertEquals(0.5,alpha.get(0,state1).doubleValue());
		assertEquals(0.5*0.1,alpha.getFinalProbability().doubleValue());
		
		sentence.add(word1);
		alpha=new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,alpha.get(-1,hmm.startState()).doubleValue());
		assertEquals(0.5*0.9,alpha.get(1,state2).doubleValue());
		assertEquals(0.5,alpha.get(0,state1).doubleValue());
		
		assertEquals(0.9*0.5,alpha.getFinalProbability().doubleValue());
	}
	
	@Test
	public void TestHard(){
		OptimizedStateCollection hmm=new OptimizedStateCollection();
		OptimizedState state1=hmm.getStateTraining("state1");
		OptimizedState state2=hmm.getStateTraining("state2");
		
		state1.setNextStateProbability(hmm.endState(), BigDouble.valueOf(0.1));
		state1.setNextStateProbability(state2, BigDouble.valueOf(0.9));
		
		state2.setNextStateProbability(hmm.endState(), BigDouble.valueOf(0.7));
		state2.setNextStateProbability(state1, BigDouble.valueOf(0.3));
		
		hmm.startState().setNextStateProbability(state1, BigDouble.valueOf(0.4));
		hmm.startState().setNextStateProbability(state2, BigDouble.valueOf(0.6));
		
		Word word1=hmm.getWordTraining("word1");
		Word word2=hmm.getWordTraining("word2");
		
		state1.setWordEmissionProbability(word1, BigDouble.valueOf(0.5));
		state1.setWordEmissionProbability(word2, BigDouble.valueOf(0.5));
		state2.setWordEmissionProbability(word1, BigDouble.valueOf(0.2));
		state2.setWordEmissionProbability(word2, BigDouble.valueOf(0.8));
		
		List<Word> sentence=new ArrayList<Word>();
		sentence.add(word1);
		
		ForwardAlgorithm<OptimizedStateCollection, OptimizedState> alpha
			=new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,alpha.get(-1,hmm.startState()).doubleValue());
		assertEquals(0.5*0.4,alpha.get(0,state1).doubleValue());
		assertEquals(0.5*0.4*0.1+0.6*0.2*0.7,alpha.getFinalProbability().doubleValue());
		
		sentence.add(word1);
		alpha=new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,sentence);
		
		assertEquals(1.0,alpha.get(-1,hmm.startState()).doubleValue());
		assertEquals(0.4*0.5*0.9*0.2,alpha.get(1,state2).doubleValue());
		assertEquals(0.5*0.4,alpha.get(0,state1).doubleValue());
		
		assertEquals(0.4*0.5*0.9*0.2*0.7+0.6*0.2*0.3*0.5*0.1,alpha.getFinalProbability().doubleValue());
	}
}

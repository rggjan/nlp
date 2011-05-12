package test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

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
}

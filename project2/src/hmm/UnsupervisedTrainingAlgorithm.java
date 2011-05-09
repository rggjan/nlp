package hmm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class UnsupervisedTrainingAlgorithm {
	static Random random=new Random(1);
	
	public static OptimizedStateCollection train(ArrayList<ArrayList<String>> trainingSentenceStrings, int stateCount){
		OptimizedStateCollection hmm=new OptimizedStateCollection();
		
		// initialize states
		for (int i=0; i<stateCount; i++){
			hmm.getStateTraining("s"+i);
		}
		
		// setup random state transistion probabilities
		for (OptimizedState a: hmm.getStates())
			for (OptimizedState b: hmm.getStates())
				a.setNextStateProbability(b, random.nextDouble());
		
		ArrayList<ArrayList<Word>> trainingSentences=new ArrayList<ArrayList<Word>>();
		
		// read all words
		for (List<String> list: trainingSentenceStrings){
			ArrayList<Word> sentence=new ArrayList<Word>();
			for (String word: list){
				sentence.add(hmm.getWordTraining(word));
			}
			trainingSentences.add(sentence);
		}
		
		// setup random word emission probabilities
		for (OptimizedState a: hmm.getStates())
			for (Word w: hmm.words.values())
				a.setWordEmissionProbability(w, random.nextDouble());
		
		// normalize all probabilitis
		for (OptimizedState a: hmm.getStates())
			a.normalize();
		
		double oldProbability=-1;
		double probability=0;
		
		// optimization loop
		do{
			// create forward and backward algorithms
			ArrayList<ForwardAlgorithm<OptimizedStateCollection, OptimizedState>>
				alphas=new ArrayList<ForwardAlgorithm<OptimizedStateCollection,OptimizedState>>();
			
			ArrayList<BackwardAlgorithm<OptimizedStateCollection, OptimizedState>>
				betas=new ArrayList<BackwardAlgorithm<OptimizedStateCollection,OptimizedState>>();
			
			for (List<Word> list: trainingSentences){
				alphas.add(new ForwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm,list));
				betas.add(new BackwardAlgorithm<OptimizedStateCollection, OptimizedState>(hmm, list));
			}
			
			// calculate the probability of the input under the current HMM
			probability=1;
			{
				int i=0;
				for (List<Word> list: trainingSentences){
					probability*=alphas.get(i).getFinalProbability();
				}
			}
			
			System.out.println(probability);
			// optimize while the probability of the output increases by at least 10 percent
			if (oldProbability*1.0>probability) return hmm;
			
			oldProbability=probability;
			
			// update probabilities of a new HMM (M-step)
			OptimizedStateCollection newHmm=copyStateCollection(hmm);
			
			// set transition probabilities
			for (OptimizedState a: hmm.getStates()){
				OptimizedState newA=newHmm.getState(a.name);
				double denominator=0;
				for (OptimizedState b: hmm.getStates()){
					
					OptimizedState newB=newHmm.getState(b.name);
					
					// calculate the numerator
					double numerator=0;
					int i=0;
					for (List<Word> sentence: trainingSentences){
						for( int t=0; t<sentence.size()-1; t++){
							double d=xi(t,sentence,a,b,alphas.get(i),betas.get(i),hmm);
							numerator+=d;
							denominator+=d;
						}
						i++;
					}
					newA.setNextStateProbability(newB, numerator);
				}
				
				// normalize with the denominator
				// this can only be done after iteration over all b's above
				if (denominator==0) denominator=1;
				for (OptimizedState b: hmm.getStates()){
					OptimizedState newB=newHmm.getState(b.name);
					newA.setNextStateProbability(newB,
							newA.nextStateProbability(newB)/denominator);
				}
			}
			
			// set emission probabilities
			for (OptimizedState a: hmm.getStates()){
				OptimizedState newA=newHmm.getState(a.name);
				
				// calculate the denominator
				double denominator=0;
				{
					int i=0;
					for (List<Word> sentence: trainingSentences){
						int t=0;
						for( Word w: sentence){
							denominator+=gamma(t,w,a,alphas.get(i),betas.get(i));
							t++;
						}
						i++;
					}
				}
				if (denominator==0) denominator=1;
				
				// set the probabilities for all words
				for (Word word: hmm.words.values()){
					double numerator=0;
					int i=0;
					for (List<Word> sentence: trainingSentences){
						int t=0;
						for( Word w: sentence){
							if (w==word)
								numerator+=gamma(t,word,a,alphas.get(i),betas.get(i));
							t++;
						}
						i++;
					}
					newA.setWordEmissionProbability(word, numerator/denominator);
				}
				
			}
			hmm=newHmm;
		} while (true); // return statement is located above
		
		// never reached
	}
	
	private static double xi(int t, List<Word> sentence, OptimizedState a, OptimizedState b, ForwardAlgorithm<OptimizedStateCollection,OptimizedState> alpha, BackwardAlgorithm<OptimizedStateCollection,OptimizedState> beta, OptimizedStateCollection hmm){
		return 
			alpha.get(t, a)
			*a.nextStateProbability(b)
			*b.wordEmittingProbability(sentence.get(t+1))
			*beta.get(t+1,b);
	}
	
	private static double gamma(int t,Word w, OptimizedState a, ForwardAlgorithm<OptimizedStateCollection,OptimizedState> alpha, BackwardAlgorithm<OptimizedStateCollection,OptimizedState> beta){
		return
			alpha.get(t,a)
			*beta.get(t,a)
			/alpha.getFinalProbability();
			
	}
	private static OptimizedStateCollection copyStateCollection(OptimizedStateCollection other){
		OptimizedStateCollection hmm=new OptimizedStateCollection();
		
		// clear data
		hmm.words.clear();
		hmm.states.clear();
		
		// add states
		for (OptimizedState s: other.states.values()){
			hmm.getStateTraining(s.name);
		}
		
		// add words
		for (Word w: other.words.values()){
			hmm.words.put(w.name,w);
		}
		
		return hmm;
	}
}

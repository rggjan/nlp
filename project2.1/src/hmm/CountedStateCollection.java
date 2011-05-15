package hmm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

/**
 * A collection of states
 * 
 * @author ruedi
 *
 */
public class CountedStateCollection extends StateCollection<CountedState> {

	private Random random;

	public CountedStateCollection() {
	}

	

	/*public StateCollection reEstimateProbabilites(ArrayList<ArrayList<String>> trainingSentences) {
		StateCollection new_collection = new StateCollection();

		// Add states to list
		for (int i=0; i<states.size(); i++) {
			new_collection.getStateTraining("<state_" + i + ">");
		}

		// Add words to list
		new_collection.setWords(words);

		// Estimate new transition probabilities
		for (State qi : states.values()) {
			for (State qj : states.values()) {
				double ep_qi_qj = 0;
				for (ArrayList<String> sentence : trainingSentences) {
					ForwardBackwardAlgorithm forward_algorithm =
						new ForwardBackwardAlgorithm(new_collection, sentence, true);

					ForwardBackwardAlgorithm backward_algorithm =
						new ForwardBackwardAlgorithm(new_collection, sentence, false);

					double p_qi_qj_o = 0;

					for (int t = 0; t < sentence.size() - 1; t++) {
						double forward = forward_algorithm.getAlphaBeta(t, qi);
						double backward = backward_algorithm.getAlphaBeta(t+1, qj);

						p_qi_qj_o += forward *
						qi.nextStateProbability(qj) *
						qj.wordEmittingProbability(getWord(sentence.get(t+1))) *
						backward;
					}

					double p_o = forward_algorithm.getFinalProbability();
					double p_qi_qj_when_o = p_qi_qj_o / p_o;

					ep_qi_qj += p_qi_qj_when_o;
				}

				// COMMENT: The following is not needed, as we normalize automatically
				// in the get probability function!

				//double ep_qi = 0;
				//for (ArrayList<String> sentence : trainingSentences) {
				//	for (State qj_any : states.values()) {
				//		ep_qi +=
				//	}
				//}

				//new_collection.getState(qi.name).setStateTransitionObservation(
				//		new_collection.getState(qj.name), ep_qi_qj);
			}
		}

		for (State qi : states.values()) {
			for (Word word : words.values()) {
				double p_vk_given_qj = 0;

				for (ArrayList<String> sentence : trainingSentences) {
					ForwardBackwardAlgorithm forward_algorithm =
						new ForwardBackwardAlgorithm(new_collection, sentence, true);

					ForwardBackwardAlgorithm backward_algorithm =
						new ForwardBackwardAlgorithm(new_collection, sentence, false);

					double p_vk_qi_o = 0;

					for (int t=0; t<sentence.size(); t++) {
						if (sentence.get(t).equals(word.name)) {
							double forward = forward_algorithm.getAlphaBeta(t, qi);
							double backward = backward_algorithm.getAlphaBeta(t, qi);

							p_vk_qi_o += forward * backward;
						}
					}

					double p_o = forward_algorithm.getFinalProbability();
					double p_vk_qi_when_o = p_vk_qi_o / p_o;

					p_vk_given_qj += p_vk_qi_when_o;
				}


				// COMMENT: Normalization should be done automatically... REALLY?
				//new_collection.getState(qi.name).setWordEmissionObservations(
				//		word, p_vk_given_qj);
			}
		}

		return new_collection;
	}*/

	public void addStateTansitionObservation(String wordString, String stateString, String previousStateString) {
		if (isFrozen()) throw new Error();
		CountedState previousState;
		CountedState state;
		Word word;

		// load states
		previousState = getStateTraining(previousStateString);
		state = getStateTraining(stateString);

		// load word
		word=getWordTraining(wordString);

		state.addWordEmissionObservation(word);
		previousState.addStateTransitionObservation(state);
	}

	public void addFinalStateTransitionObservation(String previousState) {
		if (isFrozen()) throw new Error();
		getStateTraining(previousState).addStateTransitionObservation(endState());
	}



	@Override
	protected CountedState createState(String s) {
		return new CountedState(s,this);
	}



	public Word getWord(String s) {
		return null;
	}
}

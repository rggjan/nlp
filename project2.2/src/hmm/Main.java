package hmm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final boolean simple_texts = false;

		TextParser parser = new TextParser();

		// read training data
		System.out.println("Reading training texts...");
		if (simple_texts) {
			parser.readText("data/train.txt");
		} else {
			for (int i=1; i<2; i++)
				parser.readText("data/train_" + i + ".pos");
		}

		ArrayList<ArrayList<String>> trainingList = parser.getSentences();
		HashSet<String> trainingWords = new HashSet<String>();

		for (ArrayList<String> sentence : trainingList) {
			for (String word : sentence) {
				trainingWords.add(word);
			}
		}

		// Start training with 10 states
		final int stateCount = 10;

		System.out.println("Starting Training...");
		// long start=System.currentTimeMillis();
		OptimizedStateCollection hmm=UnsupervisedTrainingAlgorithm.train(parser.getSentences(), stateCount);
		// System.out.println(System.currentTimeMillis()-start);

		System.out
		.println("=====================================================");
		System.out
		.println("Finished training! (Less than 10 percent improvement)");
		System.out
		.println("=====================================================");
		System.out.println("Trained HMM with " + stateCount + " states:");

		// Print the HMM we got
		System.out.println(hmm);

		// read the test text
		parser = new TextParser();
		if (simple_texts) {
			parser.readText("data/test.txt");
		} else {
			parser.readText("data/train_1.pos");
		}
		ArrayList<ArrayList<String>> sentenceList = parser.getSentences();

		// iterate over the test text and print the best tagging,
		// along with the probability

		System.out.println("==========================================");
		System.out.println("Training sentences, real and trained tags:");
		System.out.println("==========================================");

		for (ArrayList<String> sentence : sentenceList) {
			ArrayList<String> notTagSentence = new ArrayList<String>();

			for (String word : sentence) {
				String[] splitting = word.split("/");
				String s = splitting[0];
				System.out.print(word + " ");
				notTagSentence.add(s);
			}
			System.out.println();

			Viterbi<OptimizedStateCollection, OptimizedState> viterbi = Viterbi
			.viterbi(hmm, notTagSentence);

			for (int i = 0; i < notTagSentence.size(); i++) {
				String goodTag = viterbi.getStates().get(i).name;
				System.out.printf("%s/%s ", notTagSentence.get(i), goodTag);
			}
			System.out.println();

			System.out.println("Probability: " + viterbi.getProbability());
			System.out.println("-----------------------------------");
		}
	}
}

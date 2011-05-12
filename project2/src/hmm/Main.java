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

		BigDouble n=BigDouble.valueOf(24.5,100000);
		BigDouble m=new BigDouble(-3);
		System.out.println(n.add(m));

		// exercise_1(simple_texts);
		exercise_2(simple_texts);
	}

	private static void exercise_2(final boolean simple_texts)
	throws IOException {
		TextParser parser = new TextParser();

		// read training data
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

		final int stateCount = 10;

		long start=System.currentTimeMillis();
		OptimizedStateCollection hmm=UnsupervisedTrainingAlgorithm.train(parser.getSentences(), stateCount);
		System.out.println(System.currentTimeMillis()-start);

		// Print the HMM we got
		System.out.println(hmm);

		// read the test text
		parser = new TextParser();
		if (simple_texts) {
			parser.readText("data/test.txt");
		} else {
			parser.readText("data/test_1.pos");
		}
		ArrayList<ArrayList<String>> sentenceList = parser.getSentences();

		// iterate over the test text and print the probabilities of
		// the test sentences
		/*
		 * for (ArrayList<String> sentence : sentenceList) {
		 * System.out.println("==================="); for (String word :
		 * sentence) { System.out.print(word + " "); } System.out.println();
		 * 
		 * System.out .println("=> " +
		 * hmm.calculateProbabilityofSentenceWithStates(sentence)); }
		 */
	}

	private static void exercise_1(final boolean simple_texts)
	throws IOException {
		TextParser parser = new TextParser();

		// read training data
		if (simple_texts) {
			parser.readText("data/train.txt");
		} else {
			for (int i=1; i<2; i++)
				parser.readText("data/train_" + i + ".pos");
		}

		// get the state collection ( trained HMM)
		CountedStateCollection collection = parser.getStateCollection();
		//System.out.print(collection);

		// read the test text
		parser = new TextParser();
		if (simple_texts) {
			parser.readText("data/test.txt");
		} else {
			parser.readText("data/test_1.pos");
		}
		ArrayList<ArrayList<String>> sentenceList = parser.getSentences();

		// iterate over the test text and print the probabilities of
		// the test sentences
		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			for (String word : sentence) {
				System.out.print(word + " ");
			}
			System.out.println();

			System.out.println("=> " + collection.calculateProbabilityofSentenceWithStates(sentence));
		}

		// iterate over the test text and print the best tagging,
		// along with the probability
		int globalCorrect = 0;
		int globalTotal = 0;

		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			ArrayList<String> notTagSentence=new ArrayList<String>();
			ArrayList<String> tagOnlySentence=new ArrayList<String>();

			for (String word : sentence) {
				String[] splitting = word.split("/");
				String s=splitting[0];
				System.out.print(word + " ");
				notTagSentence.add(s);
				tagOnlySentence.add(splitting[1]);
			}
			System.out.println();

			Viterbi<CountedStateCollection,CountedState> viterbi=Viterbi.viterbi(collection, notTagSentence);

			int numCorrect = 0;
			int numTotal = 0;

			for (int i=0; i<notTagSentence.size(); i++){
				String goodTag = viterbi.getStates().get(i).name;
				System.out.printf("%s/%s ",notTagSentence.get(i), goodTag);

				if (goodTag.equals(tagOnlySentence.get(i)))
					numCorrect++;

				numTotal++;
			}
			System.out.println();

			System.out.println("Probability: " + viterbi.getProbability());
			System.out.println("Correctness: " + numCorrect * 100.0 / numTotal + "%");
			globalCorrect += numCorrect;
			globalTotal += numTotal;
		}

		System.out.println("==========================================");
		System.out.println("==========================================");

		System.out.println("Total Correctness: " + globalCorrect * 100.0 / globalTotal + "%");
	}

}

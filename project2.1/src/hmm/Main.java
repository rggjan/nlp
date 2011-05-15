package hmm;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final boolean simple_texts = false;

		TextParser parser = new TextParser();

		// read training data
		if (simple_texts) {
			parser.readText("data/train.txt");
		} else {
			for (int i = 1; i < 51; i++)
				parser.readText("data/train_" + i + ".pos");
		}

		// get the state collection ( trained HMM)
		StateCollection collection = parser.getStateCollection();

		// read the test text
		parser = new TextParser();
		if (simple_texts) {
			parser.readText("data/test.txt");
		} else {
			parser.readText("data/test_1.pos");
		}
		ArrayList<ArrayList<String>> sentenceList = parser.getSentences();

		System.out.println("===========================");
		System.out.println("Probabilities of sentences:");
		System.out.println("===========================");

		// iterate over the test text and print the probabilities of
		// the test sentences
		for (ArrayList<String> sentence : sentenceList) {
			for (String word : sentence) {
				System.out.print(word + " ");
			}
			System.out.println();

			System.out.println("=> " + collection.calculateProbabilityofSentenceWithStates(sentence));

			System.out.println("===================");
		}

		// iterate over the test text and print the best tagging,
		// along with the probability
		int globalCorrect = 0;
		int globalTotal = 0;

		System.out.println();
		System.out
		.println("=======================================================");
		System.out
		.println("Guessed tags, probability and ground truth correctness:");
		System.out
		.println("=======================================================");

		for (ArrayList<String> sentence : sentenceList) {
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

			Viterbi viterbi=Viterbi.viterbi(collection, notTagSentence);

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

			System.out.println("===================");
		}

		System.out.println("==========================================");

		System.out.println("Total Correctness: " + globalCorrect * 100.0 / globalTotal + "%");
	}
}

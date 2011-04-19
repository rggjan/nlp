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
			for (int i=1; i<4; i++)
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
		
		// iterate ofer the test text and print the probatilities of
		// the test sentences
		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			for (String word : sentence) {
				System.out.print(word + " ");
			}
			System.out.println();

			System.out.println("=> " + collection.calculateProbabilityofSentenceWithTags(sentence));
		}
	}

}

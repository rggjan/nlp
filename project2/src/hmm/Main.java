package hmm;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final boolean simple_texts = true;		
		
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
		System.out.print(collection);
		
		// read the test text
		parser = new TextParser();
		if (simple_texts) {
			parser.readText("data/test.txt");
		} else {
			parser.readText("data/test_1.pos");
		}
		ArrayList<ArrayList<String>> sentenceList = parser.getSentences();
		
		// iterate over the test text and print the probatilities of
		// the test sentences
		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			for (String word : sentence) {
				System.out.print(word + " ");
			}
			System.out.println();

			System.out.println("=> " + collection.calculateProbabilityofSentenceWithTags(sentence));
		}
		
		// iterate over the test text and print the best tagging,
		// along with the probability
		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			ArrayList<String> notTagSentence=new ArrayList<String>();
			
			for (String word : sentence) {
				String s=word.split("/")[0];
				System.out.print(s + " ");
				notTagSentence.add(s);
			}
			System.out.println();

			Viterbi viterbi=Viterbi.viterbi(collection, notTagSentence);
			
			for (int i=0; i<notTagSentence.size(); i++){
				System.out.printf("%s/%s ",notTagSentence.get(i),viterbi.getStates().get(i));
			}
			System.out.println();

			System.out.println("=> " + viterbi.getProbability());
		}
	}

}

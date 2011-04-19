package hmm;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TextParser parser = new TextParser();
		/*for (int i=1; i<4; i++)
			parser.readText("data/train_" + i + ".pos");*/
		parser.readText("data/train.txt");
		
		TagCollection collection = parser.readTags();
		
		parser = new TextParser();
		//parser.readText("data/test_1.pos");
		parser.readText("data/test.txt");
		ArrayList<ArrayList<String>> sentenceList = parser.readSentences();
		
		for (ArrayList<String> sentence : sentenceList) {
			System.out.println("===================");
			for (String word : sentence) {
				System.out.print(word + " ");
			}
			System.out.println();

			System.out.println("=> " + collection.predictWithTags(sentence));
		}
	}

}

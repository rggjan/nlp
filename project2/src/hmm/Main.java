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
		parser.readText("data/train_1.pos");
		TagCollection collection = parser.readTags();
		
		parser = new TextParser();
		parser.readText("data/test_1.pos");
		ArrayList<ArrayList<String>> sentenceList = parser.readSentences(); 
		System.out.println(sentenceList);
	}

}

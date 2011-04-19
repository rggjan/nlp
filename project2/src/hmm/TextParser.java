package hmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TextParser {
	ArrayList<String> rawSentences;
	StateCollection tagCollection;
	
	public TextParser() {
		rawSentences = new ArrayList<String>();
	}

	/**
	 * read a file and append it to the input read so far
	 * @param filename
	 * @throws IOException
	 */
	public void readText(String filename) throws IOException {		
		BufferedReader br;
		StringBuffer sentence = new StringBuffer();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				filename)));

		String line;
		line = br.readLine();
		while (line != null) {
			line = line.toLowerCase();
			if (!line.matches("========+")) {
				sentence.append(" " + line);
			} else {
				rawSentences.add(sentence.toString());
				sentence = new StringBuffer();
			}
			line = br.readLine();
		}
	}
	
	/**
	 * get the sentences of the input. The sentences are represented
	 * by a list of word/tag pairs
	 * @return
	 */
	public ArrayList<ArrayList<String>> getSentences() {

		ArrayList<ArrayList<String>> sentenceList = new ArrayList<ArrayList<String>>();
		
		for (String sentence : rawSentences) {
			ArrayList<String> wordlist = new ArrayList<String>();
			
			for (String word : sentence.split(" ")) {
				if (word.matches("[a-z-]+/[a-z$]+")) {
					//String[] splitting = word.split("/");
					wordlist.add(word);
				}
			}
			
			// Check if whole sentence had words
			if (wordlist.size() > 0)
				sentenceList.add(wordlist);
		}
		
		return sentenceList;
	}
	
	/**
	 * Creates a state collection from the text read.
	 * @return
	 */
	public StateCollection getStateCollection() {
		tagCollection = new StateCollection();
		
		for (String sentence : rawSentences) {
			String previous_tag = "";
			
			for (String word : sentence.split(" ")) {
				if (word.matches("[a-z-]+/[a-z$]+")) {
					String[] splitting = word.split("/");
					tagCollection.addStateTansitionObservation(splitting[0], splitting[1], previous_tag);
					previous_tag = splitting[1];
				}
			}
			
			// Check if whole sentence was invalid
			if (previous_tag.length() > 0)
				tagCollection.addFinalStateTransitionObservation(previous_tag);
		}
		
		return tagCollection;
	}
}

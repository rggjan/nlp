package hmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextParser {
	ArrayList<String> rawText;
	TagCollection tagCollection;
	
	public TextParser() {
		rawText = new ArrayList<String>();
	}

	public void readText(String filename) throws IOException {
		BufferedReader br;
		StringBuffer sentence = new StringBuffer();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				filename)));

		String line;
		line = br.readLine();
		while (line != null) {
			if (line.trim().length() > 0) {
				sentence.append(" " + line);
			} else {
				rawText.add(sentence.toString());
				sentence = new StringBuffer();
			}
			line = br.readLine();
		}
	}
	
	public void readTags() {
		tagCollection = new TagCollection();
		
		for (String sentence : rawText) {
			String previous_tag = "";
			
			for (String word : sentence.split(" ")) {
				if (word.matches("[A-Za-z-]+/[A-Z$]+")) {
					String[] splitting = word.split("/");
					tagCollection.addTag(splitting[0], splitting[1], previous_tag);
					previous_tag = splitting[1];
				}
			}
			
			// Check if whole sentence was invalid
			if (previous_tag.length() > 0)
				tagCollection.addFinalTag(previous_tag);
		}
	}
}

package hmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TextParser {
	ArrayList<String> rawText;
	
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
}

package hmm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextParser {
	String rawText;

	public void readText(String filename) throws IOException {
		BufferedReader br;
		StringBuffer contentOfFile = new StringBuffer();

		br = new BufferedReader(new InputStreamReader(new FileInputStream(
				filename)));

		String line;

		while ((line = br.readLine()) != null) {
			contentOfFile.append(" " + line);
		}

		rawText = contentOfFile.toString();
	}
}

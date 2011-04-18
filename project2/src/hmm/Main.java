package hmm;

import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TextParser parser = new TextParser();
		parser.readText("data/train_1.pos");
		System.out.println(parser.rawText);
	}

}

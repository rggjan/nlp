import java.io.FileNotFoundException;
import java.io.IOException;

import dom.Text;
import fst.Alphabet;
import fst.Configuration;
import fst.ResultCollector;
import fst.State;
import fst.StringLink;
import fst.Tape;

public class Main {

	public static void main(String[] args) {
		Text text = new Text("Data/Train.txt");

		try {
			text.readText();
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO Exception!");
			System.exit(1);
		}

		text.generateWords();
		text.searchForPrefixes();
		text.searchForSuffixes();
		text.generateStatistics();

		// create alphabets
		Alphabet lexicalAlphabet = new Alphabet();
		Alphabet lowerAlphabet = new Alphabet();

		for (char ch = 'a'; ch <= 'z'; ch++) {
			lexicalAlphabet.addSymbol(ch);
			lowerAlphabet.addSymbol(ch);
		}

		for (char ch = 'A'; ch <= 'Z'; ch++) {
			lexicalAlphabet.addSymbol(ch);
			lowerAlphabet.addSymbol(ch);
		}

		// create and fill the lower tape
		Tape lowerTape = new Tape(lowerAlphabet);
		for (char ch : "HelloWorld".toCharArray()) {
			lowerTape.write(ch);
		}
		lowerTape.setPosition(0);

		// create the lexical tape
		Tape lexicalTape = new Tape(lexicalAlphabet);

		// create the start and end state
		State<ResultCollector> startState = new State<ResultCollector>();

		State<ResultCollector> endState = new State<ResultCollector>();
		endState.setAccepting(true);

		// add first link
		startState.addLink(new StringLink("HelloWorld", "Yeah", endState));

		// add second link
		startState.addLink(new StringLink("HelloWorld", "YeahBoah", endState));

		// add infinite link
		endState.addLink(new StringLink("", "i", endState));

		// create the result collector
		ResultCollector collector = new ResultCollector();

		// run the configuration
		Configuration<ResultCollector> config = new Configuration<ResultCollector>(
				startState, lowerTape, lexicalTape, collector);
		config.run();

		for (Configuration<ResultCollector> conf : collector
				.getAcceptingConfigurations()) {
			System.out.println("Result: " + conf.getUpperTape());
		}
	}
}

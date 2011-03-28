import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import dom.Text;
import fst.Configuration;
import fst.FstPrinter;
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

		// create and fill the lower tape
		Tape lowerTape = new Tape();
		for (char ch : "abcd".toCharArray()) {
			lowerTape.write(ch);
		}
		lowerTape.setPosition(0);

		// create the lexical tape
		Tape lexicalTape = new Tape();

		// create the start and end state
		State<ResultCollector> startState = new State<ResultCollector>();

		State<ResultCollector> middleState = new State<ResultCollector>();

		State<ResultCollector> endState = new State<ResultCollector>();
		endState.setAccepting(true);

		// add first link
		startState.addLink(new StringLink("a", "a^", middleState));
		startState.addLink(new StringLink("ab", "ab^", middleState));

		// add second link
		middleState.addLink(new StringLink("bcd", "bcd", endState));

		try {
			FstPrinter.print(startState, new PrintStream("graph.dot"));
			Runtime.getRuntime().exec(new String[]{"dot","-o","graph.gif","-T","gif","graph.dot"});
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Why does this give a result ab^ ... ?

		// add infinite link
		// endState.addLink(new StringLink("", "i", endState));

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

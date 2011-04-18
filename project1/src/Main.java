import java.io.FileNotFoundException;
import java.io.IOException;

import dom.CacheEnabler;
import dom.Text;
import dom.Word;
import dom.WordPart;
import fst.Configuration;
import fst.ResultCollector;
import fst.State;
import fst.StringLink;
import fst.Tape;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		Text testText = new Text("Data/Test.txt");
		Text trainingText = new Text("Data/Train.txt");

		try {
			trainingText.readText();
			testText.readText();
		} catch (FileNotFoundException e) {
			System.err.println("File not found!");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO Exception!");
			System.exit(1);
		}

		testText.generateWords();
		trainingText.generateWords();
		trainingText.generateAffixes();
		trainingText.generateStatistics();

		CacheEnabler.enabled=true;

		// print statistics
		printStatistics(trainingText);
		doAnalyzing(testText, trainingText);
		doExpanding(testText, trainingText);
	}

	private static void doExpanding(Text testText, Text trainingText) {
		// create the states
		State<ResultCollector>
			startState = new State<ResultCollector>(),
			preStemState = new State<ResultCollector>();

		// add links for the prefixes
		startState.addLink(new StringLink("", "",0, preStemState));
		for (WordPart part : trainingText.getPrefixes()) {
			if (part.name.equals("")) continue;
			if (part.countUniqueValidWords()<2) continue;

			startState.addLink(new StringLink(part.name, "", part
					.calculateWeigth()
					* trainingText.getPrefixesCount(part.name.length()),
					preStemState));
		}

		int count=0;
		for (WordPart stem: trainingText.getStems()){
			//if (stem.name.length()<2) continue;
			count++;
			State<ResultCollector>
				postStemState=new State<ResultCollector>(),
				preOutputPrefixState=new State<ResultCollector>(),
				preOutputStemState=new State<ResultCollector>(),
				preOutputSuffixState=new State<ResultCollector>(),
				finalState=new State<ResultCollector>();

			// add stem link
			preStemState.addLink(new StringLink(stem.name, "", stem
					.calculateWeigth()
					* trainingText.getStemsCount(stem.name.length()),
					postStemState));

			// add links for the suffixes
			postStemState.addLink(new StringLink("#", "",0, preOutputPrefixState));
			for (WordPart part : trainingText.getSuffixes()) {
				if (part.name.equals("")) continue;
				if (part.countUniqueValidWords()<2) continue;

				postStemState.addLink( new StringLink(part.name+"#", "",
 part
						.calculateWeigth()
						* trainingText.getSuffixesCount(part.name.length()),
						preOutputPrefixState));
			}

			// add links for all possible outputs
			preOutputPrefixState.addLink(new StringLink("", "",0, preOutputStemState));
			for (WordPart part:stem.getPrefixes()){
				if (part.name.equals("")) continue;
				if (part.countUniqueValidWords()<2) continue;
				preOutputPrefixState.addLink(new StringLink("", part.name,
						0, preOutputStemState));
			}
			preOutputStemState.addLink(new StringLink("", stem.name,0, preOutputSuffixState));

			// add links for all possible outputs
			preOutputSuffixState.addLink(new StringLink("", "",0, finalState));
			for (WordPart part:stem.getSuffixes()){
				if (part.name.equals("")) continue;
				if (part.countUniqueValidWords()<2) continue;
				preOutputSuffixState.addLink(new StringLink("", part.name,
						0, finalState));
			}

			finalState.setAccepting(true);
		}

		for (Word word : testText.getWords()) {
			// create and fill the input tape
			Tape inputTape = new Tape();
			for (char ch : word.getName().toCharArray()) {
				inputTape.write(ch);
			}
			// add the final # to mark the end of the word
			inputTape.write('#');
			inputTape.setPosition(0);

			// create the output tape
			Tape outputTape = new Tape();

			// create the result collector
			ResultCollector collector = new ResultCollector();

			// run the configuration
			Configuration<ResultCollector> config = new Configuration<ResultCollector>(
					startState, inputTape, outputTape, collector);
			config.run();

			collector.sortAcceptionConfigurations();
			System.out.printf("%s\n", word.getName());
			if (collector.getAcceptingConfigurations().size()>0){
				float weight=collector.getAcceptingConfigurations().get(0).getProbability();
				for (Configuration<ResultCollector> conf : collector
						.getAcceptingConfigurations()) {
					if (conf.getProbability()<weight) break;
					System.out.printf("  %s (%.2f) \n", conf.getOutputTape(), conf
							.getProbability());
				}
			}


		}
	}

	private static void printStatistics(Text trainingText) {
		System.out.println("Prefixes");
		for (WordPart part:trainingText.getPrefixes()){
			if (part.name=="") continue;
			if (part.countUniqueValidWords()<2) continue;
			System.out
					.printf("(%f*%d=%f)%s\n", part.calculateWeigth(),
							trainingText.getPrefixesCount(part.name.length()),
							part.calculateWeigth()
									* trainingText.getPrefixesCount(part.name
											.length()),
					part.name);

		}

		System.out.println("Stems");
		for (WordPart part:trainingText.getStems()){
			if (part.name=="") continue;
			System.out.printf("(%f*%d=%f)%s\n", part.calculateWeigth(),
					trainingText.getStemsCount(part.name.length()), part
							.calculateWeigth()
							* trainingText.getStemsCount(part.name.length()),
					part.name);

		}

		System.out.println("Suffixes");
		for (WordPart part:trainingText.getSuffixes()){
			if (part.name=="") continue;
			if (part.countUniqueValidWords()<2) continue;
			System.out
					.printf("(%f*%d=%f)%s\n", part.calculateWeigth(),
							trainingText.getSuffixesCount(part.name.length()),
							part.calculateWeigth()
									* trainingText.getSuffixesCount(part.name
											.length()), part.name);

		}
	}

	private static void doAnalyzing(Text testText, Text trainingText) {
		// create the states
		State<ResultCollector>
			startState = new State<ResultCollector>(),
			preStemState = new State<ResultCollector>(),
			postStemState = new State<ResultCollector>(),
			finalState = new State<ResultCollector>(),
			pastWordEndState = new State<ResultCollector>();

		// for testing the fst, we'll add # to the end of every word.
		// this makes sure the whole word is parsed before reaching the
		// accepting state
		pastWordEndState.setAccepting(true);

		// add empty prefixes and suffixes
		StringLink link = new StringLink("", "_^", preStemState);
		link.setWeight(0);
		startState.addLink(link);
		link = new StringLink("", "^_", finalState);
		link.setWeight(0);
		postStemState.addLink(link);

		// add word end link
		finalState.addLink(new StringLink("#", "", pastWordEndState));



		// add links for the prefixes
		for (WordPart part : trainingText.getPrefixes()) {
			if (part.name.equals("")) continue;
			if (part.countUniqueValidWords()<2) continue;

			link = new StringLink(part.name, part.name + "^", preStemState);
			link.setWeight(part.calculateWeigth()
					* trainingText.getPrefixesCount(part.name.length()));
			startState.addLink(link);
		}

		// add links for the stems
		for (WordPart part : trainingText.getStems()) {
			// do not filter the stems
			link = new StringLink(part.name, part.name, postStemState);
			link.setWeight(part.calculateWeigth()
					* trainingText.getStemsCount(part.name.length()));
			//link.setWeight(part.frequency);
			preStemState.addLink(link);
		}

		// add links for the suffixes
		for (WordPart part : trainingText.getSuffixes()) {
			if (part.name.equals("")) continue;
			if (part.countUniqueValidWords()<2) continue;

			link = new StringLink(part.name, "^" + part.name, finalState);
			link.setWeight(part.calculateWeigth()
					* trainingText.getSuffixesCount(part.name.length()));
			//link.setWeight(part.frequency);
			postStemState.addLink(link);
		}

		/*try {
			FstPrinter.print(startState, new PrintStream("graph.dot"));
			Runtime.getRuntime().exec(
					new String[] { "dot", "-o", "graph.gif", "-T", "gif",
							"graph.dot" });
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


		for (Word word : testText.getWords()) {
			// create and fill the input tape
			Tape inputTape = new Tape();
			for (char ch : word.getName().toCharArray()) {
				inputTape.write(ch);
			}
			// add the final # to mark the end of the word
			inputTape.write('#');
			inputTape.setPosition(0);

			// create the output tape
			Tape outputTape = new Tape();

			// create the result collector
			ResultCollector collector = new ResultCollector();

			// run the configuration
			Configuration<ResultCollector> config = new Configuration<ResultCollector>(
					startState, inputTape, outputTape, collector);
			config.run();

			collector.sortAcceptionConfigurations();
			System.out.printf("%s\n", word.getName());
			for (Configuration<ResultCollector> conf : collector
					.getAcceptingConfigurations()) {
				String[] parts=conf.getOutputTape().toString().split("\\^");
				WordPart prefix=null,stem=null,suffix=null;
				if (!"_".equals(parts[0])) prefix=trainingText.getPrefix(parts[0]);
				stem=trainingText.getStem(parts[1]);
				if (!"_".equals(parts[2])) suffix=trainingText.getSuffix(parts[2]);

				System.out
						.printf(
								"  %s (%.2f) %.2f %.2f %.2f\n",
								conf.getOutputTape(),
								conf
						.getProbability(),
								prefix != null ? (prefix
.calculateWeigth() * trainingText
										.getPrefixesCount(prefix.name.length()))
										: 0,
								stem != null ? (stem
.calculateWeigth() * trainingText
								.getStemsCount(stem.name.length()))
								:0,
								suffix != null ? (suffix
.calculateWeigth() * trainingText
								.getSuffixesCount(suffix.name.length()))
								:0
								);
			}


		}
	}
}

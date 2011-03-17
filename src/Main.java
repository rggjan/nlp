import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		String text = "house inhouse insist interesting prefix CAT incat preoccuppied, dog insist.";

		// Normalize the text
		text = text.toLowerCase();
		text = text.replaceAll("[,.;!?]", "");

		HashSet<String> finalstems = new HashSet<String>();
		HashSet<String> finalprefixes = new HashSet<String>();

		List<String> wordlist = Arrays.asList(text.split(" "));

		// Find all prefixes
		for (int i = 4; i >= 1; i--) {
			List<String> newlist = new ArrayList<String>();

			HashMap<String, HashSet<String>> prefixes = new HashMap<String, HashSet<String>>();

			// Iterate over all words
			for (String word : wordlist) {
				if (word.length() <= i) {
					newlist.add(word);
					continue;
				}

				String prefix = word.substring(0, i);
				String stem = word.substring(i, word.length());

				HashSet<String> set = prefixes.get(prefix);

				if (set == null) {
					set = new HashSet<String>();
					prefixes.put(prefix, set);
				}

				set.add(stem);
			}

			// Get all real prefixes
			for (Map.Entry<String, HashSet<String>> entry : prefixes.entrySet()) {
				String prefix = entry.getKey();
				HashSet<String> set = entry.getValue();

				if (set.size() > 1) {
					finalprefixes.add(prefix);
					// Only take prefixes that occur in multiple words
					for (String stem : set) {
						finalstems.add(stem);
					}
				} else {
					// We didn't have a prefix... put the word together again
					newlist.add(prefix + set.iterator().next());
				}
			}
			wordlist = newlist;
		}

		System.out.println("Prefixes:");
		for (String prefix : finalprefixes) {
			System.out.println(prefix);
		}

		System.out.println("Stems:");
		for (String stem : finalstems) {
			System.out.println(stem);
		}

		System.out.println("Others:");
		for (String word : wordlist) {
			System.out.println(word);
		}

		System.exit(0);

		// create alphabets
		Alphabet lexicalAlphabet = new Alphabet();
		Alphabet lowerAlphabet = new Alphabet();

		for (char ch = 'a'; ch <= 'z'; ch++) {
			Symbol s = new Symbol(ch);
			lexicalAlphabet.addSymbol(s);
			lowerAlphabet.addSymbol(s);
		}

		for (char ch = 'A'; ch <= 'Z'; ch++) {
			Symbol s = new Symbol(ch);
			lexicalAlphabet.addSymbol(s);
			lowerAlphabet.addSymbol(s);
		}

	}
}

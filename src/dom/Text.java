package dom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Text {
	private final List<Word> words = new ArrayList<Word>();

	private final String filename;
	private String rawText;

	public HashSet<String> prefixes = new HashSet<String>();
	public HashSet<String> suffixes = new HashSet<String>();

	public void readText() throws IOException {
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

	public Text(String filename) {
		this.filename = filename;
	}

	public void generateWords() {
		// Normalize the text
		String text = rawText.toLowerCase();
		text = text.replaceAll("[,.;!?*():\"'-]+", " ");

		for (String s : text.trim().split("\\s+")) {
			words.add(new Word(s));
		}
	}

	public HashSet<String> searchForSuffixes() {
		suffixes.clear();

		HashSet<String> reversed = searchForAffixes(new StringBuffer(rawText)
				.reverse().toString());

		for (String s : reversed) {
			suffixes.add(new StringBuffer(s).reverse().toString());
		}
		return suffixes;
	}

	public HashSet<String> searchForPrefixes() {
		prefixes = searchForAffixes(rawText);
		return prefixes;
	}

	public HashSet<String> searchForAffixes(String text) {
		HashSet<String> affixes = new HashSet<String>();

		// Normalize the text
		text = text.toLowerCase();
		text = text.replaceAll("[,.;!?*():\"'-]+", " ");

		List<String> wordlist = Arrays.asList(text.trim().split("\\s+"));

		// Find all prefixes
		for (int i = 4; i >= 1; i--) {
			// i is the length of the prefix to be tried
			List<String> newlist = new ArrayList<String>();

			// maps prefixes to the stems that follow
			HashMap<String, HashSet<String>> prefix_hash = new HashMap<String, HashSet<String>>();

			// Iterate over all words
			for (String word : wordlist) {
				if (word.length() <= i) {
					newlist.add(word);
					continue;
				}

				String prefix = word.substring(0, i);
				String stem = word.substring(i, word.length());

				HashSet<String> set = prefix_hash.get(prefix);

				if (set == null) {
					set = new HashSet<String>();
					prefix_hash.put(prefix, set);
				}

				set.add(stem);
			}

			// Get all real prefixes
			for (Map.Entry<String, HashSet<String>> entry : prefix_hash
					.entrySet()) {
				String prefix = entry.getKey();
				HashSet<String> set = entry.getValue();

				if (set.size() > 1) {
					affixes.add(prefix);
					// Only take prefixes that occur in multiple words
					/*
					 * for (String stem : set) { System.out.println(prefix + "/"
					 * + stem); }
					 */
				} else {
					// We didn't have a prefix... put the word together again
					newlist.add(prefix + set.iterator().next());
				}
			}

			// replace the word list
			// the newlist only contains words which were not yet used to
			// build a prefix
			wordlist = newlist;
		}

		return affixes;
	}
}

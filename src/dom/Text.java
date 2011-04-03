package dom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Text {
	private final HashMap<String,Word> words = new HashMap<String,Word>();

	private final String filename;
	private String rawText;

	private final HashMap<String, WordPart> prefixes = new HashMap<String, WordPart>();
	private final HashMap<String, WordPart> suffixes = new HashMap<String, WordPart>();
	private final HashMap<String, WordPart> stems = new HashMap<String, WordPart>();

	public ArrayList<Integer> suffixes_count;
	public ArrayList<Integer> stems_count;
	public ArrayList<Integer> prefixes_count;

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
			if (s=="") continue;
			Word w = words.get(s);
			if (w == null)
				words.put(s, new Word(s));
			else
				w.count++;
		}
	}

	public void generateAffixes() {
		// generate splittings
		for (Word word: words.values()){
			word.generateAllPossibleSplittings(this);
		}
	}

	public WordPart getPrefix(String name){
		return prefixes.get(name);
	}

	public WordPart getStem(String name){
		return stems.get(name);
	}

	public WordPart getSuffix(String name){
		return suffixes.get(name);
	}

	public WordPart getOrAddPrefix(String name){
		if (prefixes.containsKey(name)){
			return prefixes.get(name);
		}
		else{
			WordPart result=new WordPart(name);
			prefixes.put(name, result);
			return result;
		}
	}

	public WordPart getOrAddStem(String name){
		if (stems.containsKey(name)){
			return stems.get(name);
		}
		else{
			WordPart result=new WordPart(name);
			stems.put(name, result);
			return result;
		}
	}

	public WordPart getOrAddSuffix(String name){
		if (suffixes.containsKey(name)){
			return suffixes.get(name);
		}
		else{
			WordPart result=new WordPart(name);
			suffixes.put(name, result);
			return result;
		}
	}

	public Collection<WordPart> getPrefixes(){
		return prefixes.values();
	}
	public Collection<WordPart> getStems(){
		return stems.values();
	}
	public Collection<WordPart> getSuffixes(){
		return suffixes.values();
	}

	public Collection<Word> getWords(){
		return words.values();
	}

	public int getPrefixesCount(int i) {
		if (i == 0)
			return 1;
		return prefixes_count.get(i - 1);
	}

	public int getStemsCount(int i) {
		if (i == 0)
			return 1;
		return stems_count.get(i - 1);
	}

	public int getSuffixesCount(int i) {
		if (i == 0)
			return 1;
		return suffixes_count.get(i - 1);
	}

	public void generateStatistics() {
		prefixes_count = new ArrayList<Integer>(4);
		for (int i = 1; i <= 4; i++) {
			int sum = 0;
			for (WordPart prefix : prefixes.values()) {
				if (prefix.name.length() == i)
					sum += 1;
			}
			prefixes_count.add(i - 1, sum);
		}

		int max_stem_size = 0;
		for (WordPart stem : stems.values()) {
			if (stem.name.length() > max_stem_size)
				max_stem_size = stem.name.length();
		}

		stems_count = new ArrayList<Integer>(max_stem_size);
		for (int i = 1; i <= max_stem_size; i++) {
			int sum = 0;
			for (WordPart stem : stems.values()) {
				if (stem.name.length() == i)
					sum += 1;
			}
			stems_count.add(i - 1, sum);
		}

		suffixes_count = new ArrayList<Integer>(4);
		for (int i = 1; i <= 4; i++) {
			int sum = 0;
			for (WordPart suffix : suffixes.values()) {
				if (suffix.name.length() == i)
					sum += 1;
			}
			suffixes_count.add(i - 1, sum);
		}
	}
}

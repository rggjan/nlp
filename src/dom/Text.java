package dom;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Text {
	private HashMap<String,Word> words = new HashMap<String,Word>();

	private final String filename;
	private String rawText;

	private HashMap<String, WordPart> prefixes = new HashMap<String, WordPart>();
	private HashMap<String, WordPart> suffixes = new HashMap<String, WordPart>();
	private HashMap<String, WordPart> stems = new HashMap<String, WordPart>();

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
}

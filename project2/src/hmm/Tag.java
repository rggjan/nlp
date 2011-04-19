package hmm;

import java.util.HashMap;

public class Tag {
	private HashMap<String, Integer> wordCount;
	private HashMap<String, Integer> tagCount;
	
	private int numWords = 0;
	private int numTags;
	
	String name;

	public Tag(String name_) {
		wordCount = new HashMap<String, Integer>();
		tagCount = new HashMap<String, Integer>();
		
		name = name_;
	}

	public void addWord(String word) {
		// Laplace, renormalization
		if (!wordCount.containsKey(word)) {
			wordCount.put(word, 1);
			numWords++;
		}
		
		wordCount.put(word, wordCount.get(word) + 1);
		numWords++;
	}

	// Add next tag, null is final tag
	public void addNextTag(String tag) {
		// Laplace, renormalization
		if (!tagCount.containsKey(tag)) {
			tagCount.put(tag, 1);
			numTags++;
		}

		tagCount.put(tag, tagCount.get(tag) + 1);
		numTags++;
	}
	
	public String toString() {
		return "Tag(" + name + ")"; 
	}

	public double wordProbability(String word) {
		double result;
		if (wordCount.containsKey(word))
			result = wordCount.get(word);
		else
			result = 1.0;
		
		//System.out.println(name + " => " + word + ", " + result + "/" + numWords);
		return result / numWords;
	}

	public double nextTagProbability(String tag) {
		double result;
		if (tagCount.containsKey(tag))
			result = tagCount.get(tag);
		else
			result = 1.0;
		
		//System.out.println(name + " => " + tag + ", " + result + "/" + numTags);
		return result / numTags;
	}
}

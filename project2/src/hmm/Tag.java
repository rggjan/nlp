package hmm;

import java.util.HashMap;

public class Tag {
	private HashMap<String, Integer> wordCount;
	private HashMap<Tag, Integer> tagCount;
	
	private int numWords = 0;
	private int numTags;
	
	private String name;

	public Tag(String name_) {
		wordCount = new HashMap<String, Integer>();
		tagCount = new HashMap<Tag, Integer>();
		
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

	public String toString() {
		return "Tag(" + name + ")"; 
	}
	
	// Add next tag, null is final tag
	public void addNextTag(Tag tag) {
		// Laplace, renormalization
		if (!tagCount.containsKey(tag)) {
			tagCount.put(tag, 0);
			numTags++;
		}

		tagCount.put(tag, tagCount.get(tag));
		numTags++;
	}

	public double wordProbability(String word) {
		double result;
		if (wordCount.containsKey(word))
			result = wordCount.get(word);
		else
			result = 1.0;
		
		System.out.println(name + " => " + word + ", " + result + "/" + numWords);
		return result / numWords;
	}
}

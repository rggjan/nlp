package hmm;

import java.util.HashMap;

public class Tag {
	private HashMap<String, Integer> wordCount;
	private HashMap<Tag, Integer> tagCount;
	
	private int numWords = 0;
	private int numTags;

	public Tag() {
		wordCount = new HashMap<String, Integer>();
		tagCount = new HashMap<Tag, Integer>();
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
	public void addNextTag(Tag tag) {
		// Laplace, renormalization
		if (!tagCount.containsKey(tag)) {
			tagCount.put(tag, 0);
			numTags++;
		}

		tagCount.put(tag, tagCount.get(tag));
		numTags++;
	}
}

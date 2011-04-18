package hmm;

import java.util.HashMap;

public class TagCollection {
	
	HashMap<String, Tag> tagTable;
	
	public Tag startTag() {
		return tagTable.get("");
	}
	
	public TagCollection() {
		tagTable = new HashMap<String, Tag>();
	}

	public void addTag(String word, String tagString, String previousTagString) {
		Tag tag1;
		Tag tag2;
		
		// Load tag 1
		if (!tagTable.containsKey(previousTagString)) {
			tag1 = new Tag();
			tagTable.put(previousTagString, tag1);
		} else {
			tag1 = tagTable.get(previousTagString);
		}
		
		// Load tag 2
		if (!tagTable.containsKey(tagString)) {
			tag2 = new Tag();
			tagTable.put(tagString, tag2);
		} else {
			tag2 = tagTable.get(tagString);
		}
		
		tag2.addWord(word);
		tag1.addNextTag(tag2);
	}

	public void addFinalTag(String previousTag) {
		tagTable.get(previousTag).addNextTag(null);
	}
}

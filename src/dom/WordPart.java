package dom;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WordPart {

	private final List<Splitting> splittings = new LinkedList<Splitting>();

	public String name;

	public WordPart(String name) {
		this.name = name;
	}

	public void addSplittingImp(Splitting splitting) {
		splittings.add(splitting);
	}

	public void removeSplittingImp(Splitting splitting) {
		splittings.remove(splitting);
	}

	public List<Splitting> getSplittings(){
		return Collections.unmodifiableList(splittings);
	}
	
	/**
	 * Return the number of different words which have a splitting
	 * resulting in this
	 * @return
	 */
	public Set<Word> getUniqueWords(){
		HashSet<Word> result=new HashSet<Word>();
		for (Splitting s: splittings){
			result.add(s.getWord());
		}
		return result;
	}
	
	private int uniqueValidWordsCount;
	private boolean uniqueValidWordsCountCached;
	/**
	 * Count the number of different words, which have a valid splitting
	 * resulting in this
	 * @param wordCount
	 * @return
	 */
	public int countUniqueValidWords(int wordCount){
		if (CacheEnabler.enabled && uniqueValidWordsCountCached) return uniqueValidWordsCount;
		
		HashSet<Word> result=new HashSet<Word>();
		for (Splitting s: splittings){
			if (!s.isValid(wordCount)) continue;
			result.add(s.getWord());
		}
		
		if (CacheEnabler.enabled){
			uniqueValidWordsCount=result.size();
			uniqueValidWordsCountCached=true;
		}
		return result.size();
	}
	
	public HashSet<WordPart> getPrefixes(){
		HashSet<WordPart> result=new HashSet<WordPart>();
		for (Splitting s: splittings){
			result.add(s.getPrefix());
		}
		return result;
	}
	
	public HashSet<WordPart> getSuffixes(){
		HashSet<WordPart> result=new HashSet<WordPart>();
		for (Splitting s: splittings){
			result.add(s.getSuffix());
		}
		return result;
	}
}

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
	
	/**
	 * Count the number of different words, which have a valid splitting
	 * resulting in this
	 * @param wordCount
	 * @return
	 */
	public int countUniqueValidWords(int wordCount){
		HashSet<Word> result=new HashSet<Word>();
		for (Splitting s: splittings){
			if (!s.isValid(wordCount)) continue;
			result.add(s.getWord());
		}
		return result.size();
	}
}

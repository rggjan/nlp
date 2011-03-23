package fst;


import java.util.HashSet;
import java.util.Set;

/**
 * represents the possible symbols on a tape
 * @author ruedi
 *
 */
public class Alphabet {
	private Set<Character> symbols=new HashSet<Character>();
	
	public boolean contains(Character symbol) {
		return symbols.contains(symbol);
	}
	
	public void addSymbol(Character s){
		symbols.add(s);
	}

}

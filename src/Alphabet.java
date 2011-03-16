import java.util.HashSet;
import java.util.Set;

/**
 * represents the possible symbols on a tape
 * @author ruedi
 *
 */
public class Alphabet {
	private static Set<Symbol> usedSymbols=new HashSet<Symbol>();
	
	private Set<Symbol> symbols=new HashSet<Symbol>();
	
	public boolean contains(Symbol symbol) {
		return symbols.contains(symbol);
	}
	
	public void addSymbol(Symbol s){
		if (usedSymbols.contains(s)) throw new Error();
		symbols.add(s);
		usedSymbols.add(s);
	}

}

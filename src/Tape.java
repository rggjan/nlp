import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Tape {
	private Alphabet alphabet;
	private List<Symbol> contents=new ArrayList<Symbol>();

	public Symbol get(int position) {
		Symbol symbol = contents.get(position);
		if (!alphabet.contains(symbol)) throw new Error();
		return symbol;
	}
	
	public void set(int position, Symbol s){
		if (!alphabet.contains(s)) throw new Error();

		if (position==contents.size()){
			contents.add(s);
		}
		else{
			contents.set(position, s);
		}
	}
}

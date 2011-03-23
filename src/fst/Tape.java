package fst;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Tape {
	private final Alphabet alphabet;
	private final List<Character> contents=new ArrayList<Character>();

	public Tape(Alphabet alphabet){
		this.alphabet=alphabet;
	}
	public Character get(int position) {
		Character symbol = contents.get(position);
		if (!getAlphabet().contains(symbol)) throw new Error();
		return symbol;
	}
	
	public void set(int position, Character s){
		if (!getAlphabet().contains(s)) throw new Error();

		if (position==contents.size()){
			contents.add(s);
		}
		else{
			contents.set(position, s);
		}
	}


	public Alphabet getAlphabet() {
		return alphabet;
	}
	
	@Override
	public String toString() {
		String result="";
		for (Character ch: contents){
			result+=ch;
		}
		return result;
	}
}

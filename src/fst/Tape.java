package fst;


import java.util.ArrayList;
import java.util.List;
import static fst.Utils.*;

public class Tape {
	private final Alphabet alphabet;
	private final List<Character> contents=new ArrayList<Character>();
	private int position;
	
	public Tape(Alphabet alphabet){
		this.alphabet=alphabet;
	}
	
	public Tape(Tape other){
		this.position=other.position;
		this.alphabet=other.alphabet;
		contents.addAll(other.contents);
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
	
	public Character currentSymbol(){
		return get(position);
	}
	
	public void write(Character s){
		if (!getAlphabet().contains(s)) 
			throw new Error();
		set(position++, s);
	}
	
	public Character read(){
		return get(position++);
	}
	
	public boolean canRead(){
		return position<contents.size();
	}

	public void setPosition(int i) {
		position=i;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Tape)) return false;
		Tape other=(Tape) obj;
		
		if (position!=other.position) return false;
		if (alphabet!=other.alphabet) return false;
		if (!contents.equals(other.contents)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=1;
		hash=hash*31+position;
		hash=hash*31+contents.hashCode();
		hash=hash*31+alphabet.hashCode();
		return hash;
	}
}

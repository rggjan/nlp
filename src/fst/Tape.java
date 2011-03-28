package fst;


import java.util.ArrayList;
import java.util.List;
import static fst.Utils.*;

public class Tape {
	private final List<Character> contents=new ArrayList<Character>();
	private int position;
	
	public Tape(Tape other){
		this.position=other.position;
		contents.addAll(other.contents);
	}
	
	public Tape() {
	}

	public Character get(int position) {
		Character symbol = contents.get(position);
		return symbol;
	}
	
	public void set(int position, Character s){
		if (position==contents.size()){
			contents.add(s);
		}
		else{
			contents.set(position, s);
		}
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
		if (!contents.equals(other.contents)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=1;
		hash=hash*31+position;
		hash=hash*31+contents.hashCode();
		return hash;
	}
}

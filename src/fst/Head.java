package fst;


/**
 * The head moves on a tape.  It has a position
 * @author ruedi
 *
 */
public class Head {
	private Tape tape;
	private int position;
	
	public Head(Tape tape) {
		this.tape=tape;
	}

	public Head(Head other) {
		this.tape=other.tape;
		this.position=other.position;
	}

	public Character currentSymbol(){
		return tape.get(position);
	}
	
	public void write(Character s){
		if (!tape.getAlphabet().contains(s)) 
			throw new Error();
		tape.set(position, s);
		position++;
	}
	
	public Character read(){
		return tape.get(position++);
	}
}

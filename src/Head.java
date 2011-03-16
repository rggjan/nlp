
/**
 * The head moves on a tape.  It has a position
 * @author ruedi
 *
 */
public class Head {
	private Tape tape;
	private int position;
	
	public Symbol currentSymbol(){
		return tape.get(position);
	}
	
	public void write(Symbol s){
		tape.set(position, s);
		position++;
	}
	
	public Symbol read(){
		return tape.get(position++);
	}
}

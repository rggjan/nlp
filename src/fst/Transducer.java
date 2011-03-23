package fst;

import java.util.LinkedList;
import java.util.List;


public class Transducer<TConfiguration extends Configuration> {
	private Alphabet lowerAlphabet;
	private Alphabet upperAlphabet;
	
	private Tape lowerTape;
	private Tape upperTape;
	
	private State<TConfiguration> startState;
	
	public boolean isComplete(){
		return getLowerAlphabet()!=null
		&& upperAlphabet!=null
		&& lowerTape!=null
		&& upperTape!=null
		&& startState!=null;
	}
	
	private void checkInvariant(){
		if (lowerTape!=null && lowerAlphabet !=null && lowerTape.getAlphabet()!=lowerAlphabet)
			throw new Error();
		
		if (upperTape!=null && upperAlphabet!=null && upperTape.getAlphabet()!=upperAlphabet)
			throw new Error();
	}

	public void setLowerAlphabet(Alphabet lowerAlphabet) {
		this.lowerAlphabet = lowerAlphabet;
		checkInvariant();
	}

	public Alphabet getLowerAlphabet() {
		return lowerAlphabet;
	}

	public void setUpperAlphabet(Alphabet upperAlphabet) {
		this.upperAlphabet = upperAlphabet;
		checkInvariant();
	}

	public Alphabet getUpperAlphabet() {
		return upperAlphabet;
	}

	public void setLowerTape(Tape lowerTape) {
		this.lowerTape = lowerTape;
		checkInvariant();
	}

	public Tape getLowerTape() {
		return lowerTape;
	}

	public void setUpperTape(Tape upperTape) {
		this.upperTape = upperTape;
		checkInvariant();
	}

	public Tape getUpperTape() {
		return upperTape;
	}

	public void setStartState(State<TConfiguration> startState) {
		this.startState = startState;
	}

	public State<TConfiguration> getStartState() {
		return startState;
	}
}

package fst;

import java.util.LinkedList;
import java.util.List;


public class Transducer<TConfiguration extends Configuration> {
	private Alphabet lowerAlphabet;
	private Alphabet upperAlphabet;
	
	private State<TConfiguration> startState;
	
	public boolean isComplete(){
		return getLowerAlphabet()!=null
		&& upperAlphabet!=null
		&& startState!=null;
	}

	public void setLowerAlphabet(Alphabet lowerAlphabet) {
		this.lowerAlphabet = lowerAlphabet;
	}

	public Alphabet getLowerAlphabet() {
		return lowerAlphabet;
	}

	public void setUpperAlphabet(Alphabet upperAlphabet) {
		this.upperAlphabet = upperAlphabet;
	}

	public Alphabet getUpperAlphabet() {
		return upperAlphabet;
	}

	

	public void setStartState(State<TConfiguration> startState) {
		this.startState = startState;
	}

	public State<TConfiguration> getStartState() {
		return startState;
	}
}

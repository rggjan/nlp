package hmm;

import java.math.BigDecimal;

public abstract class State<T extends State> {

	public String name;
	
	public abstract BigDecimal nextStateProbability(T state);

	public abstract BigDecimal wordEmittingProbability(Word word);

}

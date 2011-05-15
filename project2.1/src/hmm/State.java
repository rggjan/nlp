package hmm;

import java.math.BigDecimal;

public abstract class State<T extends State> {

	public String name;
	
	public abstract BigDouble nextStateProbability(T state);

	public abstract BigDouble wordEmittingProbability(Word word);

	@Override
	public String toString() {
		return name;
	}
}

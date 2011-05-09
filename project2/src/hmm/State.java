package hmm;

public abstract class State<T extends State> {

	public String name;
	
	public abstract double nextStateProbability(T state);

	public abstract double wordEmittingProbability(Word word);

}

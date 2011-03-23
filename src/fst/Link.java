package fst;

public class Link<TCollector extends IResultCollector> {
	private State<TCollector> target;
	
	public boolean cross(State<TCollector> source, Configuration<TCollector> configuration){return true;}

	void setTarget(State<TCollector> target) {
		this.target = target;
	}

	public State<TCollector> getTarget() {
		return target;
	}
}

package fst;

public class Link<TCollector extends IResultCollector> {
	private State<TCollector> target;
	private double weight = 1;

	public boolean cross(State<TCollector> source, Configuration<TCollector> configuration){return true;}

	void setTarget(State<TCollector> target) {
		this.target = target;
	}

	public State<TCollector> getTarget() {
		return target;
	}

	public void setWeight(double d) {
		this.weight = d;
	}

	public double getWeight() {
		return weight;
	}
}

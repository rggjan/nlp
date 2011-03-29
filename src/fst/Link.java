package fst;

public class Link<TCollector extends IResultCollector> {
	private State<TCollector> target;
	private float weight=1;
	
	public boolean cross(State<TCollector> source, Configuration<TCollector> configuration){return true;}

	void setTarget(State<TCollector> target) {
		this.target = target;
	}

	public State<TCollector> getTarget() {
		return target;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getWeight() {
		return weight;
	}
}

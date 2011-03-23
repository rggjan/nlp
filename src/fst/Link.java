package fst;

public class Link<TConfiguration extends Configuration> {
	private State<TConfiguration> target;
	
	public boolean cross(State<TConfiguration> source, TConfiguration configuration){return true;}

	void setTarget(State<TConfiguration> target) {
		this.target = target;
	}

	public State<TConfiguration> getTarget() {
		return target;
	}
}

package fst;

/**
 * Represents a configuration of a transducer
 * @author ruedi
 *
 */
public class Configuration<TCollector extends IResultCollector> {
	private Tape lowerTape;
	private Tape upperTape;
	private State<TCollector> currentState;
	TCollector collector;
	
	public Configuration(State<TCollector> startState, Tape lowerTape, Tape upperTape, TCollector collector) {
		this.collector=collector;
		currentState=startState;
		this.lowerTape=lowerTape;
		this.upperTape=upperTape;
	}
	
	public Configuration(Configuration<TCollector> other) {
		collector=other.collector;
		currentState=other.currentState;
		lowerTape=new Tape(other.lowerTape);
		upperTape=new Tape(other.upperTape);
	}

	public void run(){
		if (currentState.isAccepting()){
			collector.success(this);
		}
		// run this configuration
		for (Link<TCollector> link:currentState.getLinks()){
			State<TCollector> target = link.getTarget();
			
			// create the new state
			Configuration<TCollector> configuration=new Configuration<TCollector>(this);
			
			// leave the current state, cross the link and enter the target state
			currentState.leave(link, configuration);
			link.cross(currentState, configuration);
			configuration.setCurrentState(target);
			target.enter(link, currentState, configuration);
			
			// do the recursive call
			configuration.run();
		}
	}
	
	public void setCurrentState(State<TCollector> currentState) {
		this.currentState = currentState;
	}

	public State<TCollector> getCurrentState() {
		return currentState;
	}


	public Tape getLowerTape() {
		return lowerTape;
	}

	public Tape getUpperTape() {
		return upperTape;
	}
}

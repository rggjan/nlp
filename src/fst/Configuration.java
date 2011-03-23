package fst;

/**
 * Represents a configuration of a transducer
 * @author ruedi
 *
 */
public abstract class Configuration<TConfiguration extends Configuration<TConfiguration>> {
	private Tape lowerTape;
	private Tape upperTape;
	private State<TConfiguration> currentState;
	private Transducer<TConfiguration> transducer;
	
	public Configuration(Transducer<TConfiguration> transducer, Tape lowerTape, Tape upperTape) {
		this.transducer=transducer;
		currentState=transducer.getStartState();
		this.lowerTape=lowerTape;
		this.upperTape=upperTape;
	}
	
	public Configuration(Configuration<TConfiguration> other) {
		this.transducer=other.transducer;
		currentState=other.currentState;
		lowerTape=new Tape(other.lowerTape);
		upperTape=new Tape(other.upperTape);
	}

	public void run(){
		if (currentState.isAccepting()){
			success();
		}
		// run this configuration
		for (Link<TConfiguration> link:currentState.getLinks()){
			State<TConfiguration> target = link.getTarget();
			// create the new state
			TConfiguration configuration=copy();
			
			// leave the current state, cross the link and enter the target state
			currentState.leave(link, configuration);
			link.cross(currentState, configuration);
			configuration.setCurrentState(target);
			target.enter(link, currentState, configuration);
			
			// do the recursive call
			configuration.run();
		}
	}
	
	protected void success() {}

	protected abstract TConfiguration copy();

	public void setCurrentState(State<TConfiguration> currentState) {
		this.currentState = currentState;
	}

	public State<TConfiguration> getCurrentState() {
		return currentState;
	}


	public Tape getLowerTape() {
		return lowerTape;
	}

	public Tape getUpperTape() {
		return upperTape;
	}
}

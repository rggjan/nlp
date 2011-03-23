package fst;

/**
 * Represents a configuration of a transducer
 * @author ruedi
 *
 */
public abstract class Configuration<TConfiguration extends Configuration<TConfiguration>> {
	private Head lowerHead;
	private Head upperHead;
	private State<TConfiguration> currentState;
	private Transducer<TConfiguration> transducer;
	
	public Configuration(Transducer<TConfiguration> transducer) {
		this.transducer=transducer;
		currentState=transducer.getStartState();
		lowerHead=new Head(transducer.getLowerTape());
		upperHead=new Head(transducer.getUpperTape());
	}
	
	public Configuration(Configuration<TConfiguration> other) {
		this.transducer=other.transducer;
		currentState=other.currentState;
		lowerHead=new Head(other.lowerHead);
		upperHead=new Head(other.upperHead);
	}

	public void run(){
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
		}
	}
	
	public abstract TConfiguration copy();

	public void setCurrentState(State<TConfiguration> currentState) {
		this.currentState = currentState;
	}

	public State<TConfiguration> getCurrentState() {
		return currentState;
	}


	public Head getLowerHead() {
		return lowerHead;
	}

	public Head getUpperHead() {
		return upperHead;
	}
}

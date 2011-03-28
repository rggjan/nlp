package fst;

import static fst.Utils.*;

import java.util.HashSet;

/**
 * Represents a configuration of a transducer
 * @author ruedi
 *
 */
public class Configuration<TCollector extends IResultCollector> {
	private Tape lowerTape;
	private Tape upperTape;
	private State<TCollector> currentState;
	private TCollector collector;
	private final HashSet<Configuration<TCollector>> activeConfigurations;
	
	public Configuration(State<TCollector> startState, Tape lowerTape, Tape upperTape, TCollector collector) {
		this.collector=collector;
		currentState=startState;
		this.lowerTape=lowerTape;
		this.upperTape=upperTape;
		activeConfigurations=new HashSet<Configuration<TCollector>>();
	}
	
	public Configuration(Configuration<TCollector> other) {
		collector=other.collector;
		currentState=other.currentState;
		lowerTape=new Tape(other.lowerTape);
		upperTape=new Tape(other.upperTape);
		activeConfigurations=other.activeConfigurations;
	}

	public void run(){
		// accept state befor checking for recursion
		if (currentState.isAccepting()){
			collector.success(this);
		}
		
		// check if the state has been seen already
		if (activeConfigurations.contains(this)) return;
		activeConfigurations.add(this);
		
		// run this configuration
		for (Link<TCollector> link:currentState.getLinks()){
			State<TCollector> target = link.getTarget();
			
			// create the new state
			Configuration<TCollector> configuration=new Configuration<TCollector>(this);
			
			// leave the current state, cross the link and enter the target state
			currentState.leave(link, configuration);
			if (link.cross(currentState, configuration)){
				// only do the rest if it was possible to cross the link
				configuration.setCurrentState(target);
				target.enter(link, currentState, configuration);
				
				// do the recursive call
				configuration.run();
			}
		}
		
		// remove the configuration from the active configurations
		activeConfigurations.remove(this);
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
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Configuration<?>)) return false;
		Configuration<?> other=(Configuration<?>) obj;
		if (currentState!=other.currentState) return false;
		if (collector!=other.collector) return false;
		if (!areEqual(lowerTape,other.lowerTape)) return false;
		//if (!areEqual(upperTape,other.upperTape)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=1;
		hash=hash*31+currentState.hashCode();
		hash=hash*31+collector.hashCode();
		if (lowerTape!=null) hash=hash*31+lowerTape.hashCode();
		//if (upperTape!=null) hash=hash*31+upperTape.hashCode();
		return hash;
	}
	
	
}

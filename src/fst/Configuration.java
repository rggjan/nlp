package fst;

import static fst.Utils.*;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Represents a configuration of a transducer
 * @author ruedi
 *
 */
public class Configuration<TCollector extends IResultCollector> {
	private Tape inputTape;
	private Tape outputTape;
	private State<TCollector> currentState;
	private TCollector collector;
	private final HashSet<Configuration<TCollector>> activeConfigurations;
	private float probability=1;
	
	public Configuration(State<TCollector> startState, Tape lowerTape, Tape upperTape, TCollector collector) {
		this.collector=collector;
		currentState=startState;
		this.inputTape=lowerTape;
		this.outputTape=upperTape;
		activeConfigurations=new HashSet<Configuration<TCollector>>();
	}
	
	public Configuration(Configuration<TCollector> other) {
		collector=other.collector;
		currentState=other.currentState;
		inputTape=new Tape(other.inputTape);
		outputTape=new Tape(other.outputTape);
		activeConfigurations=other.activeConfigurations;
		probability=other.probability;
	}

	public void run(){
		// accept state befor checking for recursion
		if (currentState.isAccepting()){
			collector.success(this);
		}
		
		// check if the state has been seen already
		if (activeConfigurations.contains(this)) return;
		activeConfigurations.add(this);
		
		LinkedList<Configuration<TCollector>> outgoingConfigurations=new LinkedList<Configuration<TCollector>>();
		float totalWeight=0;
		
		// run this configuration
		for (Link<TCollector> link:currentState.getLinks()){
			State<TCollector> target = link.getTarget();
			
			// create the new state
			Configuration<TCollector> configuration=new Configuration<TCollector>(this);
			
			// leave the current state, cross the link and enter the target state
			if (link.cross(currentState, configuration)){
				// only do the rest if it was possible to cross the link
				configuration.setCurrentState(target);
				
				// multiply with the unweighted weight of the link
				configuration.probability*=link.getWeight();
				totalWeight+=link.getWeight();
				
				// collect outgoing configurations
				outgoingConfigurations.add(configuration);
			}
		}
		
		for (Configuration<TCollector> configuration:outgoingConfigurations){
			// weighten the probabilities
			configuration.probability/=totalWeight;
			
			// do the recursive call
			configuration.run();
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


	public Tape getInputTape() {
		return inputTape;
	}

	public Tape getOutputTape() {
		return outputTape;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Configuration<?>)) return false;
		Configuration<?> other=(Configuration<?>) obj;
		if (currentState!=other.currentState) return false;
		if (collector!=other.collector) return false;
		if (!areEqual(inputTape,other.inputTape)) return false;
		//if (!areEqual(upperTape,other.upperTape)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash=1;
		hash=hash*31+currentState.hashCode();
		hash=hash*31+collector.hashCode();
		if (inputTape!=null) hash=hash*31+inputTape.hashCode();
		//if (upperTape!=null) hash=hash*31+upperTape.hashCode();
		return hash;
	}

	public float getProbability() {
		return probability;
	}
	
	
}

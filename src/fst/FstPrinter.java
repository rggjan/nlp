package fst;

import java.io.Console;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FstPrinter<T extends IResultCollector> {
	private Set<State<T>> handledStates=new HashSet<State<T>>();
	
	private HashMap<State<T>, Integer> ids=new HashMap<State<T>,Integer>();
	private int nextId;

	private PrintStream out;
	
	public FstPrinter(PrintStream out) {
		this.out=out;
	}

	@SuppressWarnings("unchecked")
	public static <T extends IResultCollector> void print(State<T> startState, PrintStream out){
		out.println("digraph G {");
		new FstPrinter<T>(out).printRec(startState);
		out.println("}");
	}

	private int getId(State<T> state){
		if (!ids.containsKey(state)){
			ids.put(state, nextId++);
		}
		return ids.get(state);	
	}
	
	private void printRec(State<T> state) {
		// avoid loops
		if (handledStates.contains(state)) return;
		handledStates.add(state);
		
		// print output for the state
		out.printf("%d [label=\"%s\"%s];\n",getId(state),Integer.toString(getId(state)),state.isAccepting()?" peripheries=2":"");
		
		// print output for all links leaving the state
		for(Link<T> link:state.getLinks()){
			out.printf("%d->%d [label=\"%s\"];\n", getId(state),getId(link.getTarget()),link.toString());
			
			// recursively print states
			printRec(link.getTarget());
		}
	}
}

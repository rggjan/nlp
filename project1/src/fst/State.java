package fst;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public class State<TCollector extends IResultCollector> {
	private final List<Link<TCollector>> links=new LinkedList<Link<TCollector>>();
	
	private boolean accepting;
	
	public List<Link<TCollector>> getLinks(){
		return Collections.unmodifiableList(links);
	}

	
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}
	public boolean isAccepting() {
		return accepting;
	}
	public void addLink(Link<TCollector> link) {
		links.add(link);
	}
}

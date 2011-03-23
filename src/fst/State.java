package fst;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



public class State<TConfiguration extends Configuration> {
	private final List<Link<TConfiguration>> links=new LinkedList<Link<TConfiguration>>();
	
	private boolean accepting;
	
	public List<Link<TConfiguration>> getLinks(){
		return Collections.unmodifiableList(links);
	}
	public void leave(Link<TConfiguration> link, TConfiguration configuration){}
	public void enter(Link<TConfiguration> link, State<TConfiguration> source, TConfiguration configuration){}
	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}
	public boolean isAccepting() {
		return accepting;
	}
}

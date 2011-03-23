package fst;

import java.util.*;

public class ResultCollector {
	private final LinkedList<DefaultConfiguration> acceptingConfigurations=new LinkedList<DefaultConfiguration>();
	
	public void addAcceptingConfiguration(DefaultConfiguration configuration){
		acceptingConfigurations.add(configuration);
	}

	public List<DefaultConfiguration> getAcceptingConfigurations() {
		return Collections.unmodifiableList(acceptingConfigurations);
	}
	
	
}

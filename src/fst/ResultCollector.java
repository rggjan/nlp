package fst;

import java.util.*;

public class ResultCollector implements IResultCollector<ResultCollector>{
	private final LinkedList<Configuration<ResultCollector>> acceptingConfigurations=new LinkedList<Configuration<ResultCollector>>();
	
	public void addAcceptingConfiguration(Configuration<ResultCollector> configuration){
		acceptingConfigurations.add(configuration);
	}

	public List<Configuration<ResultCollector>> getAcceptingConfigurations() {
		return Collections.unmodifiableList(acceptingConfigurations);
	}

	@Override
	public void success(Configuration<ResultCollector> configuration) {
		acceptingConfigurations.add(configuration);
	}
	
	
}

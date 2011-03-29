package fst;

import java.util.*;

public class ResultCollector implements IResultCollector<ResultCollector>{
	private final LinkedList<Configuration<ResultCollector>> acceptingConfigurations=new LinkedList<Configuration<ResultCollector>>();
	
	public void sortAcceptionConfigurations(){
		Collections.sort(acceptingConfigurations, new Comparator<Configuration<ResultCollector>>() {

			@Override
			public int compare(Configuration<ResultCollector> a,
					Configuration<ResultCollector> b) {
				return Float.compare(b.getProbability(), a.getProbability());
			}
		});
	}
	
	public List<Configuration<ResultCollector>> getAcceptingConfigurations() {
		return Collections.unmodifiableList(acceptingConfigurations);
	}

	@Override
	public void success(Configuration<ResultCollector> configuration) {
		acceptingConfigurations.add(configuration);
	}
	
	
}

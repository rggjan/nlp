package hmm;

import java.util.List;
import java.util.Random;

public class OptimizedStateCollection extends StateCollection<OptimizedState>{

	
	
	@Override
	protected OptimizedState createState(String s) {
		return new OptimizedState(s);
	}

	
}

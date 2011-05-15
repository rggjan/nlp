package hmm;

public class OptimizedStateCollection extends StateCollection<OptimizedState>{

	@Override
	protected OptimizedState createState(String s) {
		return new OptimizedState(s);
	}
	
}

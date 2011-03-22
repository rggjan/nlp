package dom;

import java.util.LinkedList;
import java.util.List;

public class WordPart {

	private final List<Splitting> splittings=new LinkedList<Splitting>();
	
	public void addSplittingImp(Splitting splitting) {
		splittings.add(splitting);
	}

	public void removeSplittingImp(Splitting splitting) {
		splittings.remove(splitting);
	}

}

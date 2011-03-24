package dom;

import java.util.LinkedList;
import java.util.List;

public class WordPart {

	private final List<Splitting> splittings = new LinkedList<Splitting>();

	public String name;

	public float frequency = 0;

	public WordPart(String name) {
		this.name = name;
	}

	public void addSplittingImp(Splitting splitting) {
		splittings.add(splitting);
	}

	public void removeSplittingImp(Splitting splitting) {
		splittings.remove(splitting);
	}

}

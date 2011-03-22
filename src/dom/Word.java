package dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Word {
	private final List<Splitting> splittings=new ArrayList<Splitting>();

	public List<Splitting> getSplittings() {
		return Collections.unmodifiableList(splittings);
	}

	public void addSplitting(Splitting s){
		if (s.getWord()!=null) throw new Error("splitting already added to an other word");
		splittings.add(s);
		s.setWordImp(this);
	}
	
	public void removeSplitting(Splitting s){
		splittings.remove(s);
		s.setWordImp(null);
	}

	void addSplittingImp(Splitting splitting) {
		splittings.add(splitting);
	}

	void removeSplittingImp(Splitting splitting) {
		splittings.remove(splitting);
	}
}

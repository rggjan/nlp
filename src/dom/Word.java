package dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Word {
	private final List<Splitting> splittings = new ArrayList<Splitting>();

	private String name;

	public int count = 1;

	public Word(String name) {
		this.name = name;
	}

	public List<Splitting> getSplittings() {
		return Collections.unmodifiableList(splittings);
	}

	public void generateSplittings(HashMap<String, WordPart> prefixes,
			HashMap<String, WordPart> suffixes, HashMap<String, WordPart> stems) {
		int length = name.length();

		for (int stem_start = 0; stem_start < length; stem_start++) {
			for (int suffix_start = stem_start + 1; suffix_start <= length; suffix_start++) {
				WordPart prefix = prefixes.get(name.substring(0, stem_start));
				WordPart suffix = suffixes.get(name.substring(suffix_start,
						length));

				if (prefix != null && suffix != null) {
					String stem_string = name.substring(stem_start,
							suffix_start);
					WordPart stem = stems.get(stem_string);

					if (stem == null) {
						stem = new WordPart(stem_string);
						stems.put(stem_string, stem);
					}

					// System.out.println(prefix.name + "/" + stem.name + "/"
					// + suffix.name);
					this.addSplitting(new Splitting(prefix, stem, suffix));
				}
			}
		}

		float size = splittings.size();
		for (Splitting s : splittings) {
			s.increaseFrequency(1 / size);
		}
	}

	public void addSplitting(Splitting s) {
		if (s.getWord() != null)
			throw new Error("splitting already added to an other word");
		splittings.add(s);
		s.setWordImp(this);
	}

	public void removeSplitting(Splitting s) {
		splittings.remove(s);
		s.setWordImp(null);
	}

	void addSplittingImp(Splitting splitting) {
		splittings.add(splitting);
	}

	void removeSplittingImp(Splitting splitting) {
		splittings.remove(splitting);
	}

	public String getName() {
		return name;
	}
}

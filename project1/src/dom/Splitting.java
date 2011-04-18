package dom;

public class Splitting {

	private Word word;
	private WordPart prefix, stem, suffix;

	public Splitting(WordPart prefix, WordPart stem, WordPart suffix) {
		setPrefix(prefix);
		setStem(stem);
		setSuffix(suffix);
	}

	void setWordImp(Word word) {
		this.word = word;
	}

	public void setWord(Word word) {
		if (this.word != null) {
			if (word != null)
				throw new Error();
			this.word.removeSplittingImp(this);
			this.word = null;
		} else {
			this.word = word;
			word.addSplittingImp(this);
		}
	}

	public Word getWord() {
		return word;
	}

	public void setSuffix(WordPart suffix) {
		if (this.suffix != null) {
			if (suffix != null)
				throw new Error();
			this.suffix.removeSplittingImp(this);
			this.suffix = null;
		} else {
			this.suffix = suffix;
			suffix.addSplittingImp(this);
		}
	}

	public WordPart getSuffix() {
		return suffix;
	}

	public void setStem(WordPart stem) {
		if (this.stem != null) {
			if (stem != null)
				throw new Error();
			this.stem.removeSplittingImp(this);
			this.stem = null;
		} else {
			this.stem = stem;
			stem.addSplittingImp(this);
		}
	}

	public WordPart getStem() {
		return stem;
	}

	public void setPrefix(WordPart prefix) {
		if (this.prefix != null) {
			if (prefix != null)
				throw new Error();
			this.prefix.removeSplittingImp(this);
			this.prefix = null;
		} else {
			this.prefix = prefix;
			prefix.addSplittingImp(this);
		}
	}

	public WordPart getPrefix() {
		return prefix;
	}

	/**
	 * Return true if this is a valid splitting
	 * @return
	 */
	public boolean isValid(){
		// accept the whole word
		if ("".equals(prefix.name)&&"".equals(suffix.name)) return true;

		// check word parts
		if (!"".equals(prefix.name)&&prefix.getUniqueWords().size()<2) return false;
		// if (stem.getUniqueWords().size()<2) return false;
		if (!"".equals(suffix.name)&&suffix.getUniqueWords().size()<2) return false;

		return true;
	}
}

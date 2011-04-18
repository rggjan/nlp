package fst;

public interface IResultCollector<TCollector extends IResultCollector> {
	void success(Configuration<TCollector> configuration);
}

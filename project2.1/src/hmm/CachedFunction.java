package hmm;

import java.util.Hashtable;

/**
 * Represents a cached function. On instantiation an IFunction is provided.
 * Whenever get() is called the first time for a given parameter combination,
 * the function is evaluated and the result is stored in the cache. 
 * 
 * The second time get is called with the same parameter combination, the stored
 * result from the cache is returned, without evaluating the function.
 * @author ruedi
 *
 * @param <T1>
 * @param <T2>
 * @param <TR>
 */
public class CachedFunction<T1,T2,TR>{
	private class Pair extends hmm.Pair<T1, T2>{
		public Pair(T1 first, T2 second){
			super(first,second);
		}
	}
	private Hashtable<Pair, TR> map=new Hashtable<Pair, TR>();
	private IFunction<T1, T2, TR> func;
	
	public CachedFunction(IFunction<T1, T2, TR> func){
		this.func=func;
	}
	
	/**
	 * return a cached result, or evaluate the function if none is available
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public TR get(T1 arg1, T2 arg2){
		Pair key=new Pair(arg1, arg2);
		
		if (map.containsKey(key)){
			// return the cached result
			return map.get(key);
		}
		else{
			// evaluate the function
			TR result=func.evaluate(arg1, arg2);
			
			// store the result of the function evaluation in the cache
			map.put(key, result);
			
			// return the result
			return result;
		}
	}
	
	/**
	 * Interface representing a function to be cahced by Cache
	 * @author ruedi
	 *
	 * @param <T1>
	 * @param <T2>
	 * @param <TR>
	 */
	public interface IFunction<T1,T2,TR>{
		TR evaluate(T1 arg1, T2 arg2);
	}
}
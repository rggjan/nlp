package hmm;

import java.util.Hashtable;

public class Cache<T1,T2,TR>{
	private Hashtable<T1, Hashtable<T2, TR>> map=new Hashtable<T1, Hashtable<T2,TR>>();
	private CachedFunction<T1, T2, TR> func;
	
	public Cache(CachedFunction<T1, T2, TR> func){
		this.func=func;
	}
	
	public TR get(T1 arg1, T2 arg2){
		if (!map.containsKey(arg1)){
			map.put(arg1, new Hashtable<T2, TR>());
		}
		
		Hashtable<T2,TR> m=map.get(arg1);
		
		if (m.containsKey(arg2)){
			return m.get(arg2);
		}
		else{
			TR result=func.evaluate(arg1, arg2);
			m.put(arg2, result);
			return result;
		}
	}
	
	public interface CachedFunction<T1,T2,TR>{
		TR evaluate(T1 arg1, T2 arg2);
	}
}
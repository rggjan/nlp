package hmm;

public class Pair<T1,T2> {

	T1 first;
	T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first=first;
		this.second=second;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pair<?,?>){
			Pair<?,?> other=(Pair<?, ?>) obj;
			return equals(first,other.first) && equals(second,other.second);
		}
		return false;
	}
	
	private static boolean equals(Object a, Object b){
		if (a==null && b==null ) return true;
		if (a==null && b!=null)  return false;
		if (a!=null && b==null ) return false;
		return a.equals(b);
	}
	
	@Override
	public int hashCode() {
		int result=1;
		result=31*result + (first==null ? 0 : first.hashCode());
		result=31*result + (second==null ? 0 : second.hashCode());
		return result;
	}
}

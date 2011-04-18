package fst;


public class Utils {
	public static boolean areEqual(Object a, Object b){
		if (a==null){
			return b==null;
		}
		else{
			return a.equals(b);
		}
	}
}

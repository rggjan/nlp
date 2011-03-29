import java.io.PrintStream;
import java.util.TreeMap;


public class Histogram {
	private TreeMap<Integer, Integer> data=new TreeMap<Integer, Integer>();
	
	public void add(int n){
		if (data.containsKey(n)){
			data.put(n,data.get(n)+1);
		}
		else{
			data.put(n,1);
		}
	}
	
	public void print(PrintStream out){
		for (int i=data.firstKey(); i<=data.lastKey(); i++){
			out.printf("%d %d\n", i,data.containsKey(i)?data.get(i):0);
		}
	}
}

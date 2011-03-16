import java.io.Console;
import java.util.HashMap;


public class Main {

	public static void main(String[] args){
		HashMap<String, String> prefixes = new HashMap<String, String>();
		HashMap<String, Boolean> prefix_found = new HashMap<String, Boolean>();
		
		String text = "house inhouse insist interesting";
		for (String word : text.split(" ")) {
			for (int i=4; i>=1; i--) {
				try {
					String prefix = word.substring(0, i);
					String stem = word.substring(i, word.length());
					
					if (stem.length() > 0) {
						if (prefix_found.get(prefix) != null) {
							System.out.println(prefix + "/" + stem);
							break;
						} else {
							if (prefixes.get(prefix) != null && prefixes.get(prefix) != stem) {
								System.out.println(prefix + "/" + prefixes.get(prefix));
								System.out.println(prefix + "/" + stem);
								
								prefix_found.put(prefix, true);
								break;
							} else {
								prefixes.put(prefix, stem);
							}
						}
					}
				} catch (java.lang.StringIndexOutOfBoundsException exception) {
					// This happens with short strings...
				}
			}
		}
		
		System.exit(0);
		
		// create alphabets
		Alphabet lexicalAlphabet=new Alphabet();
		Alphabet lowerAlphabet=new Alphabet();
		
		for (char ch='a'; ch<='z'; ch++) {
			Symbol s = new Symbol(ch);
			lexicalAlphabet.addSymbol(s);
			lowerAlphabet.addSymbol(s);
		}
		
		for (char ch='A'; ch<='Z'; ch++) {
			Symbol s = new Symbol(ch);
			lexicalAlphabet.addSymbol(s);
			lowerAlphabet.addSymbol(s);
		}
		
		
	}
}


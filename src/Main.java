import java.io.Console;
import java.util.HashMap;


public class Main {

	public static void main(String[] args){
		System.out.println("Hello World\n");

		HashMap<String, String> prefixes = new HashMap<String, String>();
		
		String text = "This is a sentence";
		for (String word : text.split(" ")) {
			for (int i=0; i<4; i++) {
				try {
					String prefix = word.substring(0, i);
					String stem = word.substring(i, word.length());

					System.out.println(prefix + "/" + stem);
				} catch (java.lang.StringIndexOutOfBoundsException exception) {
					// This happens with short strings...
				}
			}
		}
		
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


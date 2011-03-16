import java.io.Console;


public class Main {

	public static void main(String[] args){
		System.out.println("Hello World\n");
		
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


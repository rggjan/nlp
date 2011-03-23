package fst;

public class StringLink extends Link<DefaultConfiguration>{
	private String lowerString;
	private String upperString;
	
	public StringLink(String lowerString, String upperString, State<DefaultConfiguration> target){
		this.lowerString=lowerString;
		this.upperString=upperString;
		setTarget(target);
	}
	
	@Override
	public boolean cross(State<DefaultConfiguration> source, DefaultConfiguration configuration) {
		super.cross(source, configuration);
		Head lowerHead=configuration.getLowerHead();
		for (char ch: lowerString.toCharArray()){
			if (ch!=lowerHead.read()) return false;
		}
		for (char ch: upperString.toCharArray()){
			configuration.getUpperHead().write(ch);
		}
		return true;
	}
}

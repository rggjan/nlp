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
		Tape lowerHead=configuration.getLowerTape();
		for (char ch: lowerString.toCharArray()){
			if (!lowerHead.canRead()) return false;
			if (ch!=lowerHead.read()) return false;
		}
		for (char ch: upperString.toCharArray()){
			configuration.getUpperTape().write(ch);
		}
		return true;
	}
}

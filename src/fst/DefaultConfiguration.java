package fst;

public class DefaultConfiguration extends Configuration<DefaultConfiguration> {

	private final ResultCollector resultCollector;
	
	public DefaultConfiguration(Transducer<DefaultConfiguration> transducer, Tape lowerTape, Tape upperTape, ResultCollector resultCollector) {
		super(transducer,lowerTape,upperTape);
		this.resultCollector=resultCollector;
	}

	public DefaultConfiguration(DefaultConfiguration other) {
		super(other);
		resultCollector=other.resultCollector;
	}

	@Override
	public DefaultConfiguration copy() {
		return new DefaultConfiguration(this);
	}

	
	@Override
	protected void success() {
		resultCollector.addAcceptingConfiguration(this);
	}
}

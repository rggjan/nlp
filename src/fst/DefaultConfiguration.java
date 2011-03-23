package fst;

public class DefaultConfiguration extends Configuration<DefaultConfiguration> {

	public DefaultConfiguration(Transducer<DefaultConfiguration> transducer) {
		super(transducer);
	}

	public DefaultConfiguration(DefaultConfiguration other) {
		super(other);
	}

	@Override
	public DefaultConfiguration copy() {
		return new DefaultConfiguration(this);
	}

}

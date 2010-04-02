package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.mediator.Messages;

public class DefaultReverseEngineeringStrategyStub extends ReverseEngineeringStrategyStub {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy"; //$NON-NLS-1$

	protected DefaultReverseEngineeringStrategy defaultReverseEngineeringStrategy;

	protected DefaultReverseEngineeringStrategyStub(Object defaultReverseEngineeringStrategy) {
		super(defaultReverseEngineeringStrategy);
		if (defaultReverseEngineeringStrategy == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.defaultReverseEngineeringStrategy = (DefaultReverseEngineeringStrategy)defaultReverseEngineeringStrategy;
	}
	
	public static DefaultReverseEngineeringStrategyStub newInstance() {
		return new DefaultReverseEngineeringStrategyStub(new DefaultReverseEngineeringStrategy());
	}
}

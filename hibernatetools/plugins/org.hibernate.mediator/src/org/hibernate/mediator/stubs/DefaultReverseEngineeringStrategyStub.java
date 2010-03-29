package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;

public class DefaultReverseEngineeringStrategyStub extends ReverseEngineeringStrategyStub {
	
	protected DefaultReverseEngineeringStrategy defaultReverseEngineeringStrategy;

	protected DefaultReverseEngineeringStrategyStub(Object defaultReverseEngineeringStrategy) {
		super(defaultReverseEngineeringStrategy);
		this.defaultReverseEngineeringStrategy = (DefaultReverseEngineeringStrategy)defaultReverseEngineeringStrategy;
	}
	
	public static DefaultReverseEngineeringStrategyStub newInstance() {
		return new DefaultReverseEngineeringStrategyStub(new DefaultReverseEngineeringStrategy());
	}
}

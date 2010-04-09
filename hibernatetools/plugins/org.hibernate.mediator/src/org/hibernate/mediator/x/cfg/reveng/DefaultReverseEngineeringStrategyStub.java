package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;

public class DefaultReverseEngineeringStrategyStub extends ReverseEngineeringStrategyStub {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy"; //$NON-NLS-1$

	protected DefaultReverseEngineeringStrategyStub(Object defaultReverseEngineeringStrategy) {
		super(defaultReverseEngineeringStrategy, CL);
	}
	
	public static DefaultReverseEngineeringStrategyStub newInstance() {
		return new DefaultReverseEngineeringStrategyStub(HObject.newInstance(CL));
	}
}

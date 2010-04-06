package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class ReverseEngineeringStrategyStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringStrategy"; //$NON-NLS-1$

	protected ReverseEngineeringStrategyStub(Object reverseEngineeringStrategy) {
		super(reverseEngineeringStrategy, CL);
	}

	protected ReverseEngineeringStrategyStub(Object reverseEngineeringStrategy, String cn) {
		super(reverseEngineeringStrategy, cn);
	}

	public void setSettings(ReverseEngineeringSettingsStub settings) {
		invoke(mn(), settings);
	}
}

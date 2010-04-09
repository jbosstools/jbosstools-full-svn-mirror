package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;

public class ReverseEngineeringStrategy extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringStrategy"; //$NON-NLS-1$

	protected ReverseEngineeringStrategy(Object reverseEngineeringStrategy) {
		super(reverseEngineeringStrategy, CL);
	}

	protected ReverseEngineeringStrategy(Object reverseEngineeringStrategy, String cn) {
		super(reverseEngineeringStrategy, cn);
	}

	public void setSettings(ReverseEngineeringSettings settings) {
		invoke(mn(), settings);
	}
}

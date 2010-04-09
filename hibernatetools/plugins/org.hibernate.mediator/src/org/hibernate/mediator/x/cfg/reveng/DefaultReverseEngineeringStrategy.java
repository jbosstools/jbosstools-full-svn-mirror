package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;

public class DefaultReverseEngineeringStrategy extends ReverseEngineeringStrategy {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy"; //$NON-NLS-1$

	protected DefaultReverseEngineeringStrategy(Object defaultReverseEngineeringStrategy) {
		super(defaultReverseEngineeringStrategy, CL);
	}
	
	public static DefaultReverseEngineeringStrategy newInstance() {
		return new DefaultReverseEngineeringStrategy(HObject.newInstance(CL));
	}
}

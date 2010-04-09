package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;

public class ReverseEngineeringSettings extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringSettings"; //$NON-NLS-1$

	protected ReverseEngineeringSettings(Object reverseEngineeringSettings) {
		super(reverseEngineeringSettings, CL);
	}
	
	public static ReverseEngineeringSettings newInstance(ReverseEngineeringStrategy reverseEngineeringStrategy) {
		return new ReverseEngineeringSettings(newInstance(CL, reverseEngineeringStrategy));
	}

	public ReverseEngineeringSettings setDefaultPackageName(String defaultPackageName) {
		invoke(mn(), defaultPackageName);
		return this;
	}

	public ReverseEngineeringSettings setDetectManyToMany(boolean b) {
		invoke(mn(), b);
		return this;
	}

	public ReverseEngineeringSettings setDetectOneToOne(boolean b) {
		invoke(mn(), b);
		return this;
	}

	public ReverseEngineeringSettings setDetectOptimisticLock(boolean optimisticLockSupportEnabled) {
		invoke(mn(), optimisticLockSupportEnabled);
		return this;
	}

}

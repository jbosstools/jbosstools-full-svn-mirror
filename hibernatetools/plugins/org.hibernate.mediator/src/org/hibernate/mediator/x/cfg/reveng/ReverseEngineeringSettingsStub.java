package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;

public class ReverseEngineeringSettingsStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringSettings"; //$NON-NLS-1$

	protected ReverseEngineeringSettingsStub(Object reverseEngineeringSettings) {
		super(reverseEngineeringSettings, CL);
	}
	
	public static ReverseEngineeringSettingsStub newInstance(ReverseEngineeringStrategyStub reverseEngineeringStrategy) {
		return new ReverseEngineeringSettingsStub(newInstance(CL, reverseEngineeringStrategy));
	}

	public ReverseEngineeringSettingsStub setDefaultPackageName(String defaultPackageName) {
		invoke(mn(), defaultPackageName);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectManyToMany(boolean b) {
		invoke(mn(), b);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectOneToOne(boolean b) {
		invoke(mn(), b);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectOptimisticLock(boolean optimisticLockSupportEnabled) {
		invoke(mn(), optimisticLockSupportEnabled);
		return this;
	}

}

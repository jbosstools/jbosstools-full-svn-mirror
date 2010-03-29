package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ReverseEngineeringSettings;

public class ReverseEngineeringSettingsStub {
	
	protected ReverseEngineeringSettings reverseEngineeringSettings;

	protected ReverseEngineeringSettingsStub(Object reverseEngineeringSettings) {
		this.reverseEngineeringSettings = (ReverseEngineeringSettings)reverseEngineeringSettings;
	}
	
	public static ReverseEngineeringSettingsStub newInstance(ReverseEngineeringStrategyStub reverseEngineeringStrategy) {
		return new ReverseEngineeringSettingsStub(new ReverseEngineeringSettings(reverseEngineeringStrategy.reverseEngineeringStrategy));
	}

	public ReverseEngineeringSettingsStub setDefaultPackageName(String defaultPackageName) {
		reverseEngineeringSettings.setDefaultPackageName(defaultPackageName);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectManyToMany(boolean b) {
		reverseEngineeringSettings.setDetectManyToMany(b);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectOneToOne(boolean b) {
		reverseEngineeringSettings.setDetectOneToOne(b);
		return this;
	}

	public ReverseEngineeringSettingsStub setDetectOptimisticLock(boolean optimisticLockSupportEnabled) {
		reverseEngineeringSettings.setDetectOptimisticLock(optimisticLockSupportEnabled);
		return this;
	}

}

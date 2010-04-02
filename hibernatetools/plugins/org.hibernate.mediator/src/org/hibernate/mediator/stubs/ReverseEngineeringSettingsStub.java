package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.mediator.Messages;

public class ReverseEngineeringSettingsStub {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringSettings"; //$NON-NLS-1$

	protected ReverseEngineeringSettings reverseEngineeringSettings;

	protected ReverseEngineeringSettingsStub(Object reverseEngineeringSettings) {
		if (reverseEngineeringSettings == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
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

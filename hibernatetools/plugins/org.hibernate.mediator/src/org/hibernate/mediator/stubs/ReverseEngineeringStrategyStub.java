package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.mediator.Messages;

public class ReverseEngineeringStrategyStub {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringStrategy"; //$NON-NLS-1$

	protected ReverseEngineeringStrategy reverseEngineeringStrategy;

	protected ReverseEngineeringStrategyStub(Object reverseEngineeringStrategy) {
		if (reverseEngineeringStrategy == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.reverseEngineeringStrategy = (ReverseEngineeringStrategy)reverseEngineeringStrategy;
	}
	
	public static String getClassName() {
		return ReverseEngineeringStrategy.class.getName();
	}

	public void setSettings(ReverseEngineeringSettingsStub settings) {
		reverseEngineeringStrategy.setSettings(settings.reverseEngineeringSettings);
	}
}

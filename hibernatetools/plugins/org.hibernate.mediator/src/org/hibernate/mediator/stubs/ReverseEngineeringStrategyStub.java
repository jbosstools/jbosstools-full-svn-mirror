package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.mediator.Messages;

public class ReverseEngineeringStrategyStub {
	
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

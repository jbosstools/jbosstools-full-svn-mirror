package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;

public class ReverseEngineeringStrategyStub {
	
	protected ReverseEngineeringStrategy reverseEngineeringStrategy;

	protected ReverseEngineeringStrategyStub(Object reverseEngineeringStrategy) {
		this.reverseEngineeringStrategy = (ReverseEngineeringStrategy)reverseEngineeringStrategy;
	}
	
	public static String getClassName() {
		return ReverseEngineeringStrategy.class.getName();
	}

	public void setSettings(ReverseEngineeringSettingsStub settings) {
		reverseEngineeringStrategy.setSettings(settings.reverseEngineeringSettings);
	}
}

package org.hibernate.mediator.x.cfg.reveng;

import java.io.File;

import org.hibernate.mediator.base.HObject;

public class OverrideRepositoryStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.OverrideRepository"; //$NON-NLS-1$

	protected OverrideRepositoryStub(Object overrideRepository) {
		super(overrideRepository, CL);
	}
	
	public static OverrideRepositoryStub newInstance() {
		return new OverrideRepositoryStub(newInstance(CL));
	}

	public void addFile(File file) {
		invoke(mn(), file);
	}

	public ReverseEngineeringStrategyStub getReverseEngineeringStrategy(
			ReverseEngineeringStrategyStub res) {
		Object obj = invoke(mn(), res);
		if (obj == null) {
			return null;
		}
		return new ReverseEngineeringStrategyStub(obj);
	}

	public void addTableFilter(TableFilterStub tf) {
		invoke(mn(), tf);
	}

}

package org.hibernate.mediator.x.cfg.reveng;

import java.io.File;

import org.hibernate.mediator.base.HObject;

public class OverrideRepository extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.OverrideRepository"; //$NON-NLS-1$

	protected OverrideRepository(Object overrideRepository) {
		super(overrideRepository, CL);
	}
	
	public static OverrideRepository newInstance() {
		return new OverrideRepository(newInstance(CL));
	}

	public void addFile(File file) {
		invoke(mn(), file);
	}

	public ReverseEngineeringStrategy getReverseEngineeringStrategy(
			ReverseEngineeringStrategy res) {
		Object obj = invoke(mn(), res);
		if (obj == null) {
			return null;
		}
		return new ReverseEngineeringStrategy(obj);
	}

	public void addTableFilter(TableFilter tf) {
		invoke(mn(), tf);
	}

}

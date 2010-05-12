package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.HibernateConsoleRuntimeException;
import org.hibernate.mediator.base.HObject;

public class ReverseEngineeringSettings extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.ReverseEngineeringSettings"; //$NON-NLS-1$

	protected ReverseEngineeringSettings(Object reverseEngineeringSettings) {
		super(reverseEngineeringSettings, CL);
	}
	
	public static ReverseEngineeringSettings newInstance(ReverseEngineeringStrategy reverseEngineeringStrategy) {
		ReverseEngineeringSettings resNewInstance = null;
		HibernateConsoleRuntimeException hcre = null;
		try {
			resNewInstance = new ReverseEngineeringSettings(newInstance(CL, reverseEngineeringStrategy));
		} catch (HibernateConsoleRuntimeException hcre1) {
			hcre = hcre1;
		}
		if (resNewInstance == null) { // hibernate 3.5 & new hibernate-tools
			try {
				resNewInstance = new ReverseEngineeringSettings(newInstance(CL));
			} catch (HibernateConsoleRuntimeException hcre2) {
				//hcre = hcre2;
			}
		}
		if (resNewInstance == null) {
			throw hcre;
		}
		return resNewInstance;
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
		try {
			invoke(mn(), b);
		} catch (HibernateConsoleRuntimeException hcre1) {
			// hibernate 3.5 & new hibernate-tools
		}
		return this;
	}

	public ReverseEngineeringSettings setDetectOptimisticLock(boolean optimisticLockSupportEnabled) {
		invoke(mn(), optimisticLockSupportEnabled);
		return this;
	}

}

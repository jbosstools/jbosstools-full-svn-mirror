package org.hibernate.mediator.x.cfg;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.PersistentClass;

public class Mappings extends HObject {
	public static final String CL = "org.hibernate.cfg.Mappings"; //$NON-NLS-1$

	protected Mappings(Object mappings) {
		super(mappings, CL);
	}

	public void addClass(PersistentClass persistentClass) {
		invoke(mn(), persistentClass);
	}
}

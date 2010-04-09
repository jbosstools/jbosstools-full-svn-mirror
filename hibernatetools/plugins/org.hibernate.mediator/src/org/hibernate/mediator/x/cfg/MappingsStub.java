package org.hibernate.mediator.x.cfg;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.PersistentClassStub;

public class MappingsStub extends HObject {
	public static final String CL = "org.hibernate.cfg.Mappings"; //$NON-NLS-1$

	protected MappingsStub(Object mappings) {
		super(mappings, CL);
	}

	public void addClass(PersistentClassStub persistentClass) {
		invoke(mn(), persistentClass);
	}
}

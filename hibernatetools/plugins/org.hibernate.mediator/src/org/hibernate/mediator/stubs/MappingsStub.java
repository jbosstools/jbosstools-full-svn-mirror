package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class MappingsStub extends HObject {
	public static final String CL = "org.hibernate.cfg.Mappings"; //$NON-NLS-1$

	protected MappingsStub(Object mappings) {
		super(mappings, CL);
	}

	public void addClass(PersistentClassStub persistentClass) {
		invoke("addClass", persistentClass); //$NON-NLS-1$
	}
}

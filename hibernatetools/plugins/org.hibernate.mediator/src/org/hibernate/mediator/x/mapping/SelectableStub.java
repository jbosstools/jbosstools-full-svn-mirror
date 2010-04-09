package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;

public class SelectableStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Selectable"; //$NON-NLS-1$

	protected SelectableStub(Object selectable) {
		super(selectable, CL);
	}

	protected SelectableStub(Object selectable, String cn) {
		super(selectable, cn);
	}
}

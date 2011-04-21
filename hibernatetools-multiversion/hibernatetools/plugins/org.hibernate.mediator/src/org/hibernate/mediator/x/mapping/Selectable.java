package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.base.HObject;

public class Selectable extends HObject {
	public static final String CL = "org.hibernate.mapping.Selectable"; //$NON-NLS-1$

	protected Selectable(Object selectable) {
		super(selectable, CL);
	}

	protected Selectable(Object selectable, String cn) {
		super(selectable, cn);
	}
}

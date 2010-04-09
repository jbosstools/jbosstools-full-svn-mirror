package org.hibernate.mediator.x.engine;

import org.hibernate.mediator.base.HObject;

public class Mapping extends HObject {
	public static final String CL = "org.hibernate.engine.Mapping"; //$NON-NLS-1$

	public Mapping(Object mapping) {
		super(mapping, CL);
	}
}

package org.hibernate.mediator.x.ejb;

import org.hibernate.mediator.base.HObject;

public class Ejb3Configuration extends HObject {

	public static final String CL = "org.hibernate.ejb.Ejb3Configuration"; //$NON-NLS-1$

	protected Ejb3Configuration(Object ejb3Configuration) {
		super(ejb3Configuration, CL);
	}

}

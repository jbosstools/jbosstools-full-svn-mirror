package org.hibernate.mediator.x.ejb;

import org.hibernate.mediator.base.HObject;

public class HibernatePersistence extends HObject {

	public static final String CL = "org.hibernate.ejb.HibernatePersistence"; //$NON-NLS-1$

	protected HibernatePersistence(Object hibernatePersistence) {
		super(hibernatePersistence, CL);
	}

	public static final String AUTODETECTION = (String)HObject.readStaticFieldValueNoException(CL, "AUTODETECTION"); //$NON-NLS-1$

}

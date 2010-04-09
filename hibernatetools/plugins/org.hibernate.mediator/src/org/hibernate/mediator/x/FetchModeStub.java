package org.hibernate.mediator.x;

import org.hibernate.mediator.base.HObject;

public class FetchModeStub extends HObject {
	public static final String CL = "org.hibernate.FetchMode"; //$NON-NLS-1$

	public FetchModeStub(Object fetchMode) {
		super(fetchMode, CL);
	}
	
	public static final FetchModeStub DEFAULT = new FetchModeStub(readStaticFieldValue(CL, "DEFAULT")); //$NON-NLS-1$
	public static final FetchModeStub SELECT = new FetchModeStub(readStaticFieldValue(CL, "SELECT")); //$NON-NLS-1$
	public static final FetchModeStub JOIN = new FetchModeStub(readStaticFieldValue(CL, "JOIN")); //$NON-NLS-1$
}

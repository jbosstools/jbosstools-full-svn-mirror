package org.hibernate.mediator.x;

import org.hibernate.mediator.base.HObject;

public class FetchMode extends HObject {
	public static final String CL = "org.hibernate.FetchMode"; //$NON-NLS-1$

	public FetchMode(Object fetchMode) {
		super(fetchMode, CL);
	}
	
	public static final FetchMode DEFAULT = new FetchMode(readStaticFieldValue(CL, "DEFAULT")); //$NON-NLS-1$
	public static final FetchMode SELECT = new FetchMode(readStaticFieldValue(CL, "SELECT")); //$NON-NLS-1$
	public static final FetchMode JOIN = new FetchMode(readStaticFieldValue(CL, "JOIN")); //$NON-NLS-1$
}

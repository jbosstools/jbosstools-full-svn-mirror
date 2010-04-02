package org.hibernate.mediator.stubs;

import org.hibernate.FetchMode;
import org.hibernate.mediator.base.HObject;

public class FetchModeStub extends HObject {
	public static final String CL = "org.hibernate.FetchMode"; //$NON-NLS-1$

	public FetchModeStub(Object fetchMode) {
		super(fetchMode, CL);
	}
	
	public static final FetchModeStub DEFAULT = new FetchModeStub(FetchMode.DEFAULT);
	public static final FetchModeStub SELECT = new FetchModeStub(FetchMode.SELECT);
	public static final FetchModeStub JOIN = new FetchModeStub(FetchMode.JOIN);
}

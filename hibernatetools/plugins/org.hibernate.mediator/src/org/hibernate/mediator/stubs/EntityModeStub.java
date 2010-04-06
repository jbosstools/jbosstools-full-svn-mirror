package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class EntityModeStub extends HObject {
	public static final String CL = "org.hibernate.EntityMode"; //$NON-NLS-1$

	public static final EntityModeStub POJO = new EntityModeStub(readStaticFieldValue(CL, "POJO")); //$NON-NLS-1$
	public static final EntityModeStub DOM4J = new EntityModeStub(readStaticFieldValue(CL, "DOM4J")); //$NON-NLS-1$
	public static final EntityModeStub MAP = new EntityModeStub(readStaticFieldValue(CL, "MAP")); //$NON-NLS-1$

	protected EntityModeStub(Object entityMode) {
		super(entityMode, CL);
	}
}

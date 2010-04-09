package org.hibernate.mediator.x;

import org.hibernate.mediator.base.HObject;

public class EntityMode extends HObject {
	public static final String CL = "org.hibernate.EntityMode"; //$NON-NLS-1$

	public static final EntityMode POJO = new EntityMode(readStaticFieldValue(CL, "POJO")); //$NON-NLS-1$
	public static final EntityMode DOM4J = new EntityMode(readStaticFieldValue(CL, "DOM4J")); //$NON-NLS-1$
	public static final EntityMode MAP = new EntityMode(readStaticFieldValue(CL, "MAP")); //$NON-NLS-1$

	protected EntityMode(Object entityMode) {
		super(entityMode, CL);
	}
}

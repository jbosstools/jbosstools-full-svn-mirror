package javax.persistence.mediator.x;

import org.hibernate.mediator.base.HObject;

public class Entity extends HObject {

	public static final String CL = "javax.persistence.Entity"; //$NON-NLS-1$

	protected Entity(Object entity) {
		super(entity, CL);
	}
}

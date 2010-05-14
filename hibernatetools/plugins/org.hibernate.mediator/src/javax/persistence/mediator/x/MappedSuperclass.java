package javax.persistence.mediator.x;

import org.hibernate.mediator.base.HObject;

public class MappedSuperclass extends HObject {

	public static final String CL = "javax.persistence.MappedSuperclass"; //$NON-NLS-1$

	protected MappedSuperclass(Object mappedSuperclass) {
		super(mappedSuperclass, CL);
	}
}

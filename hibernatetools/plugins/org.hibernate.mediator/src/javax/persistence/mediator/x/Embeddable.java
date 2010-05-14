package javax.persistence.mediator.x;

import org.hibernate.mediator.base.HObject;

public class Embeddable extends HObject {

	public static final String CL = "javax.persistence.Embeddable"; //$NON-NLS-1$

	protected Embeddable(Object embeddable) {
		super(embeddable, CL);
	}
}

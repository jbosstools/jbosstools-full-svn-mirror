package org.hibernate.mediator.x;

import java.util.List;

import org.hibernate.mediator.base.HObject;

public class Criteria extends HObject {
	public static final String CL = "org.hibernate.Criteria"; //$NON-NLS-1$

	protected Criteria(Object criteria) {
		super(criteria, CL);
	}

	public void setMaxResults(int intValue) {
		invoke(mn(), intValue);
	}

	@SuppressWarnings("unchecked")
	public List<Object> list() {
		return (List<Object>)invoke(mn());
	}
}

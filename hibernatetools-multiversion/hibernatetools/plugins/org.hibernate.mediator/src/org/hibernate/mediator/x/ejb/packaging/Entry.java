package org.hibernate.mediator.x.ejb.packaging;

import java.io.InputStream;

import org.hibernate.mediator.base.HObject;

public class Entry extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.Entry"; //$NON-NLS-1$

	protected Entry(Object entry) {
		super(entry, CL);
	}

	public String getName() {
    	return (String)invoke(mn());
    }

	public InputStream getInputStream() {
    	return (InputStream)invoke(mn());
    }

}

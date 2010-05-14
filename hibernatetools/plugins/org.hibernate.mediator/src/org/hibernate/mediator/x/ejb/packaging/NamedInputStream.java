package org.hibernate.mediator.x.ejb.packaging;

import java.io.InputStream;

import org.hibernate.mediator.base.HObject;

public class NamedInputStream extends HObject {

	public static final String CL = "org.hibernate.ejb.packaging.NamedInputStream"; //$NON-NLS-1$

	protected NamedInputStream(Object namedInputStream) {
		super(namedInputStream, CL);
	}

	public static NamedInputStream newInstance() {
		return new NamedInputStream(HObject.newInstance(CL));
	}

	public static NamedInputStream create(String name, InputStream stream) {
		return (NamedInputStream)newInstance(CL, name, stream);
	}

	public String getName() {
		return (String)invoke(mn());
	}

	public InputStream getStream() {
		return (InputStream)invoke(mn());
	}

}

package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;

public class JoinStub extends HObject {
	public static final String CL = "org.hibernate.mapping.Join"; //$NON-NLS-1$

	protected JoinStub(Object join) {
		super(join, CL);
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<PropertyStub> al = new ArrayList<PropertyStub>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new PropertyStub(obj));
			}
		}
		return al.iterator();
	}
}

package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mediator.base.HObject;

public class Join extends HObject {
	public static final String CL = "org.hibernate.mapping.Join"; //$NON-NLS-1$

	protected Join(Object join) {
		super(join, CL);
	}

	@SuppressWarnings("unchecked")
	public Iterator<Property> getPropertyIterator() {
		Iterator it = (Iterator)invoke(mn());
		ArrayList<Property> al = new ArrayList<Property>();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj != null) {
				al.add(new Property(obj));
			}
		}
		return al.iterator();
	}
}

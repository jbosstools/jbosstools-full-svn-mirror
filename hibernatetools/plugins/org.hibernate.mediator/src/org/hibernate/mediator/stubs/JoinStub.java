package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.Property;
import org.hibernate.mediator.Messages;

public class JoinStub {
	protected Join join;

	protected JoinStub(Object join) {
		if (join == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.join = (Join)join;
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator<Property> it = (Iterator<Property>)join.getPropertyIterator();
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

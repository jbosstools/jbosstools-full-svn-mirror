package org.hibernate.console.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Join;
import org.hibernate.mapping.Property;

public class JoinStub {
	protected Join join;

	protected JoinStub(Object join) {
		this.join = (Join)join;
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator<Property> it = (Iterator<Property>)join.getPropertyIterator();
		ArrayList<PropertyStub> al = new ArrayList<PropertyStub>();
		while (it.hasNext()) {
			al.add(new PropertyStub(it.next()));
		}
		return al.iterator();
	}
}

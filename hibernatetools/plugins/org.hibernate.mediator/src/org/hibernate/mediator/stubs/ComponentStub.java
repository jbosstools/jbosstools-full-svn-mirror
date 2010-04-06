package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

public class ComponentStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.Component"; //$NON-NLS-1$

	protected ComponentStub(Object component) {
		super(component, CL);
	}

	public String getComponentClassName() {
		return (String)invoke(mn());
	}

	public PersistentClassStub getOwner() {
		return PersistentClassStubFactory.createPersistentClassStub(invoke(mn()));
	}

	public String getParentProperty() {
		return (String)invoke(mn());
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

	public boolean isEmbedded() {
		return (Boolean)invoke(mn());
	}
}

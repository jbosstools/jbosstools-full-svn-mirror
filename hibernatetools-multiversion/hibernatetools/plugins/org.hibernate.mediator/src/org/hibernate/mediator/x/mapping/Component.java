package org.hibernate.mediator.x.mapping;

import java.util.ArrayList;
import java.util.Iterator;


public class Component extends SimpleValue {
	public static final String CL = "org.hibernate.mapping.Component"; //$NON-NLS-1$

	protected Component(Object component) {
		super(component, CL);
	}

	public String getComponentClassName() {
		return (String)invoke(mn());
	}

	public PersistentClass getOwner() {
		return PersistentClassFactory.createPersistentClassStub(invoke(mn()));
	}

	public String getParentProperty() {
		return (String)invoke(mn());
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

	public boolean isEmbedded() {
		return (Boolean)invoke(mn());
	}
}

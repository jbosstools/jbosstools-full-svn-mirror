package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;
import org.hibernate.mediator.Messages;

public class ComponentStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.Component"; //$NON-NLS-1$

	protected Component component;

	protected ComponentStub(Object component) {
		super(component);
		if (component == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.component = (Component)component;
	}

	public String getComponentClassName() {
		return component.getComponentClassName();
	}

	public PersistentClassStub getOwner() {
		return PersistentClassStubFactory.createPersistentClassStub(component.getOwner());
	}

	public String getParentProperty() {
		return component.getParentProperty();
	}

	@SuppressWarnings("unchecked")
	public Iterator<PropertyStub> getPropertyIterator() {
		Iterator<Property> it = (Iterator<Property>)component.getPropertyIterator();
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
		return component.isEmbedded();
	}
}

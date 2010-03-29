package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.mapping.Component;
import org.hibernate.mapping.Property;

public class ComponentStub extends SimpleValueStub {
	protected Component component;

	protected ComponentStub(Object component) {
		super(component);
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
			al.add(new PropertyStub(it.next()));
		}
		return al.iterator();
	}

	public boolean isEmbedded() {
		return component.isEmbedded();
	}
}

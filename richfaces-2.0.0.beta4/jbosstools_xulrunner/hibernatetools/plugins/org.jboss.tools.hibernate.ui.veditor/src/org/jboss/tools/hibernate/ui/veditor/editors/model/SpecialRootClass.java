/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.veditor.editors.model;

import java.util.Iterator;

import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;

public class SpecialRootClass extends RootClass {

	private Property property;
	private Property parentProperty;

	public SpecialRootClass(Property property) {
		super();
		this.property = property;
		generate();
	}

	private void generate() {
		if (property != null) {
			Component component = null;
			if (property.getValue() instanceof Collection) {
				Collection collection = (Collection)property.getValue();
				component = (Component)collection.getElement();
			} else if (property.getValue() instanceof Component) {
				component = (Component)property.getValue();
			}
			if (component != null) {
				setClassName(component.getComponentClassName());
				PersistentClass ownerClass = component.getOwner();
				if (component.getParentProperty() != null) {
					Property property = null;
					try {
						property = ownerClass.getProperty(component.getParentProperty());
					} catch (Exception e) {}
					if (property == null && ownerClass.getIdentifierProperty().getName().equals(component.getParentProperty())) {
						property = ownerClass.getIdentifierProperty();
					}
					if (property != null) parentProperty = ownerClass.getIdentifierProperty();
				}
				Iterator iterator = component.getPropertyIterator();
				while (iterator.hasNext()) {
					Property property = (Property)iterator.next();
					if (property != null) addProperty(property);
				}
			}
		}
	}

	protected Property getParentProperty() {
		return parentProperty;
	}

	public Property getProperty() {
		return this.property;
	}
}

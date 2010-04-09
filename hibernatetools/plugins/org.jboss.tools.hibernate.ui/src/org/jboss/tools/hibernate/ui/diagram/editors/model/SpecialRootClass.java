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
package org.jboss.tools.hibernate.ui.diagram.editors.model;

import java.util.Iterator;

import org.hibernate.mediator.x.mapping.CollectionStub;
import org.hibernate.mediator.x.mapping.Component;
import org.hibernate.mediator.x.mapping.PersistentClass;
import org.hibernate.mediator.x.mapping.Property;
import org.hibernate.mediator.x.mapping.RootClass;

// TODO: What is this ? And why is it extending mapping classes ?!
// vitali: it seems this is class to "wrap" properties set to RootClass
// 
// TODO: vitali: try to change "extends RootClass" into property
public class SpecialRootClass extends RootClass {
//public class SpecialRootClass {

	private Property property;
	private Property parentProperty;

	//protected String entityName;
	//protected String className;
	//protected ArrayList properties = new ArrayList();
	//protected RootClass rootClass;
	
	public SpecialRootClass(Property property) {
		super(RootClass.newInstance());
		this.property = property;
		//this.rootClass = null;
		generate();
	}

	private void generate() {
		if (property == null) {
			return;
		}
		Component component = null;
		if (property.getValue() instanceof CollectionStub) {
			CollectionStub collection = (CollectionStub)property.getValue();
			component = (Component)collection.getElement();
		} else if (property.getValue() instanceof Component) {
			component = (Component)property.getValue();
		}
		if (component != null) {
			setClassName(component.getComponentClassName());
			setEntityName(component.getComponentClassName());
			PersistentClass ownerClass = component.getOwner();
			if (component.getParentProperty() != null) {
				parentProperty = Property.newInstance();
				parentProperty.setName(component.getParentProperty());
				parentProperty.setPersistentClass(ownerClass);
			}
			Iterator<Property> iterator = component.getPropertyIterator();
			while (iterator.hasNext()) {
				Property property = iterator.next();
				if (property != null) {
					addProperty(property);
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
	/** /
	public String getEntityName() {
		if (rootClass != null) {
			return rootClass.getEntityName();
		}
		return entityName;
	}

	public void setEntityName(String entityName) {
		if (rootClass != null) {
			rootClass.setEntityName(className);
		}
		this.entityName = entityName;
	}

	public String getClassName() {
		if (rootClass != null) {
			return rootClass.getClassName();
		}
		return className;
	}

	public void setClassName(String className) {
		if (rootClass != null) {
			rootClass.setClassName(className);
		}
		this.className = className;
	}
	
	public void addProperty(Property p) {
		properties.add(p);
		p.setPersistentClass(rootClass);
	}

	public RootClass getRootClass() {
		return rootClass;
	}

	public void setRootClass(RootClass rootClass) {
		this.rootClass = rootClass;
		if (rootClass != null) {
			this.entityName = rootClass.getEntityName();
			this.className = rootClass.getClassName();
		} else {
			this.entityName = null;
			this.className = null;
		}
	}
	/**/
}

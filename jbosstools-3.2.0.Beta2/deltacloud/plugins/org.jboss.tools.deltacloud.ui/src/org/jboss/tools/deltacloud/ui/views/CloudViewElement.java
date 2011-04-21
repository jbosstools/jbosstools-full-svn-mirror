/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views;

import java.util.ArrayList;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;

public abstract class CloudViewElement implements IAdaptable {

	private Object element;
	private CloudViewElement parent;
	private ArrayList<CloudViewElement> children;
	private String name;
	
	public abstract IPropertySource getPropertySource();
	
	public Object[] getChildren() {
		return children.toArray();
	}
	
	protected void clearChildren() {
		children.clear();
	}
	
	public boolean hasChildren() {
		return children.size() > 0;
	}
	
	public Object getParent() {
		return parent;
	}
	
	public void addChild(CloudViewElement e) {
		children.add(e);
		e.setParent(this);
	}
	
	public void setParent(CloudViewElement e) {
		parent = e;
	}
	
	public CloudViewElement(Object element, String name) {
		this.element = element;
		this.name = name;
		children = new ArrayList<CloudViewElement>();
	}
	
	public String getName() {
		return name;
	}
	
	public Object getElement() {
		return element;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			IPropertySource p = getPropertySource();
			return p;
		}
		return null;
	}
}

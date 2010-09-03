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

import org.eclipse.ui.views.properties.IPropertySource;

public abstract class CVCategoryElement extends CloudViewElement {

	public final static int INSTANCES = 0;
	public final static int IMAGES = 1;
	
	protected int type;
	protected boolean initialized;
	
	public CVCategoryElement(Object element, String name, int type) {
		super(element, name);
		this.type = type;
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return null;
	}

}

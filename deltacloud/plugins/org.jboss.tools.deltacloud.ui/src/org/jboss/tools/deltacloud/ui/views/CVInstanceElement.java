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
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class CVInstanceElement extends CloudViewElement {

	public CVInstanceElement(Object element, String name) {
		super(element, name);
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return new InstancePropertySource(this, getElement());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		System.err.println("CVInstanceElement#getAdapter: adapter = " + adapter);
		if (adapter == DeltaCloudInstance.class) {
			return getElement();
		} 

		return super.getAdapter(adapter);
	}
}

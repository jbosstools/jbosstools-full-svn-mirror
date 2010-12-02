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

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVInstanceElement extends CloudViewElement {

	public CVInstanceElement(Object element) {
		super(element);
	}

	public String getName() {
		Object element = getElement();
		StringBuilder sb = new StringBuilder();
		if (element instanceof DeltaCloudInstance) {
			DeltaCloudInstance instance = (DeltaCloudInstance) element;
			if (instance.getName() != null) {
				sb.append(instance.getName());
			}
			if (instance.getId() != null) {
				sb.append(" [").append(instance.getId()).append("] ");
			}
		}
		return sb.toString();

	}

	@Override
	public IPropertySource getPropertySource() {
		return new InstancePropertySource(this, getElement());
	}
}

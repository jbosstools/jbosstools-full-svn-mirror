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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVImageElement extends CloudViewElement {

	public CVImageElement(Object element, TreeViewer viewer) {
		super(element, viewer);
	}
	
	public String getName() {
		Object element = getElement();
		if (element instanceof DeltaCloudImage) {
			return ((DeltaCloudImage) element).getName();
		} else {
			return "";
		}
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return new ImagePropertySource(getElement());
	}
}

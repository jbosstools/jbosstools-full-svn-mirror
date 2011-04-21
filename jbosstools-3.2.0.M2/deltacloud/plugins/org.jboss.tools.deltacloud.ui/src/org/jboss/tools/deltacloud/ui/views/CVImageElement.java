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

public class CVImageElement extends CloudViewElement {

	public CVImageElement(Object element, String name) {
		super(element, name);
	}

	@Override
	public IPropertySource getPropertySource() {
		return new ImagePropertySource(getElement());
	}

}

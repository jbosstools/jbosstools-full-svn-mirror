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
package org.jboss.tools.deltacloud.ui.views.cloud;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.cloud.property.InstancePropertySource;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class InstanceItem extends DeltaCloudViewItem<DeltaCloudInstance> {

	protected InstanceItem(DeltaCloudInstance model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(model, parent, viewer);
	}

	public String getName() {
		DeltaCloudInstance instance = getModel();
		StringBuilder sb = new StringBuilder();
		if (instance != null) {
			String alias = instance.getAlias();
			if (alias != null) {
				sb.append(alias).append(' ');
			} else {
				String name = instance.getName();
				if (name != null) {
					sb.append(name);
				}
			}
			if (instance.getId() != null) {
				sb.append('[').append(instance.getId()).append(']');
			}
		}
		return sb.toString();
	}

	@Override
	public IPropertySource getPropertySource() {
		return new InstancePropertySource(this, getModel());
	}

	@Override
	public boolean hasChildren() {
		return false;
	}
}

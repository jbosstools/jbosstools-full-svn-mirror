/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.viewer.property;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

/**
 * @author Xavier Coulon
 */
public class TypeMapper extends AbstractTypeMapper {

	@SuppressWarnings("rawtypes")
	@Override
	public Class mapType(Object object) {
		if (object instanceof TreeNode) {
			return ((TreeNode) object).getValue().getClass();
		}
		return super.mapType(object);
	}

}

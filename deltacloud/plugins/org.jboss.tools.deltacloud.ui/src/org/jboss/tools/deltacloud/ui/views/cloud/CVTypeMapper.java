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

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;

public class CVTypeMapper extends AbstractTypeMapper {

	@SuppressWarnings("rawtypes")
	@Override
	public Class mapType(Object object) {
        if (object instanceof TreeNode) {
            return ((TreeNode) object).getValue().getClass();
        }
        return super.mapType(object);
    }

}

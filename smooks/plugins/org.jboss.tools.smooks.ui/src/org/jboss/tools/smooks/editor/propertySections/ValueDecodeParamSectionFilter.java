/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.editor.propertySections;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.IFilter;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.editparts.javamapping.JavaBeanChildNodeEditPart;
import org.jboss.tools.smooks.model.javabean.ValueType;

/**
 * @author Dart
 * 
 */
public class ValueDecodeParamSectionFilter implements IFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IFilter#select(java.lang.Object)
	 */
	public boolean select(Object toTest) {
		if (toTest == null)
			return false;
		if (toTest instanceof JavaBeanChildNodeEditPart) {
			AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) ((JavaBeanChildNodeEditPart) toTest)
					.getModel();
			Object data = model.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if (data instanceof ValueType
					|| data instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
				return true;
			}
		}
		return false;
	}

}

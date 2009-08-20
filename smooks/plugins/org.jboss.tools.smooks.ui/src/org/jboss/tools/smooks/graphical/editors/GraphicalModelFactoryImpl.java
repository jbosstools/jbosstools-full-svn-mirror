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
package org.jboss.tools.smooks.graphical.editors;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean12.BeanType;

/**
 * @author Dart
 * 
 */
public class GraphicalModelFactoryImpl implements GraphicalModelFactory{
	public Object createGraphicalModel(Object model, ISmooksModelProvider provider) {
		AbstractSmooksGraphicalModel graphModel = null;
		if (model instanceof BindingsType || model instanceof BeanType) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider
					.getEditingDomain();
			ITreeContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
			ILabelProvider labelProvider = new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory()) {
				@Override
				public String getText(Object object) {
					Object obj = AdapterFactoryEditingDomain.unwrap(object);
					if (obj instanceof AbstractAnyType) {
						return super.getText(obj);
					}
					return super.getText(object);
				}

			};
			graphModel = new JavaBeanGraphModel(model, contentProvider, labelProvider, provider);
			((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
		}
		return graphModel;
	}
}

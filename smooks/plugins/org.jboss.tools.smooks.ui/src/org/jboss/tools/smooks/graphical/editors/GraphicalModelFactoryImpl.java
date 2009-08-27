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
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.BeanType;

/**
 * @author Dart
 * 
 */
public class GraphicalModelFactoryImpl implements GraphicalModelFactory {
	public Object createGraphicalModel(Object model, ISmooksModelProvider provider) {
		AbstractSmooksGraphicalModel graphModel = null;
		if (model instanceof BindingsType || model instanceof BeanType) {
			AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();
			ITreeContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
			final ILabelProvider labelProvider = new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory());
			LabelProvider labelProvider1 = new LabelProvider() {

				@Override
				public Image getImage(Object element) {
					element = AdapterFactoryEditingDomain.unwrap(element);
					if (element instanceof BeanType || element instanceof BindingsType) {
						return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
								GraphicsConstants.IMAGE_JAVA_BEAN);
					}
					Image img = super.getImage(element);
					if (img == null) {
						return labelProvider.getImage(element);
					}
					return img;
				}

				@Override
				public String getText(Object element) {
					Object obj = AdapterFactoryEditingDomain.unwrap(element);
					if (obj instanceof BeanType) {
						String p = ((BeanType) obj).getBeanId();
						if (p == null) {
							p = "";
						}
						return p;
					}
					if (obj instanceof BindingsType) {
						String p = ((BindingsType) obj).getBeanId();
						if (p == null) {
							p = "";
						}
						return p;
					}

					if (obj instanceof ValueType) {
						String p = ((ValueType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}
					if (obj instanceof WiringType) {
						String p = ((WiringType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}
					if (obj instanceof ExpressionType) {
						String p = ((ExpressionType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}

					if (obj instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
						String p = ((org.jboss.tools.smooks.model.javabean12.ValueType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}
					if (obj instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
						String p = ((org.jboss.tools.smooks.model.javabean12.WiringType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}
					if (obj instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType) {
						String p = ((org.jboss.tools.smooks.model.javabean12.ExpressionType) obj).getProperty();
						if (p == null) {
							p = "";
						}
						return p;
					}

					return super.getText(element);
				}

			};
			graphModel = new JavaBeanGraphModel(model, contentProvider, labelProvider1, provider);
			((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
		}
		return graphModel;
	}
}

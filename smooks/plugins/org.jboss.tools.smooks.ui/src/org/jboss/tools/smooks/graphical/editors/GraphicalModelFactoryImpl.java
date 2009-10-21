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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.graphical.editors.model.JavaBeanGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.ResourceConfigGraphModelImpl;
import org.jboss.tools.smooks.graphical.editors.model.XSLTemplateContentProvider;
import org.jboss.tools.smooks.graphical.editors.model.XSLTemplateGraphicalModel;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.rules10.RuleBase;
import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.validation10.RuleType;
import org.jboss.tools.smooks.model.xsl.Xsl;

/**
 * @author Dart
 * 
 */
public class GraphicalModelFactoryImpl implements GraphicalModelFactory {

	public GraphicalModelFactoryImpl() {
	}

	private ILabelProvider createLabelProvider(AdapterFactory factory) {
		return new AdapterFactoryLabelProvider(factory) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider
			 * #getText(java.lang.Object)
			 */
			@Override
			public String getText(Object object) {
				String label = getGraphLabelText(object);
				if (label == null) {
					return super.getText(object);
				}
				return label;
			}

		};
	}

	private String getGraphLabelText(Object element) {
		Object obj = AdapterFactoryEditingDomain.unwrap(element);
		if (obj instanceof BeanType) {
			String p = ((BeanType) obj).getBeanId();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		if (obj instanceof BindingsType) {
			String p = ((BindingsType) obj).getBeanId();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}

		if (obj instanceof ValueType) {
			String p = ((ValueType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		if (obj instanceof WiringType) {
			String p = ((WiringType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		if (obj instanceof ExpressionType) {
			String p = ((ExpressionType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}

		if (obj instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
			String p = ((org.jboss.tools.smooks.model.javabean12.ValueType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		if (obj instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
			String p = ((org.jboss.tools.smooks.model.javabean12.WiringType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		if (obj instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType) {
			String p = ((org.jboss.tools.smooks.model.javabean12.ExpressionType) obj).getProperty();
			if (p == null) {
				p = "<NULL>";
			}
			return p;
		}
		return null;
	}

	public Object createGraphicalModel(Object model, ISmooksModelProvider provider) {
		AbstractSmooksGraphicalModel graphModel = null;
		AdapterFactoryEditingDomain editingDomain = (AdapterFactoryEditingDomain) provider.getEditingDomain();
		ITreeContentProvider contentProvider = new AdapterFactoryContentProvider(editingDomain.getAdapterFactory());
		ILabelProvider labelProvider = createLabelProvider(editingDomain.getAdapterFactory());
		SmooksGraphicsExtType extType = provider.getSmooksGraphicsExt();
		String version = extType.getPlatformVersion();

		if (model instanceof RuleType || model instanceof RuleBase || model instanceof RuleBasesType) {
			return null;
		}

		if (SmooksUIUtils.isUnSupportElement(version, (EObject) model)) {
			return null;
		}
		if (model instanceof BindingsType || model instanceof BeanType) {
			graphModel = new JavaBeanGraphModel(model, contentProvider, labelProvider, provider);
			((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
		}
		if (model instanceof Xsl) {
			graphModel = new XSLTemplateGraphicalModel(model, new XSLTemplateContentProvider(contentProvider),
					new XSLLabelProvider(labelProvider), provider);
			((TreeContainerModel) graphModel).setHeaderVisable(true);
		}
		if (graphModel == null && model instanceof AbstractResourceConfig) {
			graphModel = new ResourceConfigGraphModelImpl(model, contentProvider, labelProvider, provider);
			((ResourceConfigGraphModelImpl) graphModel).setHeaderVisable(true);
		}

		return graphModel;
	}
}

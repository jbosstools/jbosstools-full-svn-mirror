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
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.javabean.IBean;
import org.jboss.tools.smooks.model.javabean.IExpression;
import org.jboss.tools.smooks.model.javabean.IValue;
import org.jboss.tools.smooks.model.javabean.IWiring;

/**
 * @author Dart
 * 
 */
public class GraphicalModelFactoryImpl implements GraphicalModelFactory {

	public GraphicalModelFactoryImpl() {
	}

	protected ILabelProvider createLabelProvider(AdapterFactory factory) {
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

	protected String getGraphLabelText(Object element) {
		Object obj = AdapterFactoryEditingDomain.unwrap(element);
		if (obj instanceof IBean) {
			String p = ((IBean) obj).getBeanId();
			if (p == null) {
				p = "<NULL>"; //$NON-NLS-1$
			}
			return p;
		}

		if (obj instanceof IValue) {
			String p = ((IValue) obj).getProperty();
			if (p == null) {
				p = "<NULL>"; //$NON-NLS-1$
			}
			return p;
		}
		if (obj instanceof IWiring) {
			String p = ((IWiring) obj).getProperty();
			if (p == null) {
				p = "<NULL>"; //$NON-NLS-1$
			}
			return p;
		}
		if (obj instanceof IExpression) {
			String p = ((IExpression) obj).getProperty();
			if (p == null) {
				p = "<NULL>"; //$NON-NLS-1$
			}
			return p;
		}
		return null;
	}

	protected boolean canCreateGraphicalModel(Object model, ISmooksModelProvider provider) {
		//@DART
//		String version = provider.getPlatformVersion();
//		if (SmooksUIUtils.isUnSupportElement(version, (EObject) model)) {
//			return false;
//		}
		return true;
	}

	public Object createGraphicalModel(Object model, ISmooksModelProvider provider) {
		
		AbstractSmooksGraphicalModel graphModel = null;
		//@DART
//		String version = provider.getPlatformVersion();
//
//		if (SmooksUIUtils.isUnSupportElement(version, (EObject) model)) {
//			return null;
//		}
		// if (model instanceof BindingsType || model instanceof BeanType) {
		// graphModel = new JavaBeanGraphModel(model, contentProvider,
		// labelProvider, provider);
		// ((JavaBeanGraphModel) graphModel).setHeaderVisable(true);
		// }
		// if (model instanceof Xsl) {
		// graphModel = new XSLTemplateGraphicalModel(model, new
		// XSLTemplateContentProvider(contentProvider),
		// new XSLLabelProvider(labelProvider), provider);
		// ((TreeContainerModel) graphModel).setHeaderVisable(true);
		// }
		// if (graphModel == null && model instanceof AbstractResourceConfig) {
		// graphModel = new ResourceConfigGraphModelImpl(model, contentProvider,
		// labelProvider, provider);
		// ((ResourceConfigGraphModelImpl) graphModel).setHeaderVisable(true);
		// }

		return graphModel;
	}
}

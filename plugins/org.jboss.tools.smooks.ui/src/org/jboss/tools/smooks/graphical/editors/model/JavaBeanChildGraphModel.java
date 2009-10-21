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
package org.jboss.tools.smooks.graphical.editors.model;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.tree.model.BeanReferenceConnection;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean12.BeanType;

/**
 * @author Dart
 * 
 */
public class JavaBeanChildGraphModel extends AbstractResourceConfigChildNodeGraphModel {

	public JavaBeanChildGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider, domainProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#isLinkable
	 * (java.lang.Class)
	 */
	@Override
	public boolean isLinkable(Class<?> connectionType) {
		if (connectionType == null) {
			return true;
		}
		if (connectionType == BeanReferenceConnection.class) {
			Object data = this.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if (SmooksUIUtils.getBeanIDRefFeature((EObject)data) != null) {
				return true;
			}
		}
//		if (connectionType == ValueBindingConnection.class) {
//			Object data = this.getData();
//			data = AdapterFactoryEditingDomain.unwrap(data);
//			if (SmooksUIUtils.getSelectorFeature((EObject)data) != null) {
//				return true;
//			}
//		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getImage()
	 */
	@Override
	public Image getImage() {
		if (parentIsCollection()) {
			return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
					GraphicsConstants.IMAGE_JAVA_COLLECTION);
		}
		if (parentIsArray()) {
			return SmooksConfigurationActivator.getDefault().getImageRegistry().get(GraphicsConstants.IMAGE_JAVA_ARRAY);
		}
		return super.getImage();
	}

	private boolean parentIsCollection() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			String classString = null;
			if (parent instanceof BeanType) {
				classString = ((BeanType) parent).getClass_();
			}
			if (parent instanceof BindingsType) {
				classString = ((BindingsType) parent).getClass_();
			}
			if (classString != null)
				classString = classString.trim();

			IJavaProject project = SmooksUIUtils.getJavaProject(parent);
			if (project != null) {
				try {
					ProjectClassLoader loader = new ProjectClassLoader(project);
					Class<?> clazz = loader.loadClass(classString);
					if (Collection.class.isAssignableFrom(clazz)) {
						return true;
					}
				} catch (Throwable t) {

				}
			}
		}
		return false;
	}

	private boolean parentIsArray() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			String classString = null;
			if (parent instanceof BeanType) {
				classString = ((BeanType) parent).getClass_();
			}
			if (parent instanceof BindingsType) {
				classString = ((BindingsType) parent).getClass_();
			}
			if (classString != null)
				classString = classString.trim();
			if (classString.endsWith("]")) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getText()
	 */
	@Override
	public String getText() {
		if (parentIsArray()) {
			return "Array Entry";
		}
		if (parentIsCollection()) {
			return "Collection Entry";
		}
		return super.getText();
	}

}

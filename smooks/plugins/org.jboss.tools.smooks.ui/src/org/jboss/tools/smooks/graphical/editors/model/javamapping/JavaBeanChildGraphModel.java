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
package org.jboss.tools.smooks.graphical.editors.model.javamapping;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigChildNodeGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVLinkConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.CSVNodeModel;
import org.jboss.tools.smooks.model.javabean.ValueType;

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
		if (TreeNodeConnection.class.isAssignableFrom(connectionType)) {
			Object data = this.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if (SmooksUIUtils.getBeanIDRefFeature((EObject) data) != null) {
				return true;
			}
		}
		if (data instanceof ValueType || data instanceof org.jboss.tools.smooks.model.javabean12.ValueType
				|| connectionType == CSVLinkConnection.class) {
			return true;
		}
		// if (connectionType == ValueBindingConnection.class) {
		// Object data = this.getData();
		// data = AdapterFactoryEditingDomain.unwrap(data);
		// if (SmooksUIUtils.getSelectorFeature((EObject)data) != null) {
		// return true;
		// }
		// }
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #canLinkWithSource(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithSource(Object model) {
		// TODO Auto-generated method stub
		return super.canLinkWithSource(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #canLinkWithTarget(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithTarget(Object model) {
		AbstractSmooksGraphicalModel gm = (AbstractSmooksGraphicalModel) model;
		Object m = gm.getData();
		if (data instanceof ValueType || data instanceof org.jboss.tools.smooks.model.javabean12.ValueType
				|| m instanceof CSVNodeModel) {
			return !((CSVNodeModel) m).isRecord();
		}
		return super.canLinkWithTarget(model);
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

	public boolean parentIsCollection() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			return SmooksUIUtils.isCollectionJavaGraphModel(parent);
		}
		return false;
	}

	public boolean parentIsArray() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			return SmooksUIUtils.isArrayJavaGraphModel(parent);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		List<AbstractSmooksGraphicalModel> list = Collections.emptyList();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getText()
	 */
	@Override
	public String getText() {
		int index = this.getParent().getChildrenWithoutDynamic().indexOf(this);
		if (parentIsArray() && index == 0) {
			return "Array Entry";
		}
		if (parentIsCollection() && index == 0) {
			return "Collection Entry";
		}
		return super.getText();
	}

}

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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 * 
 */
public class JavaBeanGraphModel extends TreeContainerModel {

	protected IEditingDomainProvider domainProvider = null;

	public JavaBeanGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider);
		this.domainProvider = domainProvider;
	}

	@Override
	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new JavaBeanChildGraphModel(model, contentProvider, labelProvider, this.domainProvider);
	}

	@Override
	public void addTargetConnection(TreeNodeConnection connection) {
		super.addTargetConnection(connection);
		Object model = getData();
		if (model instanceof EObject) {
			EStructuralFeature feature = null;
			if (model instanceof BindingsType) {
				feature = JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT;
			}
			if (model instanceof BeanType) {
				feature = Javabean12Package.Literals.BEAN_TYPE__CREATE_ON_ELEMENT;
			}
			EObject owner = (EObject) model;
			AbstractSmooksGraphicalModel targetGraphModel = connection.getSourceNode();
			Object tm = targetGraphModel.getData();
			if (tm instanceof IXMLStructuredObject) {
				String selector = SmooksUIUtils.generateFullPath((IXMLStructuredObject) tm, "/");
				Command command = SetCommand.create(domainProvider.getEditingDomain(), owner, feature, selector);
				domainProvider.getEditingDomain().getCommandStack().execute(command);
			}
		}
	}

	@Override
	public void removeTargetConnection(TreeNodeConnection connection) {
		super.removeTargetConnection(connection);
		Object model = getData();
		if (model instanceof EObject) {
			EStructuralFeature feature = null;
			if (model instanceof BindingsType) {
				feature = JavabeanPackage.Literals.BINDINGS_TYPE__CREATE_ON_ELEMENT;
			}
			if (model instanceof BeanType) {
				feature = Javabean12Package.Literals.BEAN_TYPE__CREATE_ON_ELEMENT;
			}
			EObject owner = (EObject) model;
			Command command = SetCommand.create(domainProvider.getEditingDomain(), owner, feature, null);
			domainProvider.getEditingDomain().getCommandStack().execute(command);
		}
	}
}

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
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.model.javabean.ExpressionType;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean.ValueType;
import org.jboss.tools.smooks.model.javabean.WiringType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;

/**
 * @author Dart
 *
 */
public class JavaBeanChildGraphModel extends TreeNodeModel {

	protected IEditingDomainProvider domainProvider = null;
	
	public JavaBeanChildGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider , IEditingDomainProvider domainProvider) {
		super(data, contentProvider, labelProvider);
		this.domainProvider = domainProvider;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public void addTargetConnection(TreeNodeConnection connection) {
		super.addTargetConnection(connection);
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EStructuralFeature feature = getFeature(model);
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
	
	protected EStructuralFeature getFeature(Object model){
		EStructuralFeature feature = null;
		if(model == null){
			return null;
		}
		if (model instanceof WiringType) {
			feature = JavabeanPackage.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.WiringType) {
			feature = Javabean12Package.Literals.WIRING_TYPE__WIRE_ON_ELEMENT;
		}
		if (model instanceof ValueType) {
			feature = JavabeanPackage.Literals.VALUE_TYPE__DATA;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
			feature = Javabean12Package.Literals.VALUE_TYPE__DATA;
		}
		if (model instanceof ExpressionType) {
			feature = JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT;
		}
		if (model instanceof org.jboss.tools.smooks.model.javabean12.ExpressionType) {
			feature = Javabean12Package.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT;
		}
		
		return feature;
	}

	@Override
	public void removeTargetConnection(TreeNodeConnection connection) {
		super.removeTargetConnection(connection);
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EStructuralFeature feature = getFeature(model);
			EObject owner = (EObject) model;
			Command command = SetCommand.create(domainProvider.getEditingDomain(), owner, feature, null);
			domainProvider.getEditingDomain().getCommandStack().execute(command);
		}
	}
	
}

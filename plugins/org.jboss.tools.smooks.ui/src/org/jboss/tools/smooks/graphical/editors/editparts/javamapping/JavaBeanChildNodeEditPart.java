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
package org.jboss.tools.smooks.graphical.editors.editparts.javamapping;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.command.GEFAdapterCommand;
import org.jboss.tools.smooks.graphical.editors.editparts.AbstractResourceConfigChildNodeEditPart;
import org.jboss.tools.smooks.graphical.editors.model.javamapping.JavaBeanChildGraphModel;
import org.jboss.tools.smooks.model.ISmooksModelProvider;
import org.jboss.tools.smooks.model.javabean.IExpression;
import org.jboss.tools.smooks.model.javabean.IValue;
import org.jboss.tools.smooks.model.javabean.IWiring;
import org.jboss.tools.smooks.model.javabean.JavaBeanPackage;

/**
 * @author Dart
 * 
 */
public class JavaBeanChildNodeEditPart extends AbstractResourceConfigChildNodeEditPart {

	private List<Object> supportTypes = new ArrayList<Object>();

	public JavaBeanChildNodeEditPart() {
		super();
		
		supportTypes.add(IValue.class);
		supportTypes.add(IExpression.class);
		supportTypes.add(IWiring.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.editparts.
	 * AbstractResourceConfigChildNodeEditPart#isDragLink()
	 */
	@Override
	protected boolean isDragLink() {
		//@DART
//		IGraphicalEditorPart part = (IGraphicalEditorPart) getEditorPart();
//		if (SmooksFreemarkerTemplateGraphicalEditor.ID.equals(part.getID())) {
//			AbstractSmooksGraphicalModel model = (AbstractSmooksGraphicalModel) this.getModel();
//			Object data = model.getData();
//			data = AdapterFactoryEditingDomain.unwrap(data);
//			if (data instanceof org.jboss.tools.smooks.model.javabean12.ValueType) {
//				return true;
//			}
//		}
		return super.isDragLink();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#canDirectEdit
	 * ()
	 */
	@Override
	protected boolean canDirectEdit() {
		IEditorPart editorPart = this.getEditorPart();
		//@DART
//		if (editorPart instanceof IGraphicalEditorPart) {
//			if (SmooksFreemarkerTemplateGraphicalEditor.ID.equals(((IGraphicalEditorPart) editorPart).getID())) {
//				return false;
//			}
//		}
		JavaBeanChildGraphModel graphModel = (JavaBeanChildGraphModel) getModel();
		boolean isArray = graphModel.parentIsArray();
		boolean isCollection = graphModel.parentIsCollection();
		if (isArray || isCollection) {
			return super.canDirectEdit();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart#
	 * createDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command createDirectEditCommand(DirectEditRequest request) {
		Object value = request.getCellEditor().getValue();
		ISmooksModelProvider smooksModelProvider = (ISmooksModelProvider) this.getEditorPart().getAdapter(
				ISmooksModelProvider.class);
		AbstractSmooksGraphicalModel graphModel = (AbstractSmooksGraphicalModel) this.getModel();
		Object data = graphModel.getData();
		data = AdapterFactoryEditingDomain.unwrap(data);

		EStructuralFeature feature = getFeature(data);

		if (value != null && value instanceof String && smooksModelProvider != null && feature != null) {
			org.eclipse.emf.common.command.Command setCommand = SetCommand.create(smooksModelProvider
					.getEditingDomain(), data, feature, value);
			if (setCommand.canExecute()) {
				GEFAdapterCommand gefCommand = new GEFAdapterCommand(smooksModelProvider.getEditingDomain(), setCommand);
				return gefCommand;
			}
		}
		return null;
	}

	private EStructuralFeature getFeature(Object data) {

		if (data instanceof IValue) {
			return JavaBeanPackage.Literals.VALUE__PROPERTY;
		}
		if (data instanceof IWiring) {
			return JavaBeanPackage.Literals.WIRING__PROPERTY;
		}
		if (data instanceof IExpression) {
			return JavaBeanPackage.Literals.EXPRESSION__PROPERTY;
		}
		return null;
	}

	@Override
	protected EStructuralFeature getFeature(EObject model) {
		IEditorPart editorPart = this.getEditorPart();
		//@DART
//		if (editorPart instanceof IGraphicalEditorPart) {
//			if (SmooksFreemarkerTemplateGraphicalEditor.ID.equals(((IGraphicalEditorPart) editorPart).getID())) {
//				return null;
//			}
//		}

		if(model instanceof IWiring){
			return JavaBeanPackage.Literals.BEAN__WIRE_BINDINGS;
		}
		
		if(model instanceof IValue){
			return JavaBeanPackage.Literals.BEAN__VALUE_BINDINGS;
		}
		
		if(model instanceof IExpression){
			return JavaBeanPackage.Literals.BEAN__EXPRESSION_BINDINGS;
		}
		return null;
	}

}

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
package org.jboss.tools.smooks.configuration.editors.javabean;

import org.jboss.tools.smooks.configuration.editors.IPropertyUICreator;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class JavabeanExpressionUICreator extends PropertiesAndSetterMethodSearchFieldEditorCreator implements
		IPropertyUICreator {
//	protected String getClassString(Object model) {
//		if (model instanceof EObject) {
//			EObject container = ((EObject) model).eContainer();
//			if(container instanceof BindingsType){
//				return ((BindingsType)container).getClass_();
//			}
//		}
//		return super.getClassString(model);
//	}
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#createExtendUI
//	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
//	 * org.eclipse.ui.forms.widgets.FormToolkit,
//	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
//	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
//	 */
//	@Override
//	public List<AttributeFieldEditPart> createExtendUIOnBottom(AdapterFactoryEditingDomain editingdomain,
//			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
//		List<AttributeFieldEditPart> list = new ArrayList<AttributeFieldEditPart>();
//		AttributeFieldEditPart cdatatext = SmooksUIUtils.createCDATAFieldEditor(Messages.JavabeanExpressionUICreator_ExpressionLabel, editingdomain, toolkit,
//				parent, model, null, true);
//		if (cdatatext != null) {
//			list.add(cdatatext);
//
//			Control c = cdatatext.getContentControl();
//
//			if (c instanceof Text) {
//				final FieldAssistDisposer disposer = SmooksUIUtils.addBindingsContextAssistToText((Text) c,
//						SmooksUIUtils.getSmooks11ResourceListType((EObject) model));
//				c.addDisposeListener(new DisposeListener() {
//
//					/*
//					 * (non-Javadoc)
//					 * 
//					 * @see
//					 * org.eclipse.swt.events.DisposeListener#widgetDisposed
//					 * (org.eclipse.swt.events.DisposeEvent)
//					 */
//					public void widgetDisposed(DisposeEvent e) {
//						disposer.dispose();
//					}
//
//				});
//			}
//		}
//		return list;
//	}
//
//	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain,
//			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
//
//		List<AttributeFieldEditPart> list = new ArrayList<AttributeFieldEditPart>();
//
//		AttributeFieldEditPart pEditPart = createPropertiesSearchFieldEditor(toolkit, parent, getPropertyDescriptor(
//				editingdomain, JavabeanPackage.Literals.EXPRESSION_TYPE__PROPERTY, model), model);
//		AttributeFieldEditPart mEditPart = createMethodsSearchFieldEditor(toolkit, parent, getPropertyDescriptor(
//				editingdomain, JavabeanPackage.Literals.EXPRESSION_TYPE__SETTER_METHOD, model), model);
//		list.add(pEditPart);
//		list.add(mEditPart);
//		list.addAll(createElementSelectionSection(Messages.JavabeanExpressionUICreator_ExecuteOngroupname, editingdomain, toolkit, parent, model,
//				formEditor, part,JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT,
//				JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS));
//
//		return list;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
//	 * (org.eclipse.emf.ecore.EAttribute)
//	 */
//	@Override
//	public boolean ignoreProperty(EAttribute feature) {
//		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT) {
//			return true;
//		}
//		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS) {
//			return true;
//		}
//		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__VALUE) {
//			return true;
//		}
//		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__PROPERTY) {
//			return true;
//		}
//		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__SETTER_METHOD) {
//			return true;
//		}
//		return super.ignoreProperty(feature);
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
//	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
//	 * org.eclipse.swt.widgets.Composite,
//	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
//	 * org.eclipse.emf.ecore.EAttribute,
//	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
//	 */
//	@Override
//	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
//			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
//			ISmooksModelProvider formEditor, IEditorPart part) {
//		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor,part);
//	}

}

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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.actions.OpenEditorEditInnerContentsAction;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.IPropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class JavabeanExpressionUICreator extends PropertyUICreator implements IPropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#createExtendUI
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnBottom(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, SmooksMultiFormEditor formEditor) {
		List<AttributeFieldEditPart> list = new ArrayList<AttributeFieldEditPart>();
		OpenEditorEditInnerContentsAction openCDATAEditorAction = new OpenEditorEditInnerContentsAction(editingdomain,
				(AnyType) model, SmooksUIUtils.VALUE_TYPE_TEXT, "txt");

		AttributeFieldEditPart cdatatext = SmooksUIUtils.createStringFieldEditor("Expression", parent, editingdomain,
				toolkit, null, model, true, false, false, 300, null, SmooksUIUtils.VALUE_TYPE_TEXT,
				openCDATAEditorAction);
		
		if(cdatatext != null){
			list.add(cdatatext);
		}
		openCDATAEditorAction.setRelateText((Text)cdatatext.getContentControl());
		
		return list;
	}
	
	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, SmooksMultiFormEditor formEditor) {
		List<AttributeFieldEditPart> list = createElementSelectionSection("Execute On Element", editingdomain, toolkit,
				parent, model, formEditor, JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT,
				JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS);
		
		return list;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT) {
			return true;
		}
		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS) {
			return true;
		}
		if (feature == JavabeanPackage.Literals.EXPRESSION_TYPE__VALUE) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute,
	 * org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	@Override
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			SmooksMultiFormEditor formEditor) {
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

}

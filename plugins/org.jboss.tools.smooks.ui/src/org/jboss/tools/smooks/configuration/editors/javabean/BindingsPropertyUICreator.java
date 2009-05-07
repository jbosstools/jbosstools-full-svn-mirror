/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.javabean;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;

/**
 * @author Dart (dpeng@redhat.com)
 *         <p>
 *         Apr 8, 2009
 */
public class BindingsPropertyUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.IPropertyUICreator#canCreate
	 * (org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public boolean canCreate(IItemPropertyDescriptor itemPropertyDescriptor, Object model, EAttribute feature) {
		if (feature == JavabeanPackage.eINSTANCE.getBindingsType_Class()) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,SmooksMultiFormEditor formEditor) {
		if (feature == JavabeanPackage.eINSTANCE.getBindingsType_Class()) {
			return createBeanClassTextWithButton(parent, toolkit, propertyDescriptor, model);
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}
	
	

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		if(attribute == JavabeanPackage.eINSTANCE.getBindingsType_CreateOnElement()){
			return true;
		}
		return super.isSelectorFeature(attribute);
	}

	protected AttributeFieldEditPart createBeanClassTextWithButton(Composite composite, FormToolkit toolkit,
			final IItemPropertyDescriptor propertyDescriptor, final Object model) {
		return SmooksUIUtils.createJavaTypeSearchFieldEditor(composite, toolkit, propertyDescriptor, (EObject)model);
	}
}

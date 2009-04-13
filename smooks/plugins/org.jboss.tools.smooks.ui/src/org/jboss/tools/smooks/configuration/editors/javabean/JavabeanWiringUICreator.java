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
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 9, 2009
 */
public class JavabeanWiringUICreator extends PropertiesAndSetterMethodSearchFieldEditorCreator {

	@Override
	protected boolean canCreatePropertiesSearchFieldEditor(EAttribute feature) {
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_Property()) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean canCreateMethodsSearchFieldEditor(EAttribute feature) {
		if (feature == JavabeanPackage.eINSTANCE.getWiringType_SetterMethod()) {
			return true;
		}
		return super.canCreateMethodsSearchFieldEditor(feature);
	}
	
	

	@Override
	public boolean isBeanIDRefFieldFeature(EAttribute attribute) {
		if(attribute == JavabeanPackage.eINSTANCE.getWiringType_BeanIdRef()){
			return true;
		}
		return super.isBeanIDRefFieldFeature(attribute);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		if(attribute == JavabeanPackage.eINSTANCE.getWiringType_WireOnElement()){
			return true;
		}
		return super.isSelectorFeature(attribute);
	}

	@Override
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent,
		IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
		SmooksMultiFormEditor formEditor) {
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature,
			formEditor);
	}
}

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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.javabean.BindingsType;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 9, 2009
 */
public class PropertiesAndSetterMethodSearchFieldEditorCreator extends PropertyUICreator {

	@Override
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {
		if (canCreatePropertiesSearchFieldEditor(feature)) {
			return createPropertiesSearchFieldEditor(toolkit, parent, propertyDescriptor, model);
		}
		if (canCreateMethodsSearchFieldEditor(feature)) {
			return createMethodsSearchFieldEditor(toolkit, parent, propertyDescriptor, model);
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor,part);
	}

	protected boolean canCreatePropertiesSearchFieldEditor(EAttribute feature) {
		return false;
	}

	protected boolean canCreateMethodsSearchFieldEditor(EAttribute feature) {
		return false;
	}

	protected String getPropertiesSearchButtonName() {
		return "Browse";
	}

	protected String getMethodsSearchButtonName() {
		return "Browse";
	}

	protected AttributeFieldEditPart createPropertiesSearchFieldEditor(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model) {
		if (model instanceof EObject) {
			EObject container = ((EObject) model).eContainer();
			if (container instanceof BindingsType) {
				return SmooksUIUtils.createJavaPropertySearchFieldEditor((BindingsType) container, parent, toolkit,
						propertyDescriptor, getPropertiesSearchButtonName(), (EObject) model);
			}
		}
		return null;
	}

	protected AttributeFieldEditPart createMethodsSearchFieldEditor(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model) {
		if (model instanceof EObject) {
			EObject container = ((EObject) model).eContainer();
			if (container instanceof BindingsType) {
				return SmooksUIUtils.createJavaMethodSearchFieldEditor((BindingsType) container, parent, toolkit,
						propertyDescriptor, getMethodsSearchButtonName(), (EObject) model);
			}
		}
		return null;
	}

}

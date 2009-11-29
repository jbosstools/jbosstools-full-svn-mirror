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
package org.jboss.tools.smooks.configuration.editors.groovy;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class GroovyUICreator extends PropertyUICreator {

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
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {
		if (feature == GroovyPackage.eINSTANCE.getGroovy_Imports()) {
		}
		if (feature == GroovyPackage.eINSTANCE.getGroovy_Script()) {
		}
		if (feature == GroovyPackage.eINSTANCE.getGroovy_ExecuteBefore()) {
		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor,part);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		if (attribute == GroovyPackage.eINSTANCE.getGroovy_ExecuteOnElement()) {
			return true;
		}
		return super.isSelectorFeature(attribute);
	}

	@Override
	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart part) {
		return createElementSelectionSection(Messages.GroovyUICreator_ExecuteOnElemenGroupName, editingdomain, toolkit, parent, model, formEditor,part,
				GroovyPackage.eINSTANCE.getGroovy_ExecuteOnElement(), GroovyPackage.eINSTANCE
						.getGroovy_ExecuteOnElementNS());
		// SmooksUIUtils.createCommentFieldEditor("Script Contents",editingdomain,
		// toolkit, parent, model);
	}

	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == GroovyPackage.eINSTANCE.getGroovy_ExecuteOnElement()) {
			return true;
		}
		if (feature == GroovyPackage.eINSTANCE.getGroovy_ExecuteOnElementNS()) {
			return true;
		}
		return super.ignoreProperty(feature);
	}
}
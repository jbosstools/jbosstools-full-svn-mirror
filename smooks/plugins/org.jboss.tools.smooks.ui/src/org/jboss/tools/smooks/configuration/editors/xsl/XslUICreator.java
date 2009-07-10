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
package org.jboss.tools.smooks.configuration.editors.xsl;

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
import org.jboss.tools.smooks.model.xsl.XslPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class XslUICreator extends PropertyUICreator {

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
			ISmooksModelProvider formEditor , IEditorPart editorPart) {
		if (feature == XslPackage.eINSTANCE.getXsl_ApplyBefore()) {
		}
		if (feature == XslPackage.eINSTANCE.getXsl_ApplyOnElementNS()) {
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor , editorPart);
	}

	@Override
	public boolean isSelectorFeature(EAttribute attribute) {
		return super.isSelectorFeature(attribute);
	}

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
	// public List<AttributeFieldEditPart>
	// createExtendUIOnTop(AdapterFactoryEditingDomain editingdomain,
	// FormToolkit toolkit,
	// Composite parent, Object model, ISmooksModelProvider formEditor) {
	// return createElementSelectionSection("Apply On Element", editingdomain,
	// toolkit, parent, model, formEditor,
	// XslPackage.Literals.XSL__APPLY_ON_ELEMENT,
	// XslPackage.Literals.XSL__APPLY_ON_ELEMENT_NS);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.configuration.editors.PropertyUICreator#ignoreProperty
	 * (org.eclipse.emf.ecore.EAttribute)
	 */
	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == XslPackage.eINSTANCE.getXsl_ApplyOnElement()) {
			return true;
		}
		if (feature == XslPackage.eINSTANCE.getXsl_ApplyOnElementNS()) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.PropertyUICreator#
	 * createExtendUIOnTop
	 * (org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain,
	 * org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite, java.lang.Object,
	 * org.jboss.tools.smooks.editor.ISmooksModelProvider)
	 */
	@Override
	public List<AttributeFieldEditPart> createExtendUIOnTop(AdapterFactoryEditingDomain editingDomain,
			FormToolkit formToolkit, Composite detailsComposite, Object model, ISmooksModelProvider formEditor,IEditorPart editorPart) {
		return createElementSelectionSection("Apply On Element", editingDomain, formToolkit, detailsComposite, model, formEditor,editorPart,
				XslPackage.Literals.XSL__APPLY_ON_ELEMENT, XslPackage.Literals.XSL__APPLY_ON_ELEMENT_NS);
	}

}
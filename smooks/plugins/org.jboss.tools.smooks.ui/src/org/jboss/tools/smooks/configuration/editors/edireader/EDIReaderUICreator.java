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
package org.jboss.tools.smooks.configuration.editors.edireader;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class EDIReaderUICreator extends PropertyUICreator {

	public EDIReaderUICreator() {
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
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {
//		if (feature == EdiPackage.eINSTANCE.getEDIReader_Encoding()) {
//		}
//		if (feature == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
//			return SmooksUIUtils.createFileSelectionTextFieldEditor(null, parent, null, toolkit, propertyDescriptor,
//					model, SmooksUIUtils.VALUE_TYPE_VALUE, EDIMapFormEditor.EDITOR_ID, null);
//		}
		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor, part);
	}

	@Override
	public boolean isFileSelectionFeature(EAttribute attribute) {
		// if (attribute == EdiPackage.eINSTANCE.getEDIReader_MappingModel()) {
		// return true;
		// }
		return super.isFileSelectionFeature(attribute);
	}

}
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
package org.jboss.tools.smooks.configuration.editors.freemarker;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class TemplateUICreator extends PropertyUICreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public Composite createPropertyUI(FormToolkit toolkit, Composite parent, IItemPropertyDescriptor propertyDescriptor, Object model,
			EAttribute feature, SmooksMultiFormEditor formEditor) {
		if (feature == FreemarkerPackage.eINSTANCE.getTemplate_Value()) {
		}
		if (feature == FreemarkerPackage.eINSTANCE.getTemplate_Encoding()) {
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor);
	}

	@Override
	public void createExtendUI(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit, Composite parent, Object model,
			SmooksMultiFormEditor formEditor) {
		SmooksUIUtils.createFileSelectionTextFieldEditor("Text Value", parent, editingdomain, toolkit, null, model, SmooksUIUtils.VALUE_TYPE_TEXT,
				null);
		SmooksUIUtils.createCDATAFieldEditor("Template Contents(CDATA)", editingdomain, toolkit, parent, model);
		SmooksUIUtils.createCommentFieldEditor("Template Contents(Comment)", editingdomain, toolkit, parent, model);
	}

	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature == FreemarkerPackage.eINSTANCE.getTemplate_Value()) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

}